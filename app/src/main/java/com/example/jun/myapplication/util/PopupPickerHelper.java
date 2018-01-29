package com.example.jun.myapplication.util;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.example.jun.myapplication.R;
import com.example.jun.myapplication.adapter.LoanPeriodsAdapter;
import com.example.jun.myapplication.adapter.ViewProducer;
import com.example.jun.myapplication.bean.SampleChildBean;
import com.example.jun.myapplication.bean.SampleGroupBean;
import com.example.jun.myapplication.source.MainGridView;
import com.example.jun.myapplication.widget.commonview.CommonTitleItemView;
import com.example.jun.myapplication.widget.pickerview.ScrollPickerView;
import com.example.jun.myapplication.widget.pickerview.StringScrollPicker;
import com.example.jun.myapplication.widget.recycleview.SampleAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JUN on 2017/12/25.
 */

public class PopupPickerHelper implements PopupWindow.OnDismissListener {

    private Activity mActivity;
    private View mRootView;
    private PopupWindow mPopuWindow;
    /*
     *支持下拉刷新的ViewGroup
     */
    private PtrClassicFrameLayout mPtrFrame;
    private String mSelectedLoanUse;


    public PopupPickerHelper(Activity mActivity, View mRootView) {
        this.mActivity = mActivity;
        this.mRootView = mRootView;

    }

