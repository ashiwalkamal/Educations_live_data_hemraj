package com.attors.educations.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.attors.educations.Helper.Utlity;
import com.attors.educations.Modal.Validation_custome;
import com.attors.educations.R;
import com.attors.educations.databinding.ActivityRegisterFirstBinding;

import java.util.ArrayList;

public class RegisterFirst extends AppCompatActivity implements View.OnClickListener {

    ActivityRegisterFirstBinding binding;
    ArrayList<Validation_custome> fileds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register_first);

        click();
    }

    public void click(){
        binding.btnnextone.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.btnnextone){


            fileds = new ArrayList<>();
            fileds.add(new Validation_custome("text", binding.editname));
            fileds.add(new Validation_custome("text", binding.editfathername));
            fileds.add(new Validation_custome("text", binding.editfatherprofession));
            fileds.add(new Validation_custome("text", binding.editphone));

            if (Utlity.validation(this, fileds)) {
                if (Utlity.is_online(this)) {
                    startActivity(new Intent(this,RegisterSecond.class));

                } else
                    Utlity.show_toast(this, getResources().getString(R.string.nointernet));
            }

        }

    }


}
