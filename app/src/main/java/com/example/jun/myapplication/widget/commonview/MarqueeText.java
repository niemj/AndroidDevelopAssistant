package com.example.jun.myapplication.widget.commonview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;

import com.example.jun.myapplication.R;

/**
 * Created by JUN on 2018/1/29.
 * 不够流畅，待优化
 */

public class MarqueeText extends android.support.v7.widget.AppCompatTextView implements Runnable {
    private int currentScrollX;// 当前滚动的位置
    private boolean isStop = false;
    private int textWidth;
    private boolean isMeasure = false;

    public int timeSpeed = 10;
    int point;
    TYPE_STYLE type;

    enum TYPE_STYLE {  //左出，右出两种方式
        LEFY_OUT, RIGHT_OUT
    }

    public MarqueeText(Context context) {
        this(context, null);
    }

    public MarqueeText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MarqueeText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MarqueeText);
        timeSpeed = a.getInteger(R.styleable.MarqueeText_speed, 10);
        point = a.getInteger(R.styleable.MarqueeText_type, 1);
        if (point == 1) {
            type = TYPE_STYLE.LEFY_OUT;
        } else {
            type = TYPE_STYLE.RIGHT_OUT;
        }
        a.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 文字宽度只需获取一次就可以了
        if (!isMeasure) {
            getTextWidth();
            isMeasure = true;
        }
    }

    /**
     * 获取文字宽度
     */
    private void getTextWidth() {
        Paint paint = this.getPaint();
        String textDesc = this.getText().toString();
        textWidth = (int) paint.measureText(textDesc);
    }

    @Override
    public void run() {
        if (type == TYPE_STYLE.LEFY_OUT) {
            currentScrollX += 1;
            scrollTo(currentScrollX, 0);
            if (isStop) {
                return;
            }
            if (getScrollX() >= textWidth) {
                Log.i("main", "====换行==" + currentScrollX + "/===" + textWidth + "//===" + getWidth());
                scrollTo(-getWidth(), 0);
                currentScrollX = -getWidth();
            }
        } else if (type == TYPE_STYLE.RIGHT_OUT) {
            currentScrollX -= 1;
            scrollTo(currentScrollX, 0);
            if (isStop) {
                return;
            }
            if (getScrollX() <= -(this.getWidth())) {
                scrollTo(textWidth, 0);
                currentScrollX = textWidth;
            }
        }
        postDelayed(this, timeSpeed);
    }

    // 开始滚动
    public void startScroll() {
        isStop = false;
        this.removeCallbacks(this);
        post(this);
    }

    // 停止滚动
    public void stopScroll() {
        isStop = true;
    }

    // 从头开始滚动
    public void startForStart() {
        currentScrollX = 0;
        startScroll();
    }


}
