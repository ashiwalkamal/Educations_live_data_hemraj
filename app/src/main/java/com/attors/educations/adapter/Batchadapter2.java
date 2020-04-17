package com.attors.educations.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.attors.educations.Helper.Utlity;
import com.attors.educations.Modal.Batch_model;
import com.attors.educations.R;
import com.attors.educations.activity.Profiles;
import com.attors.educations.databinding.BatcexamBinding;
import com.attors.educations.databinding.BatchitemBinding;

import java.util.List;


public class Batchadapter2 extends RecyclerView.Adapter<Batchadapter2.Myview> {
    LayoutInflater layoutInflater;
    Context context;
    List<Batch_model> coachings;
    Profiles profiles;

    public Batchadapter2(Context context, List<Batch_model> coachings, Profiles profiles) {
        this.context = context;
        this.coachings = coachings;
        this.profiles = profiles;

    }

    @NonNull
    @Override
    public Myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        BatcexamBinding batchitemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.batcexam, parent, false);
        return new Myview(batchitemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull Myview holder, final int position) {
        final Batch_model coaching = coachings.get(position);
        holder.batchitemBinding.checked.setText(coachings.get(position).getName());

        if (coaching.isChecked()) {
            holder.batchitemBinding.checked.setChecked(true);
        }

        holder.batchitemBinding.checked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                profiles.add_batch(position, coaching, isChecked);
            }
        });

    }

    @Override
    public int getItemCount() {
        return coachings.size();
    }

    public class Myview extends RecyclerView.ViewHolder {
        private final BatcexamBinding batchitemBinding;

        public Myview(BatcexamBinding batchitemBinding) {
            super(batchitemBinding.getRoot());
            this.batchitemBinding = batchitemBinding;
        }
    }
}
