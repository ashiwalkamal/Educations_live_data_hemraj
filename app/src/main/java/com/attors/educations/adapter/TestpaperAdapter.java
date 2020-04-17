package com.attors.educations.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.attors.educations.Helper.Utlity;
import com.attors.educations.Modal.TestpaperModal;
import com.attors.educations.R;
import com.attors.educations.databinding.TestdesignBinding;

import java.util.List;


public class TestpaperAdapter extends RecyclerView.Adapter<TestpaperAdapter.Myview> {
    LayoutInflater layoutInflater;
    Activity context;
    List<TestpaperModal> testpaperModals;

    public TestpaperAdapter(Activity context, List<TestpaperModal> testpaperModals) {
        this.context = context;
        this.testpaperModals = testpaperModals;
    }

    @NonNull
    @Override
    public Myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater ==null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        TestdesignBinding testdesignBinding= DataBindingUtil.inflate(layoutInflater, R.layout.testdesign,parent,false);
        return new Myview(testdesignBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final Myview holder, int position) {
        final TestpaperModal testpaperModal=testpaperModals.get(position);

      /*  if (position==0){
            holder.testdesignBinding.back.setVisibility(View.GONE);
        }
        else {
            holder.testdesignBinding.back.setVisibility(View.VISIBLE);

        }

       */

        holder.testdesignBinding.answerlist.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.optone) {
                    Utlity.show_toast(context,"answer1");
                }
                else if (checkedId == R.id.opttwo) {
                    Utlity.show_toast(context,"answer2");
                }

               else if (checkedId == R.id.optthree) {
                    Utlity.show_toast(context,"answer3");
                }
                if (checkedId == R.id.optfour) {
                    Utlity.show_toast(context, "answer4");
                }

                }
        });



    }

    @Override
    public int getItemCount() {
        return testpaperModals.size();
    }

    public class Myview extends RecyclerView.ViewHolder {
       private final TestdesignBinding testdesignBinding;
        public Myview(TestdesignBinding testdesignBinding) {
            super(testdesignBinding.getRoot());
            this.testdesignBinding = testdesignBinding;
        }
    }
}
