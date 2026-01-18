package github.stecjr.srms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import github.stecjr.srms.R;
import github.stecjr.srms.model.Option;

public class OptionSelectorAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<Option> options;

    public OptionSelectorAdapter(Context context, ArrayList<Option> options) {
        this.context = context;
        this.options = options;
    }

    @Override
    public int getCount() {
        return options.size();
    }

    @Override
    public Object getItem(int position) {
        return options.get(position);
    }

    @Override
    public long getItemId(int position) {
        return options.get(position).optionId;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.adapter_basic_selector, parent, false);
        }
        Option option = options.get(position);
        TextView basicAdapterTextView = view.findViewById(R.id.basicAdapterTextView);
        basicAdapterTextView.setText(option.optionDesc);
        return view;
    }
}