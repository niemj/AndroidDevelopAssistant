package com.example.jun.myapplication.widget.commonview;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jun.myapplication.R;


/**
 * Created by Ken on 2017/10/11.
 * Email: gfk0704@gmail.com
 * Comment: 公共规范的主题Item
 */

public class CommonTitleItemView extends RelativeLayout {

    public static final String TAG = CommonTitleItemView.class.getSimpleName();

    private TextView mTitleHolderTv;
    private TextView mTitleValueTv;

    public CommonTitleItemView(Context context) {
        super(context);
        initView(context, null);
    }

    public CommonTitleItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public CommonTitleItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        inflate(context, R.layout.view_common_title_item, this);
        mTitleHolderTv = findViewById(R.id.tv_title_holder);
        mTitleValueTv = findViewById(R.id.tv_title_value);
        ImageView mTitelIconIv = findViewById(R.id.iv_title_icon);
        TextView mTitleTv = findViewById(R.id.tv_title);
        ImageView mTilteArrowIv = findViewById(R.id.iv_title_arrow);
        if (attrs == null) {
            return;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CommonTitleItemView);
        int count = typedArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            int itemId = typedArray.getIndex(i);
            switch (itemId) {
                case R.styleable.CommonTitleItemView_icon:
                    mTitelIconIv.setVisibility(VISIBLE);
                    mTitelIconIv.setImageResource(typedArray.getResourceId(itemId, R.mipmap.ic_launcher));
                    break;
                case R.styleable.CommonTitleItemView_arrow:
                    mTilteArrowIv.setVisibility(VISIBLE);
                    mTilteArrowIv.setImageResource(typedArray.getResourceId(itemId, R.mipmap.more));
                    break;
                case R.styleable.CommonTitleItemView_place_holder:
                    mTitleHolderTv.setText(typedArray.getString(itemId));
                    break;
                case R.styleable.CommonTitleItemView_descriptions:
                    mTitleTv.setText(typedArray.getString(itemId));
                    break;
                case R.styleable.CommonTitleItemView_textsize:
                    mTitleTv.setTextSize(typedArray.getFloat(itemId, 16));
                    break;
                case R.styleable.CommonTitleItemView_textcolor:
                    mTitleTv.setTextColor(typedArray.getColorStateList(itemId));
                    break;
            }
        }
        typedArray.recycle();
    }

    public void setPlaceHolder(String placeHolder) {
        mTitleValueTv.setVisibility(GONE);
        mTitleHolderTv.setVisibility(VISIBLE);
        mTitleHolderTv.setText(placeHolder);
    }

    public void setValue(String value) {
        if (TextUtils.isEmpty(value)) {
            mTitleValueTv.setVisibility(GONE);
            mTitleHolderTv.setVisibility(VISIBLE);
        } else {
            mTitleValueTv.setVisibility(VISIBLE);
            mTitleHolderTv.setVisibility(GONE);
            mTitleValueTv.setText(value);
        }
    }

    public String getValue() {
        return mTitleValueTv.getText().toString();
    }

}
