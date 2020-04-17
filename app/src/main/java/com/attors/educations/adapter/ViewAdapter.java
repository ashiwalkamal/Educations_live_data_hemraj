package com.attors.educations.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.attors.educations.Helper.Utlity;
import com.attors.educations.Modal.TestpaperModal;
import com.attors.educations.R;
import com.attors.educations.activity.Testdetail;

import java.util.List;

public class ViewAdapter extends PagerAdapter {

    private Context mcontext;
    private LayoutInflater layoutInflater;
    List<TestpaperModal> testpaperModals;
    Testdetail testdetail;
    int attemt,right,wrong,skip;
    public ViewAdapter(Context mcontext, List<TestpaperModal> testpaperModals,Testdetail testdetail,int attemt,int right,int wrong,int skip) {
        this.mcontext = mcontext;
        this.testpaperModals = testpaperModals;
        this.testdetail=testdetail;
        this.attemt=attemt;
        this.right=right;
        this.wrong=wrong;
        this.skip=skip;
    }

    @Override
    public int getCount() {
        return testpaperModals.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater)mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.testdesign, null);

        final TestpaperModal testpaperModal=testpaperModals.get(position);
        TextView Question=(TextView)view.findViewById(R.id.qtiontest);
        final RadioGroup options=(RadioGroup)view.findViewById(R.id.answerlist);
        final RadioButton option1=(RadioButton)view.findViewById(R.id.optone);
        final RadioButton option2=(RadioButton)view.findViewById(R.id.opttwo);
        final RadioButton option3=(RadioButton)view.findViewById(R.id.optthree);
        final RadioButton option4=(RadioButton)view.findViewById(R.id.optfour);

        Question.setText(testpaperModals.get(position).getQuestion());
        option1.setText(testpaperModals.get(position).getOption1());
        option2.setText(testpaperModals.get(position).getOption2());
        option3.setText(testpaperModals.get(position).getOption3());
        option4.setText(testpaperModals.get(position).getOption4());



        if(testpaperModals.get(position).isIs_disable())
        {
            if(testpaperModals.get(position).getChecked_id()==0){
                option1.setChecked(true);
            }
            else  if(testpaperModals.get(position).getChecked_id()==1){
                option2.setChecked(true);
            }
            else if(testpaperModals.get(position).getChecked_id()==2){
                option3.setChecked(true);
            }
            else if(testpaperModals.get(position).getChecked_id()==3){
                option4.setChecked(true);
            }

            option1.setEnabled(false);
            option2.setEnabled(false);
            option3.setEnabled(false);
            option4.setEnabled(false);

        }
        else
        {
            option1.setEnabled(true);
            option2.setEnabled(true);
            option3.setEnabled(true);
            option4.setEnabled(true);
        }

        options.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                    String right_answer = testpaperModals.get(position).getRight_choice();
                    int radioButtonID = options.getCheckedRadioButtonId();
                    View radioButton = options.findViewById(radioButtonID);
                    int idx = options.indexOfChild(radioButton);
                    String id = "";
                    switch (idx) {
                        case 0:
                            id = "option1";
                            break;
                        case 1:
                            id = "option2";
                            break;
                        case 2:
                            id = "option3";
                            break;
                        case 3:
                            id = "option4";
                            break;
                    }
                    if (right_answer.equalsIgnoreCase(id)) {
                        right++;
                    } else {
                        wrong++;
                    }
                    attemt++;
                    skip=testpaperModals.size()-attemt;
                    testdetail.disable_now(position, idx, testpaperModals.get(position),right,wrong,attemt,skip);
            }
        });

        ViewPager vp =(ViewPager)container;
        vp.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager vp = (ViewPager) container;
        View view =(View)object;
        vp.removeView(view);
    }
}
