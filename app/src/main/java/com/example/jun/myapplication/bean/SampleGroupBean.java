package com.example.jun.myapplication.bean;

import android.support.annotation.NonNull;
import com.example.jun.myapplication.adapter.BaseExpandableRecyclerViewAdapter;

import java.util.List;

public class SampleGroupBean implements BaseExpandableRecyclerViewAdapter.BaseGroupBean<SampleChildBean> {

    private List<SampleChildBean> mList;
    private String mId;
    private String mDate;
    private String mMoney;

    public SampleGroupBean(@NonNull List<SampleChildBean> list, @NonNull String id) {
        mList = list;
        mId = id;
    }

    @Override
    public int getChildCount() {
        return mList.size();
    }

    @Override
    public boolean isExpandable() {
        return getChildCount() > 0;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    public String getMoney() {
        return mMoney;
    }

    public void setMoney(String mMoney) {
        this.mMoney = mMoney;
    }

    public List<SampleChildBean> getList() {
        return mList;
    }

    public void setList(List<SampleChildBean> mList) {
        this.mList = mList;
    }

    @Override
    public SampleChildBean getChildAt(int index) {
        return mList.size() <= index ? null : mList.get(index);
    }
}
