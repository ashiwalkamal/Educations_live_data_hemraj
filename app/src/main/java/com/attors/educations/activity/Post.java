package com.attors.educations.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.attors.educations.Helper.Utlity;
import com.attors.educations.Modal.PostModal;
import com.attors.educations.Modal.SettingModal;
import com.attors.educations.Network.Apis;
import com.attors.educations.R;
import com.attors.educations.adapter.PostAdapter;
import com.attors.educations.adapter.SettingAdapter;
import com.attors.educations.databinding.ActivityPostBinding;
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

public class Post extends AppCompatActivity {

   ActivityPostBinding binding;

    private PostAdapter postAdapter;
    private List<PostModal> postModals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_post);

        postModals= new ArrayList<>();
        postModals.add(new PostModal("All course available","this course willl be free",R.drawable.book));
        postModals.add(new PostModal("new course pay","this is online courses",R.drawable.book));
        postModals.add(new PostModal("course by exam","course related to exam",R.drawable.book));
        postModals.add(new PostModal("course by exam","course related to exam",R.drawable.book));
        postModals.add(new PostModal("course by exam","course related to exam",R.drawable.book));
        postModals.add(new PostModal("course by exam","course related to exam",R.drawable.book));
        postModals.add(new PostModal("course by exam","course related to exam",R.drawable.book));

        binding.postseries.setHasFixedSize(true);
        binding.postseries.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
        postAdapter= new PostAdapter(this,postModals);
        binding.postseries.setAdapter(postAdapter);
    }


    public void stock() {

        HashMap<String, String> keys = new HashMap<>();
        keys.put("party_id","");
        Request result= Utlity.post(this, keys, Apis.postdetail);

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.retryOnConnectionFailure();
        okHttpClient.connectTimeoutMillis();
        okHttpClient.newCall (result).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Utlity.dismiss_dilog(Post.this);
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, final @NonNull Response response) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        JSONObject object = null;
                        try {
                            Utlity.dismiss_dilog(Post.this);
                            object = new JSONObject(response.body().string());
                            if(object.getString("success").equalsIgnoreCase("1")) {
//                                list = Utlity.gson.fromJson(object.getJSONArray("business").toString(), new TypeToken<List<Stockmodel>>() {}.getType());
//                                partywiseBinding.creatdetail.setHasFixedSize(true);
//                                partywiseBinding.creatdetail.setLayoutManager(new LinearLayoutManager(Partywise.this, RecyclerView.VERTICAL, false));
//                                stockadapter = new Stockadapter(list,Partywise.this);
//                                partywiseBinding.creatdetail.setAdapter(stockadapter);



                            }
                            else
                            {
                                Utlity.show_toast(Post.this,object.getString("text"));
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
