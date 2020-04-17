package com.attors.educations.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.attors.educations.R;
import com.attors.educations.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends TabActivity {
    public static TabWidget tabs;
    public static TabHost tabHost;
    public static boolean is_open = false;
    public static int type = 0;
    ActivityMainBinding binding;
    AlertDialog.Builder builder;
    private ActionBarDrawerToggle drawerToggle;

    public static TabHost getCurrentTabHost() {
        return tabHost;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        builder = new AlertDialog.Builder(this);

        tabs = findViewById(android.R.id.tabs);

//        drawerToggle = new ActionBarDrawerToggle(this, binding.drawer, R.string.open, R.string.close);
//        drawerToggle.setDrawerIndicatorEnabled(true);
//        binding.drawer.addDrawerListener(drawerToggle);
//        drawerToggle.syncState();
//        //getActionBar().setDisplayHomeAsUpEnabled(true);


//        binding.navigate.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                return false;
//            }
//        });
//


        try {
            setTabs();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

    }

    private void setTabs() {
        addTab("Home", R.drawable.post, Home.class);
        addTab("News", R.drawable.test, News.class);
        addTab("Douts", R.drawable.douts, Douts.class);
        addTab("Live Class", R.drawable.live, Live_class.class);
        //  addTab("Notification",R.drawable.notify,Notification.class);
        addTab("Settings", R.drawable.setting, Settings.class);
    }

    private void addTab(String labelId, int drawableId, Class<?> c) {
        tabHost = getTabHost();
        Intent intent = new Intent(MainActivity.this, c);
        intent.putExtra("header", this.getClass().getName());
        TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);
        View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, getTabWidget(), false);
        TextView title = tabIndicator.findViewById(R.id.tab_title);
        title.setText(labelId);
        ImageView icon = tabIndicator.findViewById(R.id.tab_icon);
        icon.setImageResource(drawableId);
        spec.setIndicator(tabIndicator);
        spec.setContent(intent);

        tabHost.addTab(spec);
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
        if (is_open) {
            Home.drawerLayout.closeDrawer(Gravity.START);
            is_open = false;

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
                        onBackPressed();
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

}






