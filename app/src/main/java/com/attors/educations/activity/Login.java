package com.attors.educations.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.attors.educations.Helper.Utlity;
import com.attors.educations.Modal.Validation_custome;
import com.attors.educations.Network.Apis;
import com.attors.educations.R;
import com.attors.educations.databinding.ActivityLoginBinding;
import com.attors.educations.databinding.ActivitySuccessfullActivityBinding;
import com.google.android.gms.common.api.Api;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Login extends AppCompatActivity implements View.OnClickListener {

      ActivityLoginBinding binding;
      ArrayList<Validation_custome> fileds;
      String email,password;
      String tocken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        tocken = FirebaseInstanceId.getInstance().getToken();

//        Intent intent = getIntent();
//        String text = intent.getStringExtra("pass");
//        Log.d("responce2>>>>>",text);
//        binding.editloginemail.setText(text);


        click();


    }

    public void click(){
        binding.btnlogin.setOnClickListener(this);
        binding.create.setOnClickListener(this);
        binding.forgotpass.setOnClickListener(this);
        binding.back.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {

        if (v.getId()==R.id.btnlogin){

            email =binding.editloginemail.getText().toString();
            password=binding.editloginpassword.getText().toString();

            fileds = new ArrayList<>();
            fileds.add(new Validation_custome("text", binding.editloginemail));
            fileds.add(new Validation_custome("text", binding.editloginpassword));

            if (Utlity.validation(this, fileds)) {
                if (Utlity.is_online(this)) {
                    dologin(email, password);


                } else
                    Utlity.show_toast(this, getResources().getString(R.string.nointernet));
            }

        }
        else if(v.getId()==R.id.back)
        {
            onBackPressed();
        }

        else if (v.getId()==R.id.create){
            startActivity(new Intent(this,Register.class));

        }

        else if (v.getId()==R.id.forgotpass){
            startActivity(new Intent(this,Forogot.class));

        }


    }

    private void dologin(final String email, String password) {
        HashMap<String, String> keys = new HashMap<>();
        keys.put("email", email);
        keys.put("password", password);
        keys.put("tocken", tocken);

        Request result= Utlity.post(this, keys, Apis.login);
        OkHttpClient okHttpClient= new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Utlity.dismiss_dilog(Login.this);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Utlity.dismiss_dilog(Login.this);
                            String apidata = response.body().string();
                            Log.d("responce>>>>>",apidata);
                            JSONObject object = new JSONObject(apidata);
                            if (object.getString("success").equalsIgnoreCase("1")){
                                Utlity.user_db(Login.this,object.getJSONObject("student_info").toString());
                                startActivity(new Intent(Login.this, MainActivity.class));
                                finishAffinity();

                            }
                            else {
                                Utlity.show_toast(Login.this,object.getString("text"));
                            }
                        } catch (IOException e){
                            e.printStackTrace();
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                });

            }
        });

    }

}
