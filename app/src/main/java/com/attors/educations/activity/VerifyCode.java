package com.attors.educations.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.attors.educations.R;
import com.attors.educations.databinding.ActivityVerifyCodeBinding;

public class VerifyCode extends AppCompatActivity {

    ActivityVerifyCodeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_verify_code);

        binding.btnverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VerifyCode.this,Login.class));
            }
        });

    }
}
