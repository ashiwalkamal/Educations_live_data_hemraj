package com.attors.educations.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.attors.educations.Helper.Utlity;
import com.attors.educations.Modal.SettingModal;
import com.attors.educations.Modal.TestModal;
import com.attors.educations.R;
import com.attors.educations.activity.HistoryDetail;
import com.attors.educations.activity.Profiles;
import com.attors.educations.databinding.SettingitemBinding;
import com.attors.educations.databinding.TestitemBinding;

import java.util.List;


public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.Myview> {
    LayoutInflater layoutInflater;
    Context context;
    List<SettingModal> settingModals;

    public SettingAdapter(Context context, List<SettingModal> settingModals) {
        this.context = context;
        this.settingModals = settingModals;
    }

    @NonNull
    @Override
    public Myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        SettingitemBinding settingitemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.settingitem, parent, false);
        return new Myview(settingitemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull Myview holder, final int position) {
        final SettingModal tabModal1 = settingModals.get(position);
        holder.settingitemBinding.img.setImageResource(settingModals.get(position).getImg1());
        //holder.settingitemBinding.allpress.setImageResource(settingModals.get(position).getImg2());
        holder.settingitemBinding.title.setText(settingModals.get(position).getTxt1());


        holder.settingitemBinding.allpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 0) {
                    context.startActivity(new Intent(context, Profiles.class));
                }
                else if (position == 1) {
                    context.startActivity(new Intent(context, HistoryDetail.class));
                }
                else if (position == 2) {
                    shareApp();
                } else if (position == 3) {
                    sendFeedback();
                } else if (position == 4) {
                    rateApp();
                } else if (position == 5) {
                    Utlity.Logout(context);
                }
            }
        });


    }

    private void shareApp() {
        final String appPackageName = context.getPackageName();
        String myUrl = "https://play.google.com/store/apps/details?id=" + appPackageName;

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, myUrl);
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, "Share using..."));
    }

    private void rateApp() {
        final String appPackageName = context.getPackageName();
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    private void sendFeedback() {
        final Intent _Intent = new Intent(android.content.Intent.ACTION_SEND);
        _Intent.setType("text/html");
        _Intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{context.getString(R.string.mail_feedback_email)});
        _Intent.putExtra(android.content.Intent.EXTRA_SUBJECT, context.getString(R.string.mail_feedback_subject));
        _Intent.putExtra(android.content.Intent.EXTRA_TEXT, context.getString(R.string.mail_feedback_message));
        context.startActivity(Intent.createChooser(_Intent, context.getString(R.string.title_send_feedback)));
    }

    @Override
    public int getItemCount() {
        return settingModals.size();
    }

    public class Myview extends RecyclerView.ViewHolder {
        private final SettingitemBinding settingitemBinding;

        public Myview(SettingitemBinding settingitemBinding) {
            super(settingitemBinding.getRoot());
            this.settingitemBinding = settingitemBinding;
        }
    }
}
