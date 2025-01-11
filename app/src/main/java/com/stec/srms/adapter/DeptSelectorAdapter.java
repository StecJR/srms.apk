package com.stec.srms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.stec.srms.R;
import com.stec.srms.model.DeptInfo;

import java.util.ArrayList;

public class DeptSelectorAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<DeptInfo> departments;

    public DeptSelectorAdapter(Context context, ArrayList<DeptInfo> departments) {
        this.context = context;
        this.departments = departments;
    }

    @Override
    public int getCount() {
        return departments.size();
    }

    @Override
    public Object getItem(int position) {
        return departments.get(position);
    }

    @Override
    public long getItemId(int position) {
        // return departments.get(position).deptId;
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_dept_selector, parent, false);
        }
        DeptInfo deptInfo = departments.get(position);
        TextView departmentName = convertView.findViewById(R.id.departmentName);
        departmentName.setText(deptInfo.shortDesc);
        return convertView;
    }
}
