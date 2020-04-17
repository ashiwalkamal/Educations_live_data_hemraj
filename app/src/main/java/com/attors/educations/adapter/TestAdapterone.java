package com.attors.educations.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.attors.educations.Modal.Batch_model;
import com.attors.educations.R;


import java.util.List;

public class TestAdapterone extends BaseAdapter {

    List<Batch_model> strings;
    Context context;
    private LayoutInflater mInflater;

    public TestAdapterone(List<Batch_model> stringList, Context context) {
        strings = stringList;
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return strings.size();
    }

    @Override
    public String getItem(int position) {
        return strings.get(position).getName();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TimeAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layoutsingle, null);
            holder = new TimeAdapter.ViewHolder();
            holder.spinnerValue = (TextView) convertView.findViewById(R.id.spinnervalue);
            convertView.setTag(holder);
        }
        else
        {
            holder = (TimeAdapter.ViewHolder) convertView.getTag();
        }
        TextView textView = convertView.findViewById(R.id.time);

        textView.setText(strings.get(position).getName());
        //here you can use position or string


        return convertView;
    }
}
