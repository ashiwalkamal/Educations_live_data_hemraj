package com.attors.educations.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.attors.educations.Helper.Utlity;
import com.attors.educations.Modal.NewsModal;
import com.attors.educations.Network.Apis;
import com.attors.educations.R;
import com.attors.educations.activity.Newsdetail;
import com.attors.educations.databinding.NewsitemBinding;

import java.util.List;


public class NotifiAdapter extends RecyclerView.Adapter<NotifiAdapter.Myview> {
    LayoutInflater layoutInflater;
    Context context;
    List<NewsModal> newsModals;

    public NotifiAdapter(Context context, List<NewsModal> newsModals) {
        this.context = context;
        this.newsModals = newsModals;
    }

    @NonNull
    @Override
    public Myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater ==null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        NewsitemBinding newsitemBinding= DataBindingUtil.inflate(layoutInflater, R.layout.newsitem,parent,false);
        return new Myview(newsitemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull Myview holder, int position) {
        final NewsModal newsModal=newsModals.get(position);
        Utlity.Set_image(Apis.uplad+newsModals.get(position).getImage(),holder.newsitemBinding.img);
//        holder.newsitemBinding.title.setText(newsModals.get(position).getTitle());
//        holder.newsitemBinding.descprition.setText(newsModals.get(position).getDesc());


    }

    @Override
    public int getItemCount() {
        return newsModals.size();
    }

    public class Myview extends RecyclerView.ViewHolder {
       private final NewsitemBinding newsitemBinding;
        public Myview(NewsitemBinding newsitemBinding) {
            super(newsitemBinding.getRoot());
            this.newsitemBinding = newsitemBinding;
        }
    }
}
