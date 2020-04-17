package com.attors.educations.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;

import com.attors.educations.Helper.Utlity;
import com.attors.educations.Modal.Validation_custome;
import com.attors.educations.R;
import com.attors.educations.databinding.ActivityRegisterFinalBinding;

import java.util.ArrayList;

public class RegisterFinal extends AppCompatActivity implements View.OnClickListener {

     ActivityRegisterFinalBinding binding;
    ArrayList<Validation_custome> fileds;


    String[] Coachingduration = {"AM", "PM"};
    String[] CoachingName = {"Desire Coaching", "MPS Coaching", "AR Coaching","Perfect Education", "Perfect Hub"};
    String dob;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register_final);



        ArrayAdapter<String> myAdapter = new
                ArrayAdapter<String>(RegisterFinal.this, android.R.layout.simple_spinner_item, Coachingduration);
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinampm.setAdapter(myAdapter);

        ArrayAdapter<String> Adapter = new
                ArrayAdapter<String>(RegisterFinal.this, android.R.layout.simple_spinner_item, CoachingName);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spincoaching.setAdapter(Adapter);


        allclick();

    }

    public void allclick(){
        binding.btnregister.setOnClickListener(this);
        binding.backfinal.setOnClickListener(this);
        binding.editdate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        dob=binding.editdate.getText().toString();

        if (v.getId()==R.id.btnregister){

            fileds = new ArrayList<>();
            fileds.add(new Validation_custome("text", binding.editCourse));
            fileds.add(new Validation_custome("text", binding.editwhichcourse));

            if (Utlity.validation(this, fileds)) {
                if (binding.spincoaching.getSelectedItemPosition() == 0) {
                    Utlity.show_toast(RegisterFinal.this, "please select coaching");
                } else if (binding.spinampm.getSelectedItemPosition() == 0) {
                    Utlity.show_toast(RegisterFinal.this, "please select time type");
                }

                else if (TextUtils.isEmpty(dob)) {
                    Utlity.show_toast(RegisterFinal.this, "select date!");
                }


                else if (Utlity.is_online(this)) {
                    startActivity(new Intent(RegisterFinal.this,Successfull_activity.class));

                } else
                    Utlity.show_toast(this, getResources().getString(R.string.nointernet));
            }

        }
        else if (v.getId()==R.id.backfinal){
            startActivity(new Intent(RegisterFinal.this,RegisterSecond.class));

        }
        else if (v.getId()==R.id.editdate){
            Utlity.show_date_picker(this,binding.editdate);

        }

    }

}
