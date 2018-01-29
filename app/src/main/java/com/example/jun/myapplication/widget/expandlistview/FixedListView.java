package com.example.jun.myapplication.widget.expandlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * Created by JUN on 2017/12/28.
 * listView与ScrollView嵌套使用，会出现listView最多仅显示一行的情况，改写listView的onMeasure方法即可解决。
 */

public class FixedListView extends ExpandableListView {

    private boolean fixed = true;

    public FixedListView(Context context) {
        super(context);
    }

    public FixedListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FixedListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void isFixed(boolean fixed){
        this.fixed = fixed;
    }

    /**
     * 重写该方法，达到使ListView适应ScrollView的效果
     * 可设置isFixed取消该效果
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = heightMeasureSpec;
        if(fixed) {
            expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}