    public View initPopuWindow(int resId, int width, int height, int styleid) {
        View contentView = LayoutInflater.from(mActivity).inflate(resId, null);
        if (mPopuWindow == null || !mPopuWindow.getContentView().equals(contentView)) {
            mPopuWindow = new PopupWindow(contentView, width, height, true);
            mPopuWindow.setOutsideTouchable(true);
            mPopuWindow.setBackgroundDrawable(new BitmapDrawable());
        }
        if (contentView.findViewById(R.id.pop_cancel) != null) {
            contentView.findViewById(R.id.pop_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissPopuWindow();
                }
            });
        }
        WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
        params.alpha = 0.7f;

        mActivity.getWindow().setAttributes(params);
        mPopuWindow.setOnDismissListener(this);
        mPopuWindow.setAnimationStyle(styleid);

        return contentView;
    }

    private void dismissPopuWindow() {
        if (mPopuWindow != null && mPopuWindow.isShowing()) {
            mPopuWindow.dismiss();
        }
    }

    @Override
    public void onDismiss() {
        WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
        params.alpha = 1f;
        mActivity.getWindow().setAttributes(params);

    }

    public void showBackPopuWindow() {
        final View view = initPopuWindow(R.layout.popupwindow_back, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, R.style.popwin_anim_style);
        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissPopuWindow();
            }
        });

        view.findViewById(R.id.sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissPopuWindow();
                mActivity.finish();
            }
        });
        mPopuWindow.showAtLocation(mRootView, Gravity.CENTER, 0, 0);

    }

    public void showLoanUsePopuWindow(final List<String> list) {
        View content = initPopuWindow(R.layout.popuwindow_loan_use, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,  R.style.popwin_anim_style);
        final StringScrollPicker picker = (StringScrollPicker) content.findViewById(R.id.picker_load_use);
        final CommonTitleItemView ctivLoan = mActivity.findViewById(R.id.ctiv_loan_use);
        picker.setData(list);
        picker.setSelectedPosition(0);
        picker.setColor(Color.parseColor("#333333"), Color.parseColor("#999999"));
        picker.setIsCirculation(false);
        if (list != null && list.size() > 0) {
            mSelectedLoanUse = list.get(0);
        }
        picker.setOnSelectedListener(new ScrollPickerView.OnSelectedListener() {
            @Override
            public void onSelected(ScrollPickerView scrollPickerView, int position) {
                if (list != null && list.size() > 0) {
                    mSelectedLoanUse = list.get(position);
                }
            }
        });
        content.findViewById(R.id.tv_ensure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctivLoan.setValue(mSelectedLoanUse);
                dismissPopuWindow();
            }
        });

        mPopuWindow.showAtLocation(mRootView, Gravity.BOTTOM, 0, 0);

    }

    public void showRepaymenPlanPopWindow(final List<SampleGroupBean> list) {

        View popupWindowView = initPopuWindow(R.layout.popwindow_recyclerview_repayment, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,  R.style.popwin_anim_style);
        final RecyclerView mRecyclerView = popupWindowView.findViewById(R.id.recyclerview);
        final SampleAdapter adapter = new SampleAdapter(list);
        final RecyclerAdapterWithHF mAdapter = new RecyclerAdapterWithHF(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        //设置只展开一项
        adapter.setOnlyOneExpand(true);
        //设置列表第一项默认展开
        if (list != null && list.size() > 0) {
            adapter.expandGroup(list.get(0));
        }
        adapter.setEmptyViewProducer(new ViewProducer() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
                return new DefaultEmptyViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.empty, parent, false)
                );
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder) {

            }
        });
        mRecyclerView.setAdapter(mAdapter);

        mPtrFrame = (PtrClassicFrameLayout) popupWindowView.findViewById(R.id.rotate_header_list_view_frame);
        //下拉刷新支持时间
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        //下拉刷新一些设置 详情参考文档
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(1000);
        // default is false
        mPtrFrame.setPullToRefresh(false);
        mPtrFrame.setLoadMoreEnable(true);
        // default is true
        mPtrFrame.setKeepHeaderWhenRefresh(true);
        //进入Activity就进行自动下拉刷新
      /*  mPtrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (list != null && list.size() > 0) {
                    adapter.expandGroup(list.get(0));
                }
                mPtrFrame.autoRefresh();
                mAdapter.notifyDataSetChanged();
                mPtrFrame.refreshComplete();
                mPtrFrame.setLoadMoreEnable(true);
            }
        }, 100);*/
        //下拉刷新
        mPtrFrame.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                list.clear();
                //模拟数据
                for (int i = 1; i < 7; i++) {
                    final List<SampleChildBean> childList = new ArrayList<>(i);

                    childList.add(new SampleChildBean("含本金38.39，利息6.72"));
                    SampleGroupBean sampleGroupBean = new SampleGroupBean(childList, String.valueOf(i));

                    sampleGroupBean.setDate("2017-10-21");
                    sampleGroupBean.setMoney("45.71");

                    list.add(sampleGroupBean);
                }
                //模拟联网 延迟更新列表
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (list != null && list.size() > 0) {
                            adapter.expandGroup(list.get(0));
                        }
                        mAdapter.notifyDataSetChanged();
                        mPtrFrame.refreshComplete();
                        mPtrFrame.setLoadMoreEnable(true);
                    }
                }, 1000);
            }
        });
        //上拉加载
        mPtrFrame.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {

                //模拟联网延迟更新数据
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        //模拟数据
                        if (list.size() >= 12) {
                            mPtrFrame.loadMoreComplete(true);
                            Toast.makeText(mActivity, "no more data", Toast.LENGTH_SHORT)
                                    .show();
                            return;
                        }
                        for (int i = 7; i < 14; i++) {
                            final List<SampleChildBean> childList = new ArrayList<>(i);

                            childList.add(new SampleChildBean("含本金38.39，利息6.72"));
                            SampleGroupBean sampleGroupBean = new SampleGroupBean(childList, String.valueOf(i));

                            sampleGroupBean.setDate("2017-10-21");
                            sampleGroupBean.setMoney("45.71");

                            list.add(sampleGroupBean);
                        }
                        mAdapter.notifyDataSetChanged();
                        mPtrFrame.loadMoreComplete(true);
                        Toast.makeText(mActivity, "load more complete", Toast.LENGTH_SHORT)
                                .show();
                    }
                }, 1000);


            }
        });

        mPopuWindow.showAtLocation(mRootView, Gravity.BOTTOM, 0, 0);
    }


    public void showLoanPeriodsPopuWindow(final List<String> list, final OnLoanPeriodSelectClick loanPeriodSelectClick) {
        View content = initPopuWindow(R.layout.popupwindow_loan_periods, 550, ViewGroup.LayoutParams.WRAP_CONTENT, R.style.AnimationRightFade);
        final MainGridView mListView = (MainGridView) content.findViewById(R.id.list_view);
        final LoanPeriodsAdapter mAdapter = new LoanPeriodsAdapter(mActivity, list);
        mListView.setAdapter(mAdapter);
        //默认选中第一期
        mAdapter.setSelectPosition(0);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mAdapter.setSelectPosition(position);
                loanPeriodSelectClick.selected(list.get(position));
            }
        });
        mPopuWindow.showAtLocation(mRootView, Gravity.RIGHT, 0, 500);

    }

    public interface OnLoanPeriodSelectClick {
        void selected(String period);
    }

}
