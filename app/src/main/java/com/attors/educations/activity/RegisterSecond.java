package com.attors.educations.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.attors.educations.Helper.Utlity;
import com.attors.educations.Modal.Validation_custome;
import com.attors.educations.R;
import com.attors.educations.databinding.ActivityRegisterSecondBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class RegisterSecond extends AppCompatActivity implements View.OnClickListener {

    ActivityRegisterSecondBinding binding;
    ArrayList<Validation_custome> fileds;

    boolean doubleBackToExitPressedOnce = false;
    String[] Coachingmedium = {"Hindi Medium", "English Medium"};
    String dob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register_second);

        ArrayAdapter<String> myAdapters = new
                ArrayAdapter<String>(RegisterSecond.this, android.R.layout.simple_spinner_item, Coachingmedium);
        myAdapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinmedium.setAdapter(myAdapters);


        allclick();

    }


    public void allclick(){
        binding.btnnexttwo.setOnClickListener(this);
        binding.backsecond.setOnClickListener(this);
        binding.textdob.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        dob=binding.textdob.getText().toString();
        if (v.getId()==R.id.btnnexttwo){

            fileds = new ArrayList<>();
            fileds.add(new Validation_custome("text", binding.editaddress));
            fileds.add(new Validation_custome("text", binding.editemail));
            fileds.add(new Validation_custome("text", binding.editbatch));

            if (Utlity.validation(this, fileds)) {
                if (binding.spinmedium.getSelectedItemPosition() == 0) {
                    Utlity.show_toast(RegisterSecond.this, "please select medium");
                }

                else if (TextUtils.isEmpty(dob)) {
                    Utlity.show_toast(RegisterSecond.this, "select date!");
                }



                else if (Utlity.is_online(this)) {
                    startActivity(new Intent(this,RegisterFinal.class));

                } else
                    Utlity.show_toast(this, getResources().getString(R.string.nointernet));
            }

        }

        else if (v.getId()==R.id.backsecond){
            startActivity(new Intent(RegisterSecond.this,RegisterFirst.class));

        }


        else if (v.getId()==R.id.textdob){
            Utlity.show_date_picker(this,binding.textdob);

        }


    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

}


