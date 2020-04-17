package com.attors.educations.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.attors.educations.Helper.Utlity;
import com.attors.educations.Modal.Coaching;
import com.attors.educations.Network.Apis;
import com.attors.educations.R;
import com.attors.educations.databinding.ActivityCoachingBinding;

public class Coaching_Details extends AppCompatActivity {
    Coaching coaching;
    ActivityCoachingBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_coaching);
        if(getIntent()!=null)
        {
            coaching= Utlity.gson.fromJson(getIntent().getStringExtra("detail"),Coaching.class);
            //to set value  of coaching
            set_values(coaching);
        }
    }

    private void set_values(Coaching coaching) {
        Utlity.Set_image(coaching.getCoaching_banner(),binding.backimage);
        Utlity.Set_image(coaching.getCoaching_logo(),binding.logo);
        binding.coacnme.setText(coaching.getName());
        binding.coachnumber.setText(coaching.getMobile_no());
        binding.coacaddress.setText(coaching.getCoaching_address());
        binding.aboutinfo.setText(coaching.getAbout_coaching());
    }

    public void direction(View view) {
        String map = "http://maps.google.co.in/maps?q=" + coaching.getCoaching_address();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
        startActivity(intent);
    }

    public void call(View view) {
        Utlity.call(this,binding.coachnumber.getText().toString());
    }
}
