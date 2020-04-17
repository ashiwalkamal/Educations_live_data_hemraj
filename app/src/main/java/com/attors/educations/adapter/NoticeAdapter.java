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

import com.attors.educations.Helper.Utlity;
import com.attors.educations.Modal.NewsModal;
import com.attors.educations.Modal.Notice;
import com.attors.educations.R;
import com.attors.educations.activity.Newsdetail;
import com.attors.educations.activity.Webdetail;
import com.attors.educations.databinding.NewsitemBinding;
import com.attors.educations.databinding.NoticeitemBinding;

import java.util.List;


public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.Myview> {
    LayoutInflater layoutInflater;
    Context context;
    List<Notice> newsModals;

    public NoticeAdapter(Context context, List<Notice> newsModals) {
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
        final Notice newsModal=newsModals.get(position);


       holder.newsitemBinding.title.setText(Html.fromHtml(newsModals.get(position).getTitle()));
       holder.newsitemBinding.date.setText(newsModals.get(position).getData());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, Webdetail.class).putExtra("detail", newsModal.getTitle()).putExtra("title","Notice"));
                }
            });



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
