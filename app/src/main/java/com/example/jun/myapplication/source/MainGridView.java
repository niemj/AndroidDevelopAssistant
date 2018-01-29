package com.example.jun.myapplication.source;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * <p>
 * Title: MainGridView.java
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * CreateTime：16/9/6
 * </p>
 *
 * @author 作者名
 * @version common v1.0
 */
public class MainGridView extends GridView {
    public MainGridView(Context context) {
        super(context);
    }

    public MainGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}