package com.attors.educations.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.attors.educations.Helper.Utlity;
import com.attors.educations.Modal.Validation_custome;
import com.attors.educations.Network.Apis;
import com.attors.educations.R;
import com.attors.educations.databinding.ActivityRegisterBinding;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import id.zelory.compressor.Compressor;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Register extends AppCompatActivity implements View.OnClickListener {

    ActivityRegisterBinding registerBinding;
    ArrayList<Validation_custome> fileds;
    String pass,conpass;
    String tocken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBinding= DataBindingUtil.setContentView(this,R.layout.activity_register);
        tocken = FirebaseInstanceId.getInstance().getToken();
        regiterclcik();

        registerBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void regiterclcik(){
        registerBinding.register.setOnClickListener(this);
        registerBinding.creates.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        pass =registerBinding.firstpass.getText().toString().trim();
        conpass =registerBinding.secondpass.getText().toString().trim();

        if (v.getId()==R.id.register){

            fileds = new ArrayList<>();
            fileds.add(new Validation_custome("text", registerBinding.name));
            fileds.add(new Validation_custome("text", registerBinding.email));
            fileds.add(new Validation_custome("text", registerBinding.nomber));
            fileds.add(new Validation_custome("text", registerBinding.firstpass));
            if (!conpass.equals(pass)) {
                registerBinding.secondpass.setError("enter match pass");

            }
            else {


                if (Utlity.validation(this, fileds)) {
                    if (Utlity.is_online(this)) {
                        signup();

                    } else
                        Utlity.show_toast(this, getResources().getString(R.string.nointernet));
                }
            }

        }

       else if (v.getId()==R.id.creates){
            startActivity(new Intent(this, Login.class));

        }




        }




    public void signup() {

        Utlity.show_progress(Register.this);
        MultipartBody.Builder builder = new MultipartBody.Builder();

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Gson gson = new Gson();
        builder.setType(MultipartBody.FORM);
        //builder.addFormDataPart("user_id", Utlity.get_user(this).getId());
        builder.addFormDataPart("name", registerBinding.name.getText().toString());
        builder.addFormDataPart("email", registerBinding.email.getText().toString());
        builder.addFormDataPart("mobile_no", registerBinding.nomber.getText().toString());
        builder.addFormDataPart("password", registerBinding.firstpass.getText().toString());
      //  builder.addFormDataPart("signup_code", registerBinding.code.getText().toString());
        builder.addFormDataPart("tocken", tocken);


        MultipartBody body = builder.build();

        final Request request = new Request.Builder()
                .url(Apis.register)
                .post(body)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.retryOnConnectionFailure();
        okHttpClient.connectTimeoutMillis();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Utlity.dismiss_dilog(Register.this);
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, final @NonNull Response response) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        JSONObject object = null;
                        try {
                            Utlity.dismiss_dilog(Register.this);
                            object = new JSONObject(response.body().string());
                            if (object.getString("success").equalsIgnoreCase("1")) {
                                Utlity.show_toast(Register.this, object.getString("text"));
                                startActivity(new Intent(Register.this, Login.class));
                                finish();
                            } else {
                                Utlity.show_toast(Register.this, object.getString("text"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

        });
    }

}
