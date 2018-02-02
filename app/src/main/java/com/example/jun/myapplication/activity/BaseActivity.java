package com.example.jun.myapplication.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.debug.hv.ViewServer;
import com.example.jun.myapplication.ActivityCollector;
import com.example.jun.myapplication.BaseApplication;
import com.example.jun.myapplication.util.AntiHijackingUtil;
import com.example.jun.myapplication.util.ToastUtils;

/**
 * @author JUN
 * @date 2017/8/29
 */
@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("BaseActivity", getClass().getSimpleName());
        ActivityCollector.addActivity(this);
        ViewServer.get(this).addWindow(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
        ViewServer.get(this).removeWindow(this);
        BaseApplication.getRefWatcher(this).watch(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ViewServer.get(this).setFocusedWindow(this);
        // JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // JPushInterface.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        boolean safe = AntiHijackingUtil.checkActivity(this);
        if (!safe) {
            ToastUtils.showLong(this, "您的应用怀疑被虚假界面所劫持，请注意不要泄露您的账户、密码！");
        }
    }
}
