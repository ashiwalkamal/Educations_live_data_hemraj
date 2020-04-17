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
import com.attors.educations.Modal.TestModal;
import com.attors.educations.R;
import com.attors.educations.activity.Testdetail;
import com.attors.educations.databinding.TestitemBinding;

import java.util.List;


public class TestAdapter extends RecyclerView.Adapter<TestAdapter.Myview> {
    LayoutInflater layoutInflater;
    Context context;
    List<TestModal> testModal;

    public TestAdapter(Context context, List<TestModal> testModals) {
        this.context = context;
        this.testModal = testModals;
    }

    @NonNull
    @Override
    public Myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater ==null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        TestitemBinding testitemBinding= DataBindingUtil.inflate(layoutInflater, R.layout.testitem,parent,false);
        return new Myview(testitemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull Myview holder, int position) {
        final TestModal tabModal1=testModal.get(position);
        holder.testitemBinding.title.setText(tabModal1.getTest_name());
        holder.testitemBinding.date.setText(tabModal1.getExpire());
        Utlity.Set_image(tabModal1.getImage(),holder.testitemBinding.testphoto);
        holder.testitemBinding.questinno.setText(tabModal1.getHour()+" hour :"+tabModal1.getMin()+" min");
        holder.testitemBinding.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, Testdetail.class).putExtra("detail", Utlity.gson.toJson(tabModal1)));
            }
        });

    }

    @Override
    public int getItemCount() {
        return testModal.size();
    }

    public class Myview extends RecyclerView.ViewHolder {
       private final TestitemBinding testitemBinding;
        public Myview(TestitemBinding testitemBinding) {
            super(testitemBinding.getRoot());
            this.testitemBinding = testitemBinding;
        }
    }
}
