package com.attors.educations.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.attors.educations.R;

import java.util.List;

/**
 * Created by Kamal on 28-Nov-16.
 */

public class TimeAdapter extends BaseAdapter {
    private List<String> alertList;
    private LayoutInflater mInflater;

    public TimeAdapter(Context context, Object results) {
        alertList = (List<String>) results;
        //just check if it works.
        //in real, actual object fits in
        alertList.add("09-10");
        alertList.add("10-11");
        alertList.add("11-12");
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return alertList.size();
    }

    @Override
    public Object getItem(int position) {
        return alertList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_time_sppiner_row, null);
            holder = new ViewHolder();
            holder.spinnerValue = (TextView) convertView.findViewById(R.id.spinnervalue);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        if(position==2)
        {
//            holder.spinnerValue.setText(alertList.get(position));
            holder.spinnerValue.setHint(alertList.get(position));

        }
        else
        {
            holder.spinnerValue.setText(alertList.get(position));
        }
        return convertView;
    }
    static class ViewHolder {
        TextView spinnerValue; //spinner name
    }
}

