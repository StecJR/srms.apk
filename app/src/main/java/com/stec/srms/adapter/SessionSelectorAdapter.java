package com.stec.srms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.stec.srms.R;
import com.stec.srms.model.SessionInfo;

import java.util.ArrayList;

public class SessionSelectorAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<SessionInfo> sessions;

    public SessionSelectorAdapter(Context context, ArrayList<SessionInfo> sessions) {
        this.context = context;
        this.sessions = sessions;
    }

    @Override
    public int getCount() {
        return sessions.size();
    }

    @Override
    public Object getItem(int position) {
        return sessions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return sessions.get(position).sessionId;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.adapter_basic_selector, parent, false);
        }
        SessionInfo sessionInfo = sessions.get(position);
        TextView basicAdapterTextView = view.findViewById(R.id.basicAdapterTextView);
        basicAdapterTextView.setText(sessionInfo.desc);
        return view;
    }
}

