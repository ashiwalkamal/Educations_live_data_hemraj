package com.attors.educations.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.attors.educations.Modal.SettingModal;
import com.attors.educations.Modal.TestModal;
import com.attors.educations.R;
import com.attors.educations.adapter.SettingAdapter;
import com.attors.educations.adapter.TestAdapter;
import com.attors.educations.databinding.ActivitySettingsBinding;

import java.util.ArrayList;
import java.util.List;

public class Settings extends AppCompatActivity {
    ActivitySettingsBinding settingsBinding;
    private SettingAdapter settingAdapter;
    private List<SettingModal> settingModals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingsBinding= DataBindingUtil.setContentView(this,R.layout.activity_settings);

        settingModals= new ArrayList<>();
        settingModals.add(new SettingModal("My Profile",R.drawable.ic_person_active));
        settingModals.add(new SettingModal("My Test History",R.drawable.ic_history_black_24dp));
        settingModals.add(new SettingModal("Share App",R.drawable.ic_share));
        settingModals.add(new SettingModal("Feedback",R.drawable.ic_help));
        settingModals.add(new SettingModal("Rate App",R.drawable.ic_star));
        settingModals.add(new SettingModal("Logout",R.drawable.ic_logout));

        settingsBinding.setting.setHasFixedSize(true);
        settingsBinding.setting.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
        settingAdapter= new SettingAdapter(this,settingModals);
        settingsBinding.setting.setAdapter(settingAdapter);

    }


    @Override
    public void onBackPressed() {
        MainActivity.tabHost.setCurrentTab(0);
    }
}
