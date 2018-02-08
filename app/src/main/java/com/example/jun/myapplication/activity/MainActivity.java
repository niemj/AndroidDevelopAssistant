package com.example.jun.myapplication.activity;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jun.myapplication.R;
import com.example.jun.myapplication.bean.SampleChildBean;
import com.example.jun.myapplication.bean.SampleGroupBean;
import com.example.jun.myapplication.bean.event.MessageEvent;
import com.example.jun.myapplication.source.NavigationBar;
import com.example.jun.myapplication.util.PermissionUtils;
import com.example.jun.myapplication.util.PopupPickerHelper;
import com.example.jun.myapplication.util.ToastUtils;
import com.example.jun.myapplication.util.Utils;
import com.mylhyl.acp.AcpListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MainActivity extends BaseActivity implements View.OnClickListener {
    private PopupPickerHelper mPopuUtils;
    private View mRootView;
    private List<SampleGroupBean> mRepaymentPlanList;
    private NavigationBar mNavigationBar;
    private TextView mMessageTV;
    private List<String> mList;

    private ComponentName mDefault;
    private ComponentName mAlias;
    private ComponentName mAlias1;
    private PackageManager mPm;

    public static boolean isForeground = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //隐藏状态栏和ActionBar
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        registerMessageReceiver();  // used for receive msg

        //注册EventBus
        EventBus.getDefault().register(this);

        initIcon();
        initAcp();

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_main);
        mNavigationBar = (NavigationBar) findViewById(R.id.nb_title);
        mNavigationBar.setBackgroundColor(getResources().getColor(R.color.white));
        mNavigationBar.setLeftImageResource(R.mipmap.nav_back);
        mNavigationBar.setLeftClickListener(mNBLeftListener);
        mNavigationBar.setTitle("主界面");

        mRootView = findViewById(R.id.rootView);
        mPopuUtils = new PopupPickerHelper(this, mRootView);
        mMessageTV = findViewById(R.id.tv_message);

        Button btnRepaymentDetail = findViewById(R.id.btn_repayment_detail);
        Button btnExpandList = findViewById(R.id.btn_expand_list);
        Button btnShoppingCart = findViewById(R.id.btn_shopping_cart);
        Button btnOpenWebView = findViewById(R.id.btn_open_webview);
        Button btnReadContact = findViewById(R.id.btn_read_contact);
        Button btnSildePopup = findViewById(R.id.btn_sidle_popup);
        Button btnTest = findViewById(R.id.btn_test);

        btnRepaymentDetail.setOnClickListener(this);
        btnExpandList.setOnClickListener(this);
        btnShoppingCart.setOnClickListener(this);
        btnOpenWebView.setOnClickListener(this);
        btnReadContact.setOnClickListener(this);
        btnSildePopup.setOnClickListener(this);
        btnTest.setOnClickListener(this);

    }


    /**
     * NavigationBar返回键监听器
     */
    private View.OnClickListener mNBLeftListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPopuUtils.showBackPopuWindow();
        }
    };

    @Override
    public void initData() {
        mRepaymentPlanList = new ArrayList<>();
        mList = new ArrayList<>();
        mList.add("3期");
        mList.add("6期");
        mList.add("9期");
        mList.add("12期");
        mList.add("18期");
        mList.add("24期");
        mList.add("36期");

        for (int i = 1; i < 7; i++) {
            final List<SampleChildBean> childList = new ArrayList<>(i);
            childList.add(new SampleChildBean("含本金38.39，利息6.72"));

            SampleGroupBean sampleGroupBean = new SampleGroupBean(childList, String.valueOf(i));
            sampleGroupBean.setDate("2017-10-21");
            sampleGroupBean.setMoney("45.71");

            mRepaymentPlanList.add(sampleGroupBean);
        }
    }


    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
    }


    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        //注销EventBus
        EventBus.getDefault().unregister(this);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    /**
     * 动态更换桌面图标
     */
    private void initIcon() {
        mDefault = new ComponentName(getBaseContext(), "com.example.jun.myapplication.activity.MainActivityDefault");
        mAlias = new ComponentName(getBaseContext(), "com.example.jun.myapplication.activity.MainActivityAlias");
        mAlias1 = new ComponentName(getBaseContext(), "com.example.jun.myapplication.activity.MainActivityAlias1");
        mPm = getApplicationContext().getPackageManager();

        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();

        calendar.set(2018, 2, 2);//活动开始时间2018-03-02
        Date yearTime = calendar.getTime();

        calendar.set(2018, 2, 7);
        Date girlStart = calendar.getTime();

        calendar.set(2018, 2, 8);
        Date girlEnd = calendar.getTime();

        if (now.compareTo(yearTime) <= 0) {
            disableComponent(mDefault);
            enableComponent(mAlias);
            disableComponent(mAlias1);

        } else if (now.compareTo(girlStart) >= 0 && now.compareTo(girlEnd) <= 0) {
            disableComponent(mDefault);
            disableComponent(mAlias);
            enableComponent(mAlias1);

        } else {
            enableComponent(mDefault);
            disableComponent(mAlias);
            disableComponent(mAlias1);
        }
    }

    /**
     * 申请相关权限
     */
    private void initAcp() {
        if (Utils.isDeviceRooted()) {
            ToastUtils.showLong(this, "检测到您的手机已经被Root了！");
        }

        PermissionUtils.requestPermissions(this, new AcpListener() {
            @Override
            public void onGranted() {
            }

            @Override
            public void onDenied(List<String> permissions) {
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_repayment_detail:
                mPopuUtils.showRepaymenPlanPopWindow(mRepaymentPlanList);
                break;
            case R.id.btn_read_contact:
                startActivity(new Intent(MainActivity.this, PersonInfoActivity.class));
                break;
            case R.id.btn_expand_list:
                startActivity(new Intent(MainActivity.this, ExpandListViewActivity.class));
                break;
            case R.id.btn_shopping_cart:
                startActivity(new Intent(MainActivity.this, ShoppingCartActivity.class));
                break;
            case R.id.btn_open_webview:
                startActivity(new Intent(MainActivity.this, WebViewActivity.class));
                break;
            case R.id.btn_sidle_popup:
                mPopuUtils.showLoanPeriodsPopuWindow(mList, new PopupPickerHelper.OnLoanPeriodSelectClick() {
                    @Override
                    public void selected(String period) {
                        mMessageTV.setText(period);
                    }
                });
                break;
            case R.id.btn_test:
                handler.sendEmptyMessageDelayed(0, 3000);
                break;
            default:
        }

    }

    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(MainActivity.this, "消息发送成功！！", Toast.LENGTH_SHORT).show();
        }
    };


    private void enableComponent(ComponentName componentName) {
        mPm.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    private void disableComponent(ComponentName name) {
        mPm.setComponentEnabledSetting(name, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }


    @Subscribe
    public void onEventMainThread(MessageEvent event) {

    }

    /**
     * for receive customer msg from jpush server
     */
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                    if (!TextUtils.isEmpty(extras)) {
                        showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                    }
                    setCostomMsg(showMsg.toString());
                }
            } catch (Exception e) {
            }
        }
    }

    private void setCostomMsg(String msg) {
       /* if (null != mMessageTV) {
            mMessageTV.setText("接收到的消息为：" + msg);
        }*/
    }


    /**
     * 带按钮的通知栏
     */
    private void showNotification(Context context, int id, String title, String text) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(title);
        builder.setContentText(text);
        builder.setAutoCancel(true);
        builder.setOnlyAlertOnce(true);
        // 需要VIBRATE权限
        builder.setDefaults(Notification.DEFAULT_VIBRATE);
        builder.setPriority(Notification.PRIORITY_DEFAULT);

        // Creates an explicit intent for an Activity in your app
        //自定义打开的界面
        Intent resultIntent = new Intent(context, ShoppingCartActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, builder.build());
    }

}
