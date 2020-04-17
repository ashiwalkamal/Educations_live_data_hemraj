package com.attors.educations.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.attors.educations.Helper.Utlity;
import com.attors.educations.Modal.Batch_model;
import com.attors.educations.Modal.Coaching;
import com.attors.educations.Modal.Coaching_post;
import com.attors.educations.Modal.NewsModal;
import com.attors.educations.Modal.Notice;
import com.attors.educations.Modal.PostModal;
import com.attors.educations.Modal.SettingModal;
import com.attors.educations.Modal.TestModal;
import com.attors.educations.Network.Apis;
import com.attors.educations.R;
import com.attors.educations.adapter.Batchadapter;
import com.attors.educations.adapter.Coaching_post_Adapter;
import com.attors.educations.adapter.MenuAdapter;
import com.attors.educations.adapter.NoticeAdapter;
import com.attors.educations.adapter.PostAdapter;
import com.attors.educations.adapter.TestAdapter;
import com.attors.educations.databinding.ActivityHomeBinding;
import com.attors.educations.databinding.ActivityNotificationBinding;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.attors.educations.activity.MainActivity.is_open;

public class Notification extends AppCompatActivity  {
    ActivityNotificationBinding homeBinding;
    AlertDialog.Builder builder;
    List<String> elements = new ArrayList<String>();
    boolean doubleBackToExitPressedOnce = false;
    private PostAdapter postAdapter;
    private List<PostModal> postModals;
    private Coaching coaching;
    private ArrayList<NewsModal> posts;
    private ArrayList<TestModal> tests;
    private ArrayList<Batch_model> batchs;
    private Coaching_post_Adapter newsadpater;
    private TestAdapter testadpater;
    private Batchadapter batchadpater;
    private Coaching_post coaching_post;
    public static DrawerLayout drawerLayout;
    private MenuAdapter menuAdapter;
    private ArrayList<SettingModal> settingModals;
    private RecyclerView menus;
    private TextView stname;
    private ImageView stimage;
    private ArrayList<Notice> notice;
    private NoticeAdapter noticeAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeBinding = DataBindingUtil.setContentView(this, R.layout.activity_notification);
        homeBinding.title.setText("Notification");

        if (Utlity.is_online(this)) {
            Postdata();
        } else {
            Utlity.show_toast(this, getResources().getString(R.string.nointernet));
        }

        homeBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        homeBinding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utlity.is_online(Notification.this)) {
                    Postdata();
                } else {
                    Utlity.show_toast(Notification.this, getResources().getString(R.string.nointernet));
                }

            }
        });


    }


    public void Postdata() {
        HashMap<String, String> keys = new HashMap<>();
        keys.put("id", Utlity.get_user(this).getCoaching_id());
        //keys.put("sid", Utlity.get_user(this).getId());
        keys.put("eid", Utlity.get_user(this).getExam_id());
        Request result = Utlity.post(this, keys, Apis.postdetail);

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.retryOnConnectionFailure();
        okHttpClient.connectTimeoutMillis();
        okHttpClient.newCall(result).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Utlity.dismiss_dilog(Notification.this);
                        homeBinding.swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, final @NonNull Response response) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        JSONObject object = null;
                        try {
                            Utlity.dismiss_dilog(Notification.this);
                            homeBinding.swipeRefreshLayout.setRefreshing(false);
                            object = new JSONObject(response.body().string());

                            if (object.getString("success").equalsIgnoreCase("1")) {


                                notice = Utlity.gson.fromJson(object.getJSONArray("notice").toString(), new TypeToken<List<Notice>>() {}.getType());

                                homeBinding.newsdetail.setLayoutManager(new LinearLayoutManager(Notification.this, RecyclerView.VISIBLE, false));
                                noticeAdapter = new NoticeAdapter(Notification.this, notice);
                                homeBinding.newsdetail.setAdapter(noticeAdapter);


                            } else {
                                Utlity.show_toast(Notification.this, "failed");
                            }

                        } catch (JSONException e) {
                            Utlity.dismiss_dilog(Notification.this);
                            homeBinding.swipeRefreshLayout.setRefreshing(false);
                            e.printStackTrace();
                        } catch (IOException e) {
                            Utlity.dismiss_dilog(Notification.this);
                            homeBinding.swipeRefreshLayout.setRefreshing(false);
                            e.printStackTrace();
                        }
                    }
                });
            }

        });
    }

}
