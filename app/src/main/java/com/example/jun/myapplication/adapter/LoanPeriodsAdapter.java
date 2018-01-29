package com.example.jun.myapplication.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.example.jun.myapplication.R;

import java.util.List;

/**
 * Created by JUN on 2018/1/25.
 */

public class LoanPeriodsAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mPeriodsList;
    private SparseBooleanArray mSelectMap;

    public LoanPeriodsAdapter(Context context, List<String> list) {
        mContext = context;
        mPeriodsList = list;
        mSelectMap = new SparseBooleanArray(mPeriodsList.size());
        for (int i = 0; i < mPeriodsList.size(); i++) {
            mSelectMap.put(i, false);
        }
    }

    @Override
    public int getCount() {
        return mPeriodsList != null ? mPeriodsList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mPeriodsList != null ? mPeriodsList.get(position) : "";
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_loan_periods, null);
            holder = new ViewHolder();
            holder.periodRL = (RelativeLayout) convertView.findViewById(R.id.rl_period);
            holder.loanPeriodRB = (RadioButton) convertView.findViewById(R.id.tv_loan_period);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final String period = mPeriodsList.get(position);
        holder.loanPeriodRB.setText(period);
        holder.loanPeriodRB.setChecked(mSelectMap.get(position));

        return convertView;
    }

    public void setSelectPosition(int position) {
        for (int i = 0; i < mSelectMap.size(); i++) {
            mSelectMap.put(i, position == i);
        }
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        RelativeLayout periodRL;
        RadioButton loanPeriodRB;
    }


}