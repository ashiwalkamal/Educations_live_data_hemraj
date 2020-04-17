package com.attors.educations.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.attors.educations.Helper.Utlity;
import com.attors.educations.Modal.Batch_model;
import com.attors.educations.Modal.Blog_Catgory;
import com.attors.educations.Modal.Coaching;
import com.attors.educations.Modal.Coaching_post;
import com.attors.educations.Modal.NewsModal;
import com.attors.educations.Modal.PostModal;
import com.attors.educations.Modal.TestModal;
import com.attors.educations.Network.Apis;
import com.attors.educations.R;
import com.attors.educations.adapter.Batchadapter;
import com.attors.educations.adapter.Coaching_post_Adapter;
import com.attors.educations.adapter.NewsAdapter;
import com.attors.educations.adapter.NewsAdapter2;
import com.attors.educations.adapter.PostAdapter;
import com.attors.educations.adapter.TestAdapter;
import com.attors.educations.databinding.ActivityNewsBinding;
import com.attors.educations.databinding.NewsitemBinding;
import com.attors.educations.fragment.Newsfragments;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class News extends AppCompatActivity {

    ActivityNewsBinding newsitemBinding;
    private ArrayList<Blog_Catgory> category;
    private ArrayList<NewsModal> newsModals;
    String type="";
    public  static News news;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newsitemBinding= DataBindingUtil.setContentView(this,R.layout.activity_news);
        newsitemBinding.title.setText("Current's Updates");
        news=this;
        newsitemBinding.back.setVisibility(View.GONE);

            if (Utlity.is_online(this)) {
                News();
            } else {
                Utlity.show_toast(this, getResources().getString(R.string.nointernet));
            }


        newsitemBinding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                if (Utlity.is_online(News.this)) {
                    News();
                } else {
                    Utlity.show_toast(News.this, getResources().getString(R.string.nointernet));
                }

            }
        });

    }


    public void refresh()
    {
        if (Utlity.is_online(News.this)) {
            News();
        } else {
            Utlity.show_toast(News.this, getResources().getString(R.string.nointernet));
        }

    }


    public void News() {
        HashMap<String, String> keys = new HashMap<>();
        if(MainActivity.type==1) {
            keys.put("id", Utlity.get_user(this).getCoaching_id());
            MainActivity.type=0;
        }
        else {
            keys.put("id", "0");
        }
        Request result= Utlity.post(this, keys, Apis.newsblog);

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.retryOnConnectionFailure();
        okHttpClient.connectTimeoutMillis();
        okHttpClient.newCall (result).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Utlity.dismiss_dilog(News.this);
                        newsitemBinding.swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(@NonNull final Call call, final @NonNull Response response) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        JSONObject object = null;
                        try {
                            Utlity.dismiss_dilog(News.this);
                            newsitemBinding.swipeRefreshLayout.setRefreshing(false);
                            object = new JSONObject(response.body().string());
                            if(object.getString("success").equalsIgnoreCase("1")) {

                               newsModals = Utlity.gson.fromJson(object.getJSONObject("text").getJSONArray("blogs").toString(), new TypeToken<List<NewsModal>>() {}.getType());
                               category = Utlity.gson.fromJson(object.getJSONObject("text").getJSONArray("category").toString(), new TypeToken<List<Blog_Catgory>>() {}.getType());

                               if(category.size()!=0) {
                                   // Setting ViewPager for each Tabs
                                   setupViewPager(newsitemBinding.viewpager,newsModals,category);
                                   // Set Tabs inside Toolbar
                                   newsitemBinding.resultTabs.setupWithViewPager(newsitemBinding.viewpager);
                               }

                            }
                            else
                            {
                                Utlity.show_toast(News.this,"failed");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

        });
    }



    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager,ArrayList<NewsModal> newsModals,ArrayList<Blog_Catgory> category) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.notifyDataSetChanged();
        for(Blog_Catgory catgory:category) {
            Newsfragments newsfragments=new Newsfragments();
            Bundle bundle = new Bundle();
            bundle.putString("blogs",Utlity.gson.toJson(get_blogs(catgory.getId(),newsModals)));
            newsfragments.setArguments(bundle);
            adapter.addFragment(newsfragments, catgory.getCategory_name());
        }
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

private ArrayList<NewsModal> get_blogs(String cat,ArrayList<NewsModal> blogs)
{
    ArrayList<NewsModal> catg_blog=new ArrayList<>();
    for(NewsModal modal:blogs)
    {
        if(modal.getCate_id().equalsIgnoreCase(cat))
        {
            catg_blog.add(modal);
        }
    }
    return catg_blog;
}
    @Override
    public void onBackPressed() {
        MainActivity.tabHost.setCurrentTab(0);
    }

}
