package com.attors.educations.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.attors.educations.Modal.PostModal;
import com.attors.educations.Modal.TestModal;
import com.attors.educations.R;
import com.attors.educations.activity.Testdetail;
import com.attors.educations.databinding.PostitemBinding;
import com.attors.educations.databinding.TestitemBinding;

import java.util.List;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.Myview> {
    LayoutInflater layoutInflater;
    Context context;
    List<PostModal> postModals;

    public PostAdapter(Context context, List<PostModal> postModals) {
        this.context = context;
        this.postModals = postModals;
    }

    @NonNull
    @Override
    public Myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater ==null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        PostitemBinding postitemBinding= DataBindingUtil.inflate(layoutInflater, R.layout.postitem,parent,false);
        return new Myview(postitemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull Myview holder, int position) {
        final PostModal postModal1=postModals.get(position);
        holder.postitemBinding.img.setImageResource(postModals.get(position).getImg1());
        holder.postitemBinding.title.setText(postModals.get(position).getTxt1());
        holder.postitemBinding.descprition.setText(postModals.get(position).getTxt2());

        holder.postitemBinding.viewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, Testdetail.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return postModals.size();
    }

    public class Myview extends RecyclerView.ViewHolder {
       private final PostitemBinding postitemBinding;
        public Myview(PostitemBinding postitemBinding) {
            super(postitemBinding.getRoot());
            this.postitemBinding = postitemBinding;
        }
    }
}
