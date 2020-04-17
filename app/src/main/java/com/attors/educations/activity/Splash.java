package com.attors.educations.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.attors.educations.Helper.Utlity;
import com.attors.educations.R;

public class Splash extends AppCompatActivity {

    Thread splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        splash = new Thread() {
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if(!TextUtils.isEmpty(Utlity.get_user(Splash.this).getId())) {
                        startActivity(new Intent(Splash.this, MainActivity.class));
                    }
                    else
                    {
                        startActivity(new Intent(Splash.this, Login.class));
                    }

                    finishAffinity();



                }
            }

        };splash.start();
    }
}