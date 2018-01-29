package com.example.jun.myapplication.widget.commonview;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jun.myapplication.R;

/**
 * Created by Ken on 2017/10/12.
 * Email: gfk0704@gmail.com
 * Comment: 公共规范的输入框
 */

public class CommonEditText extends RelativeLayout {

    private EditText mValueEt;
    private ImageView mTitleIndicateIv;

    public CommonEditText(Context context) {
        super(context);
        initView(context, null);
    }

    public CommonEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public CommonEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        inflate(context, R.layout.view_common_edit_text, this);

        mValueEt = (EditText) findViewById(R.id.et_value);
        mTitleIndicateIv = findViewById(R.id.iv_title_indicate);

        if (attrs == null) {
            return;
        }

        TextView mTitleTv = (TextView) findViewById(R.id.tv_title);
        ImageView mIconIv = (ImageView) findViewById(R.id.iv_title_icon);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CommonEditText);
        int count = typedArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            int itemId = typedArray.getIndex(i);
            switch (itemId) {
                case R.styleable.CommonEditText_et_icon:
                    mIconIv.setVisibility(VISIBLE);
                    mIconIv.setImageResource(typedArray.getResourceId(itemId, R.mipmap.ic_launcher));
                    break;
                case R.styleable.CommonEditText_et_indicate:
                    mTitleIndicateIv.setVisibility(VISIBLE);
                    mTitleIndicateIv.setImageResource(typedArray.getResourceId(itemId, R.mipmap.more));
                    break;
                case R.styleable.CommonEditText_et_descriptions:
                    mTitleTv.setText(typedArray.getString(itemId));
                    break;
                case R.styleable.CommonEditText_et_place_holder:
                    mValueEt.setHint(typedArray.getString(itemId));
                    break;
                case R.styleable.CommonEditText_et_single_line:
                    mValueEt.setSingleLine(typedArray.getBoolean(itemId, false));
                    break;
                case R.styleable.CommonEditText_etc_max_length:
                    mValueEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(typedArray.getIndex(itemId))});
                    break;
            }
        }
        typedArray.recycle();
    }

    public void setValue(String value) {
        mValueEt.setText(value);
    }

    public String getValue() {
        return mValueEt.getText().toString();
    }

    public EditText getValueEt() {
        return mValueEt;
    }

    public ImageView getTitleIndicateIv() {
        return mTitleIndicateIv;
    }
}
