package com.attors.educations.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.attors.educations.Helper.Utlity;
import com.attors.educations.Modal.Validation_custome;
import com.attors.educations.Network.Apis;
import com.attors.educations.R;
import com.attors.educations.databinding.ActivityForogotBinding;

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

public class Forogot extends AppCompatActivity implements View.OnClickListener {

    ActivityForogotBinding forogotBinding;
    ArrayList<Validation_custome> fileds;
    String email;
    String data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        forogotBinding = DataBindingUtil.setContentView(this, R.layout.activity_forogot);
        forogtclcik();

    }


    public void forogtclcik() {
        forogotBinding.reset.setOnClickListener(this);
        forogotBinding.back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        email = forogotBinding.emails.getText().toString();
        // password=forogotBinding.newpassword.getText().toString();


        if (v.getId() == R.id.reset) {

            fileds = new ArrayList<>();
            fileds.add(new Validation_custome("text", forogotBinding.emails));
            //fileds.add(new Validation_custome("text", forogotBinding.newpassword));

            if (Utlity.validation(this, fileds)) {
                if (Utlity.is_online(this)) {
                    if (TextUtils.isEmpty(Utlity.get_user(Forogot.this).getEmail())) {
                        forgetPassword(email);

                    } else {
                        Utlity.show_toast(this, "please verify your email ");
                    }


                } else
                    Utlity.show_toast(this, getResources().getString(R.string.nointernet));
            }

        } else if (v.getId() == R.id.back) {
            finish();
        }
    }


    private void forgetPassword(final String email) {
        HashMap<String, String> keys = new HashMap<>();
        keys.put("email", email);

        Request result = Utlity.post(this, keys, Apis.forgot);
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Utlity.dismiss_dilog(Forogot.this);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Utlity.dismiss_dilog(Forogot.this);
                            String apidata = response.body().string();
                            Log.d("responce>>>>>", apidata);
                            JSONObject object = new JSONObject(apidata);
                            if (object.getString("success").equalsIgnoreCase("1")) {
                                alert_dilaog();
                            } else {
                                Utlity.show_toast(Forogot.this, object.getString("text"));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });

    }

    private void alert_dilaog() {
        final Dialog dialog = new Dialog(Forogot.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogbox_forgetpassword);
        dialog.getWindow().setLayout(ViewPager.LayoutParams.FILL_PARENT, ViewPager.LayoutParams.WRAP_CONTENT);
        Button buttonForgetPassword = dialog.findViewById(R.id.btnlogin);
        buttonForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        dialog.show();
    }


}
