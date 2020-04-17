package com.attors.educations.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.attors.educations.R;
import com.attors.educations.databinding.ActivitySuccessfullActivityBinding;

public class Successfull_activity extends AppCompatActivity {

    ActivitySuccessfullActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_successfull_activity);

       binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Successfull_activity.this,VerifyCode.class));
            }
        });

    }
}
