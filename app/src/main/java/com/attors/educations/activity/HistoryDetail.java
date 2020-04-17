package com.attors.educations.activity;

import android.os.Bundle;
import android.view.View;

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

import com.attors.educations.Helper.Utlity;
import com.attors.educations.Modal.Blog_Catgory;
import com.attors.educations.Modal.History;
import com.attors.educations.Modal.NewsModal;
import com.attors.educations.Modal.Notice;
import com.attors.educations.Network.Apis;
import com.attors.educations.R;
import com.attors.educations.adapter.HistoryAdapter;
import com.attors.educations.adapter.NoticeAdapter;
import com.attors.educations.databinding.ActivityHistoryBinding;
import com.attors.educations.databinding.ActivityNewsBinding;
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

public class HistoryDetail extends AppCompatActivity {

    ActivityHistoryBinding newsitemBinding;
    private HistoryAdapter history;
    private List<History>notice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newsitemBinding = DataBindingUtil.setContentView(this, R.layout.activity_history);
        newsitemBinding.title.setText("Test History");

        newsitemBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (Utlity.is_online(this)) {
            News();
        } else {
            Utlity.show_toast(this, getResources().getString(R.string.nointernet));
        }


        newsitemBinding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utlity.is_online(HistoryDetail.this)) {
                    News();
                } else {
                    Utlity.show_toast(HistoryDetail.this, getResources().getString(R.string.nointernet));
                }

            }
        });

    }


    public void News() {
        HashMap<String, String> keys = new HashMap<>();
        keys.put("student_id", Utlity.get_user(this).getId());
        keys.put("coaching_id", Utlity.get_user(this).getCoaching_id());

        Request result = Utlity.post(this, keys, Apis.test);

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.retryOnConnectionFailure();
        okHttpClient.connectTimeoutMillis();
        okHttpClient.newCall(result).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Utlity.dismiss_dilog(HistoryDetail.this);
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
                            Utlity.dismiss_dilog(HistoryDetail.this);
                            newsitemBinding.swipeRefreshLayout.setRefreshing(false);
                            object = new JSONObject(response.body().string());
                            if (object.getString("success").equalsIgnoreCase("1")) {
                                notice = Utlity.gson.fromJson(object.getJSONArray("history").toString(), new TypeToken<List<History>>() {}.getType());
                                newsitemBinding.history.setLayoutManager(new LinearLayoutManager(HistoryDetail.this, RecyclerView.VERTICAL, false));
                                history = new HistoryAdapter(HistoryDetail.this, notice);
                                newsitemBinding.history.setAdapter(history);
                            } else {
                                Utlity.show_toast(HistoryDetail.this, "failed");
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

}
