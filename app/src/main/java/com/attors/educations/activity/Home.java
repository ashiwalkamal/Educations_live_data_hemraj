package com.attors.educations.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.NotProvisionedException;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.attors.educations.adapter.NewsAdapter;
import com.attors.educations.adapter.NewsAdapter2;
import com.attors.educations.adapter.NoticeAdapter;
import com.attors.educations.adapter.PostAdapter;
import com.attors.educations.adapter.SettingAdapter;
import com.attors.educations.adapter.TestAdapter;
import com.attors.educations.databinding.ActivityHomeBinding;
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

public class Home extends AppCompatActivity implements View.OnClickListener {
    ActivityHomeBinding homeBinding;
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
        homeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        homeBinding.title.setText("Welcome " + Utlity.get_user(this).getName());
        builder = new AlertDialog.Builder(this);
        drawerLayout=homeBinding.drawerLayout;
        homeBinding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_open=true;
                open_drawer();
            }
        });

        if (!Utlity.is_empty(Utlity.get_user(this).getExam_id())) {
            StringTokenizer st = new StringTokenizer(Utlity.get_user(this).getExam_id(), ",");

            // create ArrayList object
            elements.clear();
            // iterate through StringTokenizer tokens
            while (st.hasMoreTokens()) {
                // add tokens to AL
                elements.add(st.nextToken());
            }
        }

        click2();


        if (Utlity.is_online(this)) {
            Postdata();
        } else {
            Utlity.show_toast(this, getResources().getString(R.string.nointernet));
        }


        homeBinding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utlity.is_online(Home.this)) {
                    Postdata();
                } else {
                    Utlity.show_toast(Home.this, getResources().getString(R.string.nointernet));
                }

            }
        });


    }

    private void setupmenu(Coaching coaching) {
        settingModals= new ArrayList<>();
        settingModals.add(new SettingModal("Home",R.drawable.ic_home_active));
        settingModals.add(new SettingModal("About Us",R.drawable.ic_info_black_24dp));
        settingModals.add(new SettingModal("Contact us",R.drawable.ic_help));
        settingModals.add(new SettingModal("Staff Detail",R.drawable.ic_person_active));
        settingModals.add(new SettingModal("Update's",R.drawable.ic_notifications_active));
        menus=(RecyclerView)drawerLayout.findViewById(R.id.menus);
        stname=(TextView) drawerLayout.findViewById(R.id.username);
        stimage=(ImageView) drawerLayout.findViewById(R.id.photo);
        menus.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
        menuAdapter= new MenuAdapter(this,settingModals,coaching);
        menus.setAdapter(menuAdapter);

        Utlity.Set_image(Utlity.get_user(this).getImage(),stimage);
        stname.setText(Utlity.get_user(this).getName());
    }

    private void profile_complte_alert() {
        builder = new AlertDialog.Builder(this);
        //Setting message manually and performing action on button click
        builder.setMessage("Your profile not complete do you want to complete ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        startActivity(new Intent(Home.this, Profiles.class));
                    }
                });

        //Creating dialog box
        AlertDialog alert = builder.create();

        alert.show();
    }

    public void click2() {
        homeBinding.logo.setOnClickListener(this);
        homeBinding.notication.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.logo) {
            startActivity(new Intent(Home.this, Coaching_Details.class).putExtra("detail", Utlity.gson.toJson(coaching)));
        }
        else if(v.getId()==R.id.notication)
        {
            startActivity(new Intent(Home.this, Notification.class));

        }

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
                        Utlity.dismiss_dilog(Home.this);
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
                            Utlity.dismiss_dilog(Home.this);
                            homeBinding.swipeRefreshLayout.setRefreshing(false);
                            object = new JSONObject(response.body().string());

                            if (object.getString("success").equalsIgnoreCase("1")) {

                                coaching = Utlity.gson.fromJson(object.getJSONObject("coaching_info").toString(), Coaching.class);

                                //setup menu
                                setupmenu(coaching);

                                Utlity.Set_image(coaching.getCoaching_banner(), homeBinding.background);
                                Utlity.Set_image(coaching.getCoaching_logo(), homeBinding.logo);

                                homeBinding.coachingname.setText(coaching.getName());

                                tests = Utlity.gson.fromJson(object.getJSONArray("test_info").toString(), new TypeToken<List<TestModal>>() {
                                }.getType());
                                homeBinding.tests.setLayoutManager(new LinearLayoutManager(Home.this, RecyclerView.HORIZONTAL, false));
                                testadpater = new TestAdapter(Home.this, tests);
                                homeBinding.tests.setAdapter(testadpater);
                                if (tests.size() == 0) {
                                    homeBinding.llexam.setVisibility(View.GONE);
                                } else {
                                    homeBinding.llexam.setVisibility(View.VISIBLE);
                                }

                                batchs = Utlity.gson.fromJson(object.getJSONArray("batch").toString(), new TypeToken<List<Batch_model>>() {
                                }.getType());

                                homeBinding.batch.setLayoutManager(new LinearLayoutManager(Home.this, RecyclerView.HORIZONTAL, false));
                                batchadpater = new Batchadapter(Home.this, batchs, elements);
                                homeBinding.batch.setAdapter(batchadpater);
                                if (batchs.size() == 0) {
                                    homeBinding.llbatch.setVisibility(View.GONE);
                                } else {
                                    homeBinding.llbatch.setVisibility(View.VISIBLE);
                                }


                            } else {
                                Utlity.show_toast(Home.this, "failed");
                            }

                        } catch (JSONException e) {
                            Utlity.dismiss_dilog(Home.this);
                            homeBinding.swipeRefreshLayout.setRefreshing(false);
                            e.printStackTrace();
                        } catch (IOException e) {
                            Utlity.dismiss_dilog(Home.this);
                            homeBinding.swipeRefreshLayout.setRefreshing(false);
                            e.printStackTrace();
                        }
                    }
                });
            }

        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) < 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.d("CDA", "onKeyDown Called");
            onBackPressed();
        }

        return super.onKeyDown(keyCode, event);
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onBackPressed() {
        if (homeBinding.drawerLayout.isDrawerOpen(Gravity.START)) {
            open_drawer();
        } else {

            alert();

        }
    }

    private void alert() {
        //Setting message manually and performing action on button click
        builder.setMessage("Do you want to close this application ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.show();
    }

    @SuppressLint("WrongConstant")
    private void open_drawer() {
        if (homeBinding.drawerLayout.isDrawerOpen(Gravity.START)) {
            homeBinding.drawerLayout.closeDrawer(Gravity.START);
        } else {
            homeBinding.drawerLayout.openDrawer(Gravity.START);

        }
    }

}
