package github.stecjr.srms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import github.stecjr.srms.R;
import github.stecjr.srms.model.SemesterInfo;

public class SemesterSelectorAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<SemesterInfo> semesters;

    public SemesterSelectorAdapter(Context context, ArrayList<SemesterInfo> semesters) {
        this.context = context;
        this.semesters = semesters;
    }

    @Override
    public int getCount() {
        return semesters.size();
    }

    @Override
    public Object getItem(int position) {
        return semesters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return semesters.get(position).semesterId;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.adapter_basic_selector, parent, false);
        }
        SemesterInfo semesterInfo = semesters.get(position);
        TextView basicAdapterTextView = view.findViewById(R.id.basicAdapterTextView);
        basicAdapterTextView.setText(semesterInfo.shortDesc);
        return view;
    }
}

