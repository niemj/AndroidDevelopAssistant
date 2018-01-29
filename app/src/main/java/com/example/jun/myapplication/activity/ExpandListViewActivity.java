package com.example.jun.myapplication.activity;


import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import com.example.jun.myapplication.R;
import com.example.jun.myapplication.bean.SampleChildBean;
import com.example.jun.myapplication.bean.SampleGroupBean;
import com.example.jun.myapplication.widget.expandlistview.ExpandListViewAdapter;
import com.example.jun.myapplication.widget.expandlistview.FixedListView;
import com.example.jun.myapplication.widget.expandlistview.OnGroupExpandedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JUN
 */
public class ExpandListViewActivity extends BaseActivity {
    private List<SampleGroupBean> mRepaymentPlanList;
    private FixedListView mExpandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expand_list_view);
        initData();

        mExpandableListView = (FixedListView) findViewById(R.id.expand_list);
        ExpandListViewAdapter adapter = new ExpandListViewAdapter(mRepaymentPlanList);
        mExpandableListView.setAdapter(adapter);
        // 清除默认的Indicator
        mExpandableListView.setGroupIndicator(null);
        // 清除默认的分割线
        mExpandableListView.setDivider(null);
        // 默认展开第一项
        mExpandableListView.expandGroup(0);
        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return false;
            }
        });

        adapter.setOnGroupExpandedListener(new OnGroupExpandedListener() {
            @Override
            public void onGroupExpanded(int groupPosition) {
                expandOnlyOne(groupPosition);
            }
        });

    }

    /**
     * 每次展开一个分组后，关闭其他的分组
     * @param expandedPosition
     * @return
     */
    private boolean expandOnlyOne(int expandedPosition) {
        boolean result = true;
        int groupLength = mExpandableListView.getExpandableListAdapter().getGroupCount();
        for (int i = 0; i < groupLength; i++) {
            if (i != expandedPosition && mExpandableListView.isGroupExpanded(i)) {
                result &= mExpandableListView.collapseGroup(i);
            }
        }
        return result;
    }

    private void initData() {
        mRepaymentPlanList = new ArrayList<>();
        for (int i = 1; i < 40; i++) {
            final List<SampleChildBean> childList = new ArrayList<>(i);
            childList.add(new SampleChildBean("含本金38.39，利息6.72"));

            SampleGroupBean sampleGroupBean = new SampleGroupBean(childList, String.valueOf(i));
            sampleGroupBean.setDate("2017-10-21");
            sampleGroupBean.setMoney("45.71");

            mRepaymentPlanList.add(sampleGroupBean);
        }
    }
}
