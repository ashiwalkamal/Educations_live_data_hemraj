package com.attors.educations.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.attors.educations.Helper.Utlity;
import com.attors.educations.Modal.TestModal;
import com.attors.educations.Modal.Test_Save;
import com.attors.educations.Modal.TestpaperModal;
import com.attors.educations.Network.Apis;
import com.attors.educations.R;
import com.attors.educations.adapter.TestpaperAdapter;
import com.attors.educations.adapter.ViewAdapter;
import com.attors.educations.databinding.ActivityTestdetailBinding;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Testdetail extends AppCompatActivity {

    ActivityTestdetailBinding testdetailBinding;

    // private TestpaperAdapter testpaperAdapter;
    AlertDialog.Builder builder;
    ArrayList<Test_Save> old_save;
    private List<TestpaperModal> testpaperModals = new ArrayList<>();
    private ViewAdapter viewAdapter;
    private TestModal detail;
    private int pos = 0;
    private int current_second = 0;
    private int right,wrong,skip,attemp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        testdetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_testdetail);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        old_save = Utlity.get_all_test(this);
        if (old_save == null) {
            old_save = new ArrayList<>();
        }

        if (getIntent() != null) {
            detail = Utlity.gson.fromJson(getIntent().getStringExtra("detail"), TestModal.class);
            testdetailBinding.testnme.setText(detail.getTest_name());
            testdetailBinding.testdte.setText(detail.getDate());

            if (old_save != null && old_save.size() != 0) {
                int index = get_index();
                if (index != -1) {
                    Test_Save save = old_save.get(index);
                    testpaperModals=save.getQuestion();
                    attemp=save.getAttep();
                    right=save.getRight();
                    wrong=save.getWrong();
                    skip=save.getSkip();
                    viewAdapter = new ViewAdapter(Testdetail.this, save.getQuestion(), Testdetail.this, save.getAttep(), save.getRight(), save.getWrong(), save.getSkip());
                    testdetailBinding.viewdetail.setAdapter(viewAdapter);
                    testdetailBinding.dotsIndicator.setViewPager(testdetailBinding.viewdetail);
                    testdetailBinding.viewdetail.setCurrentItem(save.getPos());
                    testdetailBinding.qtskip.setText("" + save.getSkip());
                    testdetailBinding.qright.setText("" + save.getRight());
                    testdetailBinding.qtwrong.setText("" + save.getWrong());
                    testdetailBinding.qtno.setText("" + save.getAttep());
                    if(save.getPos()==save.getQuestion().size())
                    {
                        testdetailBinding.llresult.setVisibility(View.VISIBLE);
                    }
                    start_timer(save.getSecond());
                } else {
                    if (Utlity.is_online(this)) {
                        TestData();
                    } else {
                        Utlity.show_toast(this, getResources().getString(R.string.nointernet));
                    }

                }
            }
            else
            {
                if (Utlity.is_online(this)) {
                    TestData();
                } else {
                    Utlity.show_toast(this, getResources().getString(R.string.nointernet));
                }
            }

        }
        testdetailBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancle_wwarning();
            }
        });
        testdetailBinding.viewdetail.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                pos = position;
                if (pos == testpaperModals.size() - 1) {
                    testdetailBinding.llresult.setVisibility(View.VISIBLE);
                } else {
                    testdetailBinding.llresult.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        testdetailBinding.privews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (testpaperModals.size() != 0) {
                    pos--;
                    testdetailBinding.viewdetail.setCurrentItem(pos);
                    if (pos == testpaperModals.size() - 1) {
                        testdetailBinding.llresult.setVisibility(View.VISIBLE);
                    } else {
                        testdetailBinding.llresult.setVisibility(View.GONE);
                    }
                }
            }
        });
        testdetailBinding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (testpaperModals.size() != pos) {
                    pos++;
                    testdetailBinding.viewdetail.setCurrentItem(pos);
                    if (pos == testpaperModals.size() - 1) {
                        testdetailBinding.llresult.setVisibility(View.VISIBLE);
                    } else {
                        testdetailBinding.llresult.setVisibility(View.GONE);
                    }
                }

            }
        });

        testdetailBinding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utlity.is_online(Testdetail.this))
                {
                    test_complete();
                }
                else
                {
                    Utlity.show_toast(Testdetail.this,getString(R.string.nointernet));
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        cancle_wwarning();
    }

    public void TestData() {

        HashMap<String, String> keys = new HashMap<>();
        keys.put("id", detail.getId());
        Request result = Utlity.post(this, keys, Apis.testpaper);
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.retryOnConnectionFailure();
        okHttpClient.connectTimeoutMillis();
        okHttpClient.newCall(result).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Utlity.dismiss_dilog(Testdetail.this);
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, final @NonNull Response response) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        JSONObject object = null;
                        try {
                            Utlity.dismiss_dilog(Testdetail.this);
                            object = new JSONObject(response.body().string());
                            if (object.getString("success").equalsIgnoreCase("1")) {
                                testpaperModals = Utlity.gson.fromJson(object.getJSONObject("text").getJSONArray("question_info").toString(), new TypeToken<List<TestpaperModal>>() {
                                }.getType());
                                viewAdapter = new ViewAdapter(Testdetail.this, testpaperModals, Testdetail.this, 0, 0, 0, testpaperModals.size());
                                testdetailBinding.viewdetail.setAdapter(viewAdapter);
                                testdetailBinding.dotsIndicator.setViewPager(testdetailBinding.viewdetail);
                                testdetailBinding.qtskip.setText("" + testpaperModals.size());
                                int second = 0, min = 0;
                                try {
                                    second = Integer.parseInt(detail.getHour()) * 60 * 60;
                                    min = Integer.parseInt(detail.getMin()) * 60;
                                    second = second + min;
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                                start_timer(second);

                            } else {
                                Utlity.show_toast(Testdetail.this, object.getString("text"));
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

    private void start_timer(int second) {
        new CountDownTimer(second * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);

                int hours = seconds / (60 * 60);
                int tempMint = (seconds - (hours * 60 * 60));
                int minutes = tempMint / 60;
                seconds = tempMint - (minutes * 60);

                testdetailBinding.timer.setText("TIME : " + String.format("%02d", hours)
                        + ":" + String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds));

                int new_hour = hours * 60 * 60;
                int new_second = minutes * 60;

                current_second = new_second + new_hour;
            }

            public void onFinish() {
                testdetailBinding.timer.setText("Time Out !");
            }
        }.start();
    }

    private void cancle_wwarning() {
        builder = new AlertDialog.Builder(this);
        //Setting message manually and performing action on button click
        builder.setMessage("Do you want to save test or cancel ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int index = get_index();
                        if (index >= 0) {
                            old_save.set(index, new Test_Save(detail.getId(), current_second, pos,attemp,wrong,right,skip,testpaperModals));
                        } else {
                            old_save.add( new Test_Save(detail.getId(), current_second, pos,attemp,wrong,right,skip,testpaperModals));
                        }
                        Utlity.user_test(Testdetail.this, Utlity.gson.toJson(old_save));
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.show();
    }

    public void disable_now(int position, int selected, TestpaperModal testpaperModal, int righte, int wronge, int attemte, int skipe) {
        testpaperModal.setChecked_id(selected);
        testpaperModal.setIs_disable(true);
        testpaperModals.set(position, testpaperModal);
        viewAdapter = new ViewAdapter(Testdetail.this, testpaperModals, Testdetail.this, attemte, righte, wronge, skipe);
        testdetailBinding.viewdetail.setAdapter(viewAdapter);
        testdetailBinding.dotsIndicator.setViewPager(testdetailBinding.viewdetail);
        testdetailBinding.viewdetail.setCurrentItem(position);
        right=righte;
        wrong=wronge;
        attemp=attemte;
        skip=skipe;
        testdetailBinding.qright.setText("" + right);
        testdetailBinding.qtwrong.setText("" + wrong);
        testdetailBinding.qtno.setText("" + attemte);
        testdetailBinding.qtskip.setText("" + skip);

    }

    private int get_index() {
        int pos = -1;
        for (int i = 0; i < old_save.size(); i++) {
            if (old_save.get(i).getId().equalsIgnoreCase(detail.getId())) {
                pos = i;
            }
        }
        return pos;
    }

    private void test_complete() {
        HashMap<String, String> keys = new HashMap<>();
        keys.put("student_id", Utlity.get_user(this).getId());
        keys.put("coaching_id", Utlity.get_user(this).getCoaching_id());
        keys.put("attempt", String.valueOf(attemp));
        keys.put("wrong", String.valueOf(wrong));
        keys.put("right_question", String.valueOf(right));
        keys.put("skip", String.valueOf(skip));
        keys.put("test_id", detail.getId());
        keys.put("test_name", detail.getTest_name());
        keys.put("status", "2");
        Request result= Utlity.post(this, keys, Apis.test_submit);
        OkHttpClient okHttpClient= new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Utlity.dismiss_dilog(Testdetail.this);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Utlity.dismiss_dilog(Testdetail.this);
                            String apidata = response.body().string();
                            Log.d("responce>>>>>",apidata);
                            JSONObject object = new JSONObject(apidata);
                            if (object.getString("success").equalsIgnoreCase("1")){

                                int index = get_index();
                                if (index >= 0) {
                                    old_save.remove(index);
                                    Utlity.user_test(Testdetail.this,Utlity.gson.toJson(old_save));
                                }
                                Utlity.show_toast(Testdetail.this,object.getString("text"));
                                finish();
                            }
                            else {
                                Utlity.show_toast(Testdetail.this,object.getString("text"));
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
