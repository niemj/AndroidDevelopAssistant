package com.example.jun.myapplication.util;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import java.lang.ref.SoftReference;

/**
 * Created by JUN on 2018/2/1.
 */

public class ToastUtils {

    private ToastUtils() {

    }


    /**
     * 使用软引用保存 Context 为 Application 的 Toast 的实例
     */

    private static SoftReference<Toast> sToast = new SoftReference<>(null);


    /**
     * 弹出 Toast, 默认短时间
     *
     * @param context 指定 Context
     * @param text    消息内容
     */

    public static void show(Context context, String text) {

        show(context, text, Toast.LENGTH_SHORT);

    }


    /**
     * 弹出长时间 Toast
     *
     * @param context 指定 Context
     * @param text    消息内容
     */

    public static void showLong(Context context, String text) {

        show(context, text, Toast.LENGTH_LONG);

    }


    /**
     * 弹出新的 Toast, 默认短时间
     *
     * @param context 指定 Context
     * @param text    消息内容
     */

    public static void showNew(Context context, String text) {

        showNew(context, text, Toast.LENGTH_SHORT);

    }


    /**
     * 弹出新的长时间 Toast, 默认短时间
     *
     * @param context 指定 Context
     * @param text    消息内容
     */

    public static void showNewLong(Context context, String text) {

        showNew(context, text, Toast.LENGTH_LONG);

    }


    /**
     * 弹出 Toast
     *
     * @param context  指定 Context
     * @param text     消息内容
     * @param duration 指定时长, {@link Toast#LENGTH_SHORT} 或 {@link Toast#LENGTH_LONG}
     */

    private static void show(Context context, String text, int duration) {

        Toast toast = getToast(context, text, duration);

        toast.setText(text);

        toast.show();

    }


    /**
     * 弹出新的 Toast
     *
     * @param context  指定 Context
     * @param text     消息内容
     * @param duration 指定时长, {@link Toast#LENGTH_SHORT} 或 {@link Toast#LENGTH_LONG}
     */

    private static void showNew(Context context, String text, int duration) {

        Toast.makeText(context, text, duration).show();

    }


    /**
     * 根据不同的 Context 获取 Toast 实例
     *
     * @param context  指定 Context
     * @param text     消息内容
     * @param duration 指定时长, {@link Toast#LENGTH_SHORT} 或 {@link Toast#LENGTH_LONG}
     */

    @SuppressLint("ShowToast")

    private static Toast getToast(Context context, String text, int duration) {

        Toast toast;

        if (context instanceof Application) {

            toast = sToast.get();

            if (toast == null) {

                toast = Toast.makeText(context, text, duration);

                sToast = new SoftReference<>(toast);

            } else {

                toast.setDuration(duration);

            }

        } else {

            toast = Toast.makeText(context, text, duration);

        }

        return toast;

    }
}
