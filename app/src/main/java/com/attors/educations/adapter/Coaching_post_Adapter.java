package com.attors.educations.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.attors.educations.Helper.Utlity;
import com.attors.educations.Modal.NewsModal;
import com.attors.educations.Modal.TestModal;
import com.attors.educations.Modal.TestpaperModal;
import com.attors.educations.Network.Apis;
import com.attors.educations.R;
import com.attors.educations.activity.Testdetail;

import java.util.ArrayList;
import java.util.List;

public class Coaching_post_Adapter extends PagerAdapter {

    private Context mcontext;
    private LayoutInflater layoutInflater;
    ArrayList<NewsModal> testpaperModals;
    Testdetail testdetail;
    int attemt,right,wrong,skip;
    public Coaching_post_Adapter(Context mcontext, ArrayList<NewsModal> testpaperModals) {
        this.mcontext = mcontext;
        this.testpaperModals = testpaperModals;
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
        View view=layoutInflater.inflate(R.layout.blog_single, null);
        ImageView img=(ImageView)view.findViewById(R.id.images);
        Utlity.Set_image(testpaperModals.get(position).getImage(),img);
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
