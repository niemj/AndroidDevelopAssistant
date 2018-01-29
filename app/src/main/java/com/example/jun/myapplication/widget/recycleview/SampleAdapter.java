package com.example.jun.myapplication.widget.recycleview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.jun.myapplication.R;
import com.example.jun.myapplication.adapter.BaseExpandableRecyclerViewAdapter;
import com.example.jun.myapplication.bean.SampleChildBean;
import com.example.jun.myapplication.bean.SampleGroupBean;

import java.util.List;


public class SampleAdapter extends
        BaseExpandableRecyclerViewAdapter<SampleGroupBean, SampleChildBean, SampleAdapter.GroupVH, SampleAdapter.ChildVH> {

    private List<SampleGroupBean> mList;

    public SampleAdapter(List<SampleGroupBean> list) {
        mList = list;
    }

    @Override
    public int getGroupCount() {
        return mList.size();
    }

    @Override
    public SampleGroupBean getGroupItem(int position) {
        return mList.get(position);
    }

    @Override
    public GroupVH onCreateGroupViewHolder(ViewGroup parent, int groupViewType) {
        return new GroupVH(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.listitem_group, parent, false));
    }

    @Override
    public ChildVH onCreateChildViewHolder(ViewGroup parent, int childViewType) {
        return new ChildVH(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.listitem_child, parent, false));
    }

    @Override
    public void onBindGroupViewHolder(GroupVH holder, SampleGroupBean sampleGroupBean, boolean isExpanding) {
        holder.tvGroupId.setText(sampleGroupBean.getId());
        holder.tvGroupDate.setText(sampleGroupBean.getDate());
        holder.tvGroupMoney.setText(sampleGroupBean.getMoney());

        if (sampleGroupBean.isExpandable()) {
            holder.ivGroupFold.setVisibility(View.VISIBLE);
            holder.ivGroupFold.setImageResource(isExpanding ? R.drawable.ic_arrow_expanding : R.drawable.ic_arrow_folding);
        } else {
            holder.ivGroupFold.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBindChildViewHolder(ChildVH holder, SampleGroupBean groupBean, SampleChildBean sampleChildBean) {
        holder.tvItemDetail.setText(sampleChildBean.getDetail());
    }


    static class GroupVH extends BaseExpandableRecyclerViewAdapter.BaseGroupViewHolder {
        ImageView ivGroupFold;
        TextView tvGroupId;
        TextView tvGroupDate;
        TextView tvGroupMoney;

        GroupVH(View itemView) {
            super(itemView);
            ivGroupFold = (ImageView) itemView.findViewById(R.id.group_item_indicator);
            tvGroupId = (TextView) itemView.findViewById(R.id.tv_id);
            tvGroupDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvGroupMoney = (TextView) itemView.findViewById(R.id.tv_money);
        }

        @Override
        protected void onExpandStatusChanged(RecyclerView.Adapter relatedAdapter, boolean isExpanding) {
            ivGroupFold.setImageResource(isExpanding ? R.drawable.ic_arrow_expanding : R.drawable.ic_arrow_folding);
        }
    }

    static class ChildVH extends RecyclerView.ViewHolder {
        TextView tvItemDetail;

        ChildVH(View itemView) {
            super(itemView);
            tvItemDetail = (TextView) itemView.findViewById(R.id.tv_detail);
        }
    }
}
