package com.example.jun.myapplication.widget.expandlistview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.jun.myapplication.R;
import com.example.jun.myapplication.bean.SampleChildBean;
import com.example.jun.myapplication.bean.SampleGroupBean;

import java.util.List;

/**
 * Created by JUN on 2017/12/28.
 */

public class ExpandListViewAdapter extends BaseExpandableListAdapter {

    private List<SampleGroupBean> mList;
    private OnGroupExpandedListener mOnGroupExpandedListener;

    public ExpandListViewAdapter(List<SampleGroupBean> list) {
        mList = list;
    }

    public void setOnGroupExpandedListener(OnGroupExpandedListener onGroupExpandedListener) {
        mOnGroupExpandedListener = onGroupExpandedListener;
    }

    @Override
    public int getGroupCount() {
        return mList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return mList.get(i).getChildCount();
    }

    @Override
    public SampleGroupBean getGroup(int i) {
        return mList.get(i);
    }

    @Override
    public SampleChildBean getChild(int i, int i1) {
        return mList.get(i).getChildAt(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        GroupViewHolder groupViewHolder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listitem_group, viewGroup, false);
            groupViewHolder = new GroupViewHolder(view);
            view.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) view.getTag();
        }
        groupViewHolder.ivGroupFold.setImageResource(b ? R.drawable.ic_arrow_expanding : R.drawable.ic_arrow_folding);
        groupViewHolder.tvGroupId.setText(mList.get(i).getId());
        groupViewHolder.tvGroupDate.setText(mList.get(i).getDate());
        groupViewHolder.tvGroupMoney.setText(mList.get(i).getMoney());
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ChildViewHolder childViewHolder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listitem_child, viewGroup, false);
            childViewHolder = new ChildViewHolder(view);
            view.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) view.getTag();
        }
        childViewHolder.tvItemDetail.setText(mList.get(i).getChildAt(i1).getDetail());
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    @Override
    public void onGroupExpanded(int i) {
        if (mOnGroupExpandedListener != null) {
            mOnGroupExpandedListener.onGroupExpanded(i);
        }
    }

    @Override
    public void onGroupCollapsed(int i) {

    }

    private class GroupViewHolder {
        ImageView ivGroupFold;
        TextView tvGroupId;
        TextView tvGroupDate;
        TextView tvGroupMoney;

        GroupViewHolder(View itemView) {
            ivGroupFold = (ImageView) itemView.findViewById(R.id.group_item_indicator);
            tvGroupId = (TextView) itemView.findViewById(R.id.tv_id);
            tvGroupDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvGroupMoney = (TextView) itemView.findViewById(R.id.tv_money);
        }
    }

    private class ChildViewHolder {
        TextView tvItemDetail;

        ChildViewHolder(View itemView) {
            tvItemDetail = (TextView) itemView.findViewById(R.id.tv_detail);
        }
    }
}
