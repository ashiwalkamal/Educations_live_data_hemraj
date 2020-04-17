package com.attors.educations.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.attors.educations.Helper.Utlity;
import com.attors.educations.Modal.Batch_model;
import com.attors.educations.Modal.Coaching;
import com.attors.educations.Network.Apis;
import com.attors.educations.R;
import com.attors.educations.activity.Profiles;
import com.attors.educations.databinding.BatchitemBinding;

import java.util.List;


public class Batchadapter extends RecyclerView.Adapter<Batchadapter.Myview> {
    LayoutInflater layoutInflater;
    Context context;
    List<Batch_model> coachings;
    List<String> elements;
    boolean is_enrolled=false;
    public Batchadapter(Context context, List<Batch_model> coachings,List<String> element) {
        this.context = context;
        this.coachings = coachings;
        this.elements=element;
    }

    @NonNull
    @Override
    public Myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater ==null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        BatchitemBinding batchitemBinding= DataBindingUtil.inflate(layoutInflater, R.layout.batchitem,parent,false);
        return new Myview(batchitemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull Myview holder, int position) {
        final Batch_model coaching = coachings.get(position);
        Utlity.Set_image(coachings.get(position).getLogo(),holder.batchitemBinding.bimg);
        holder.batchitemBinding.currenttitle.setText(coachings.get(position).getName());
        for(String id:elements)
        {
            if(coaching.getId().equalsIgnoreCase(id))
            {
                holder.batchitemBinding.btnenroll.setText("Enrolled");
                holder.batchitemBinding.btnenroll.setBackground(context.getResources().getDrawable(R.drawable.editback2));

            }


        }

        holder.batchitemBinding.btnenroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    context.startActivity(new Intent(context, Profiles.class));


            }
        });

    }

    @Override
    public int getItemCount() {
        return coachings.size();
    }

    public class Myview extends RecyclerView.ViewHolder {
       private final BatchitemBinding batchitemBinding;
        public Myview(BatchitemBinding batchitemBinding) {
            super(batchitemBinding.getRoot());
            this.batchitemBinding = batchitemBinding;
        }
    }
}
