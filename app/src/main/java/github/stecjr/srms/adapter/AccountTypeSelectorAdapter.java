package github.stecjr.srms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import github.stecjr.srms.R;
import github.stecjr.srms.model.AccountType;

public class AccountTypeSelectorAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<AccountType> accountTypes;

    public AccountTypeSelectorAdapter(Context context, ArrayList<AccountType> accountTypes) {
        this.context = context;
        this.accountTypes = accountTypes;
    }

    @Override
    public int getCount() {
        return accountTypes.size();
    }

    @Override
    public Object getItem(int position) {
        return accountTypes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return accountTypes.get(position).accountId;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.adapter_basic_selector, parent, false);
        }
        AccountType accountType = accountTypes.get(position);
        TextView basicAdapterTextView = view.findViewById(R.id.basicAdapterTextView);
        basicAdapterTextView.setText(accountType.accountType);
        return view;
    }
}