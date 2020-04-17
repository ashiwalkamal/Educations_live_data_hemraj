package com.attors.educations.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.attors.educations.Modal.History;
import com.attors.educations.Modal.Notice;
import com.attors.educations.R;
import com.attors.educations.activity.Webdetail;
import com.attors.educations.databinding.NoticeitemBinding;

import java.util.List;


public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.Myview> {
    LayoutInflater layoutInflater;
    Context context;
    List<History> newsModals;

    public HistoryAdapter(Context context, List<History> newsModals) {
        this.context = context;
        this.newsModals = newsModals;
    }

    @NonNull
    @Override
    public Myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater ==null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        NoticeitemBinding newsitemBinding= DataBindingUtil.inflate(layoutInflater, R.layout.noticeitem,parent,false);
        return new Myview(newsitemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull Myview holder, int position) {
        final History newsModal=newsModals.get(position);

       holder.newsitemBinding.title.setText(Html.fromHtml(newsModals.get(position).getTest_name()));
       holder.newsitemBinding.date.setText(newsModals.get(position).getDate_time());

    }

    @Override
    public int getItemCount() {
        return newsModals.size();
    }

    public class Myview extends RecyclerView.ViewHolder {
       private final NoticeitemBinding newsitemBinding;
        public Myview(NoticeitemBinding newsitemBinding) {
            super(newsitemBinding.getRoot());
            this.newsitemBinding = newsitemBinding;
        }
    }
}
