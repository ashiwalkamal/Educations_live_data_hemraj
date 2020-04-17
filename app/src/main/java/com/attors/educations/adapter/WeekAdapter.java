package com.attors.educations.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.attors.educations.Modal.TestModal;
import com.attors.educations.Modal.Weekmodal;
import com.attors.educations.R;
import com.attors.educations.databinding.TestitemBinding;
import com.attors.educations.databinding.WeekitemBinding;

import java.util.List;


public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.Myview> {
    LayoutInflater layoutInflater;
    Context context;
    List<Weekmodal> weekmodals;

    public WeekAdapter(Context context, List<Weekmodal> weekmodals) {
        this.context = context;
        this.weekmodals = weekmodals;
    }

    @NonNull
    @Override
    public Myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater ==null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        WeekitemBinding weekitemBinding= DataBindingUtil.inflate(layoutInflater, R.layout.weekitem,parent,false);
        return new Myview(weekitemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull Myview holder, int position) {
        final Weekmodal weekmodal=weekmodals.get(position);
        holder.weekitemBinding.img.setImageResource(weekmodals.get(position).getImg1());
        holder.weekitemBinding.title.setText(weekmodals.get(position).getTxt1());
        holder.weekitemBinding.descprition.setText(weekmodals.get(position).getTxt2());

    }

    @Override
    public int getItemCount() {
        return weekmodals.size();
    }

    public class Myview extends RecyclerView.ViewHolder {
       private final WeekitemBinding weekitemBinding;
        public Myview(WeekitemBinding weekitemBinding) {
            super(weekitemBinding.getRoot());
            this.weekitemBinding = weekitemBinding;
        }
    }
}
