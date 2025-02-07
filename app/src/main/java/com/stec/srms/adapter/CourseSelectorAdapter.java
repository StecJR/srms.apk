package com.stec.srms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.stec.srms.R;
import com.stec.srms.model.CourseInfo;

import java.util.ArrayList;

public class CourseSelectorAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<CourseInfo> courses;

    public CourseSelectorAdapter(Context context, ArrayList<CourseInfo> courses) {
        this.context = context;
        this.courses = courses;
    }

    @Override
    public int getCount() {
        return courses.size();
    }

    @Override
    public Object getItem(int position) {
        return courses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return courses.get(position).courseCode;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.adapter_basic_selector, parent, false);
        }
        CourseInfo courseInfo = courses.get(position);
        TextView basicAdapterTextView = view.findViewById(R.id.basicAdapterTextView);
        basicAdapterTextView.setText(courseInfo.shortDesc);
        return view;
    }
}
