package com.attors.educations.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.attors.educations.R;

public class Douts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_douts);
    }
    @Override
    public void onBackPressed() {
        MainActivity.tabHost.setCurrentTab(0);
    }
}
