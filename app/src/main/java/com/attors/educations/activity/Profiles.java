package com.attors.educations.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.attors.educations.Helper.Utlity;
import com.attors.educations.Modal.Batch_model;
import com.attors.educations.Modal.Validation_custome;
import com.attors.educations.Network.Apis;
import com.attors.educations.R;
import com.attors.educations.adapter.Batchadapter2;
import com.attors.educations.adapter.TestAdapterone;
import com.attors.educations.databinding.ActivityProfilesBinding;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;

import id.zelory.compressor.Compressor;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Profiles extends AppCompatActivity implements View.OnClickListener {

    private static final int RESULT_LOAD_IMAGE1 = 1;
    ActivityProfilesBinding profilesBinding;
    String[] Coachingduration = {"AM", "PM"};
    String dob, dte;
    ArrayList<Validation_custome> fileds;
    ArrayList<String> pic = null;
    TestAdapterone testAdapterone;
    String exam_id = "";
    String meduim = "";
    StringTokenizer st;
    // create ArrayList object
    List<String> elements = new ArrayList<String>();
    private ArrayList<Batch_model> batchs;
    private String[] ids;
    private String examids = "";
    private String batch_name = "";
    private Batchadapter2 batchadpater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profilesBinding = DataBindingUtil.setContentView(this, R.layout.activity_profiles);

        profilesBinding.backicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (Utlity.is_online(this)) {


            profilesBinding.usenme.setText(Utlity.get_user(this).getName());
            profilesBinding.usenumber.setText(Utlity.get_user(this).getMobile_no());

            Utlity.Set_image(Utlity.get_user(this).getImage(), profilesBinding.img);

            profilesBinding.editfathername.setText(Utlity.get_user(this).getFather_name());
            profilesBinding.editfatherprofession.setText(Utlity.get_user(this).getFather_profession());
            profilesBinding.textdob.setText(Utlity.get_user(this).getDate_of_birth());
            profilesBinding.address.setText(Utlity.get_user(this).getAddress());
            if (Utlity.get_user(this).getMedium().equalsIgnoreCase("Hindi medium")) {
                profilesBinding.hindi.setChecked(true);
                meduim = "Hindi medium";
            } else if (Utlity.get_user(this).getMedium().equalsIgnoreCase("English medium")) {
                profilesBinding.english.setChecked(true);
                meduim = "English medium";
            }

            if (!Utlity.is_empty(Utlity.get_user(this).getExam_id())) {
                StringTokenizer st = new StringTokenizer(Utlity.get_user(this).getExam_id(), ",");

                // create ArrayList object
                elements.clear();
                // iterate through StringTokenizer tokens
                while (st.hasMoreTokens()) {
                    // add tokens to AL
                    elements.add(st.nextToken());
                }
            }

            getAll();

        } else {
            Utlity.show_toast(this, getResources().getString(R.string.nointernet));
        }

        batch_name = Utlity.get_user(this).getBatch_name();

        clicker();

        pic = new ArrayList<>();

        profilesBinding.meduim.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.hindi) {
                    meduim = "Hindi medium";
                } else {
                    meduim = "English medium";
                }
            }
        });

        profilesBinding.signupcodech.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    profilesBinding.signupcode.setVisibility(View.VISIBLE);
                } else {
                    profilesBinding.signupcode.setVisibility(View.GONE);
                }
            }
        });


    }

    public void clicker() {
        profilesBinding.editdate.setOnClickListener(this);
        profilesBinding.textdob.setOnClickListener(this);
        profilesBinding.update.setOnClickListener(this);
        profilesBinding.photo.setOnClickListener(this);
        profilesBinding.backicon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.update) {
            dob = profilesBinding.textdob.getText().toString();
            //dte=profilesBinding.editdate.getText().toString();

            fileds = new ArrayList<>();
            if (profilesBinding.signupcodech.isChecked()) {
                fileds.add(new Validation_custome("text", profilesBinding.signupcode));
            }
            //fileds.add(new Validation_custome("text", profilesBinding.editbatch));
            //fileds.add(new Validation_custome("text", profilesBinding.editCourse));
            //fileds.add(new Validation_custome("text", profilesBinding.editwhichcourse));

            if (Utlity.validation(this, fileds)) {
                if (Utlity.is_online(this)) {
                    new AsyncTaskRunner().execute();

                } else {
                    Utlity.show_toast(this, getResources().getString(R.string.nointernet));
                }
            }


        } else if (v.getId() == R.id.textdob) {
            Utlity.show_date_picker(this, profilesBinding.textdob);
        } else if (v.getId() == R.id.photo) {

            open_iamge_picker();
        } else if (v.getId() == R.id.backicon) {
            onBackPressed();

        }

    }


    private void open_iamge_picker() {

        Pix.start(this, RESULT_LOAD_IMAGE1, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Pix.start(this, RESULT_LOAD_IMAGE1, 1);
                    profilesBinding.img.setEnabled(true);
                }
                return;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE1 && resultCode == RESULT_OK && null != data) {
            pic.clear();
            pic.addAll(Objects.requireNonNull(data.getStringArrayListExtra(Pix.IMAGE_RESULTS)));
            profilesBinding.img.setImageURI(Uri.parse(pic.get(0)));
        }
    }


    private void update() {

        Utlity.show_progress(this);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        if (pic.size() != 0) {
            File userimgs = new File(pic.get(0));
            try {
                File now_file = new Compressor(this).compressToFile(userimgs);
                builder.addFormDataPart("old_file", now_file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), now_file));

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            builder.addFormDataPart("old_file", "");

        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Gson gson = new Gson();
        builder.setType(MultipartBody.FORM);

        builder.addFormDataPart("id", Utlity.get_user(this).getId());
        builder.addFormDataPart("exam_id", exam_id);
        builder.addFormDataPart("name", profilesBinding.usenme.getText().toString());
        builder.addFormDataPart("mobile_no", profilesBinding.usenumber.getText().toString());
        builder.addFormDataPart("father_name", profilesBinding.editfathername.getText().toString());
        builder.addFormDataPart("father_profession", "0");
        builder.addFormDataPart("medium", meduim);
        builder.addFormDataPart("date_of_birth", profilesBinding.textdob.getText().toString());
        builder.addFormDataPart("address", profilesBinding.address.getText().toString());
        builder.addFormDataPart("batch_name", batch_name);
        if(profilesBinding.signupcodech.isChecked()) {
            builder.addFormDataPart("signup_code", profilesBinding.signupcode.getText().toString());
        }
        else
        {
            builder.addFormDataPart("signup_code", "");
        }
        builder.addFormDataPart("coaching_id", Utlity.get_user(this).getCoaching_id());
        builder.addFormDataPart("course", "");
        builder.addFormDataPart("course_addmission_desired", "");
        builder.addFormDataPart("date", "");
        builder.addFormDataPart("clock_time", "");

        MultipartBody body = builder.build();

        final Request request = new Request.Builder()
                .url(Apis.update)
                .post(body)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.retryOnConnectionFailure();
        okHttpClient.connectTimeoutMillis();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Utlity.dismiss_dilog(Profiles.this);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Utlity.dismiss_dilog(Profiles.this);
                            String apidata = response.body().string();
                            Log.d("responce>>>>>", apidata);
                            JSONObject object = new JSONObject(apidata);
                            if (object.getInt("success") == 1) {
                                Utlity.user_db(Profiles.this, object.getJSONObject("student_info").toString());
                                Utlity.show_toast(Profiles.this, object.getString("text"));

                            } else {
                                Utlity.show_toast(Profiles.this, object.getString("text"));
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


    public void getAll() {

        HashMap<String, String> keys = new HashMap<>();
        keys.put("id", Utlity.get_user(this).getCoaching_id());
        keys.put("eid", Utlity.get_user(this).getExam_id());
        Request result = Utlity.post(this, keys, Apis.postdetail);

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.retryOnConnectionFailure();
        okHttpClient.connectTimeoutMillis();
        okHttpClient.newCall(result).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Utlity.dismiss_dilog(Profiles.this);
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, final @NonNull Response response) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        JSONObject object = null;
                        try {
                            Utlity.dismiss_dilog(Profiles.this);
                            object = new JSONObject(response.body().string());
                            if (object.getString("success").equalsIgnoreCase("1")) {

                                batchs = Utlity.gson.fromJson(object.getJSONArray("batch").toString(), new TypeToken<List<Batch_model>>() {
                                }.getType());
                                testAdapterone = new TestAdapterone(batchs, Profiles.this);
                                profilesBinding.batchid.setAdapter(testAdapterone);
                                if (batchs.size() != 0) {

                                    for (int i = 0; i < batchs.size(); i++) {
                                        for (String id : elements) {
                                            if (batchs.get(i).getId().equalsIgnoreCase(id)) {
                                                Batch_model batch_model = batchs.get(i);
                                                batch_model.setChecked(true);
                                                batchs.set(i, batch_model);
                                            }
                                        }
                                    }
                                    profilesBinding.llbatchs.setVisibility(View.VISIBLE);
                                    profilesBinding.batchs.setLayoutManager(new LinearLayoutManager(Profiles.this, RecyclerView.HORIZONTAL, false));
                                    batchadpater = new Batchadapter2(Profiles.this, batchs, Profiles.this);
                                    profilesBinding.batchs.setAdapter(batchadpater);

                                } else {
                                    profilesBinding.llbatchs.setVisibility(View.GONE);
                                }


                            } else {
                                Utlity.show_toast(Profiles.this, "failed");
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

    public void add_batch(int position, Batch_model coaching, boolean isChecked) {
        coaching.setChecked(isChecked);
        batchs.set(position, coaching);
        batchadpater.notifyDataSetChanged();
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, Void> {

        @Override
        protected Void doInBackground(String... params) {
            for (Batch_model id : batchs) {
                if (id.isChecked()) {
                    if (TextUtils.isEmpty(exam_id))
                        exam_id = exam_id + id.getId();
                    else
                        exam_id = exam_id + "," + id.getId();
                }


            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (Utlity.is_online(Profiles.this)) {
                update();
            }
        }


    }


}
