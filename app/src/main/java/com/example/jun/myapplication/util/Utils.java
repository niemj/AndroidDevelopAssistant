package com.example.jun.myapplication.util;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.jun.myapplication.util.crypto.MD5Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.NetworkInterface;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utils {

    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;//挂载了sdcard，返回真
        } else {
            return false;//否则返回假
        }
    }

    /**
     * APK文件安装
     *
     * @param context 上下文
     * @param file    apk文件
     */
    public static void installApk(Activity context, @NonNull File file) {
        if (!file.exists()) {
            Toast.makeText(context, "安装异常", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(context, "com.kernal.passportreader.myapplication.fileprovider", file);
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            }
            context.startActivity(intent);
            context.finish();
        } catch (Exception e) {
            Toast.makeText(context, "安装异常", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 检测相机是否可用
     *
     * @return true：可用
     */

    public static boolean cameraIsCanUse() {
        boolean isCanUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            Camera.Parameters mParameters = mCamera.getParameters();
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            isCanUse = false;
        }
        if (mCamera != null) {
            try {
                mCamera.release();
                mCamera = null;
            } catch (Exception e) {
                e.printStackTrace();
                return isCanUse;
            }
        }
        return isCanUse;
    }

    public static final int ERROR_PATCH_GOOGLEPLAY_CHANNEL = -6;
    public static final int ERROR_PATCH_ROM_SPACE = -7;
    public static final int ERROR_PATCH_MEMORY_LIMIT = -8;
    public static final int ERROR_PATCH_CRASH_LIMIT = -9;
    public static final int ERROR_PATCH_CONDITION_NOT_SATISFIED = -10;
    public static final int ERROR_PATCH_ALREADY_APPLY = -11;
    public static final int ERROR_PATCH_RETRY_COUNT_LIMIT = -12;
    public static final String PLATFORM = "platform";

    public static final int MIN_MEMORY_HEAP_SIZE = 45;

    private static boolean background = false;

    /**
     * sendMessage: 一步发消息
     */
    public static void sendMessage(Handler handler, int msgWhat, Bundle msgData) {
        Message msg = handler.obtainMessage(msgWhat);
        msg.setData(msgData);
        msg.sendToTarget();
    }

    /**
     * 把px转化dip
     */
    public static int px2dip(Context context, float px) {
        float density = context.getResources().getDisplayMetrics().density;// 密度
        int dip = (int) (px * (density / 1));
        return dip;
    }

    /**
     * 获取系统版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        int versionCode = -1;
        try {
            versionCode = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionCode;

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取设备型号
     *
     * @return
     */
    public static String getDevVersion() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取设备系统版本
     *
     * @return
     */
    public static String getDevSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取屏幕大小 0:width 1:height
     */
    public static String getScreenSize(Context context, int type) {
        // Display dis = ((Activity) context).getWindowManager()
        // .getDefaultDisplay();
        // int screenWidth = dis.getWidth();
        // int screenHeight = dis.getHeight();
        // int size=0;
        // switch (type) {
        // case 0:
        // size=screenWidth;
        // break;
        //
        // case 1:
        // size=screenHeight;
        // break;
        // }
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getApplicationContext().getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;// 屏幕宽（像素，如：480px）
        int screenHight = dm.heightPixels;
        String str = "" + screenWidth + "x" + screenHight;
        return str;
    }

    /**
     * 得到系统当前时间 格式为yyyy-MM-dd return
     */
    public static String getCurrentDate(String time) {
        time = time.trim();
        if ("".equals(time) || time == null) {
            return "无此项内容";
        } else {
            long minl = Long.parseLong(time);
            Date date = new Date(minl);
            String currentSysDate = new SimpleDateFormat("yyyy-MM-dd")
                    .format(date);
            return currentSysDate;
        }
    }

    public static String getCurrentDates(String time) {
        time = time.trim();
        if ("".equals(time) || time == null) {
            return "无此项内容";
        } else {
            long minl = Long.parseLong(time);
            Date date = new Date(minl);
            String currentSysDate = new SimpleDateFormat("yyyy.MM.dd")
                    .format(date);
            return time;
        }
    }

    public static String formatDate(Date formatDate) {
        String currentSysDate = new SimpleDateFormat("yyyy-MM-dd")
                .format(formatDate);

        return currentSysDate;
    }

    /**
     * yyyymmdd 字符串转换成yyyy-MM-dd字符串
     */
    public static String formatdate(String datestring) {
        String date1;
        if (null != datestring && !"".equals(datestring)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            Date a;
            try {
                a = sdf.parse(datestring);
                date1 = sdf1.format(a);
                return date1;
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                return "";
            }
        } else {
            return "";
        }
    }

    /**
     * 基金类型
     */
    public static String getFundType(String type) {
        String str = null;
        switch (type) {
            case "0":
                str = "其他类型基金";
                break;
            case "1":
                str = "股票型基金";
                break;
            case "2":
                str = "债券型基金";
                break;
            case "3":
                str = "混合型基金";
                break;
            case "4":
                str = "货币型基金";
                break;
            case "5":
                str = "保本型基金";
                break;
            case "6":
                str = "指数型基金";
                break;
            case "7":
                str = "QDII基金";
                break;
            case "8":
                str = "商品型基金";
                break;
            default:
                str = "其他类型基金";
                break;
        }
        return str;
    }

    public static String getBigDate(int week) {
        String str = null;
        switch (week) {
            case 1:
                str = "一";
                break;
            case 2:
                str = "二";
                break;
            case 3:
                str = "三";
                break;
            case 4:
                str = "四";
                break;
            case 5:
                str = "五";
                break;
            default:
                str = "一";
                break;
        }
        return str;
    }

    public static String parseAllotRate(List<Map<String, Object>> list) {
        if (list == null || list.size() == 0) {
            return "暂无";
        }
        StringBuilder sb = new StringBuilder();
        for (Map<String, Object> map : list) {
            String lowerLimitInclusive = ObjCastUtil.castBoolean(map.get("lowerLimitInclusive")) ? "<=" : "<";
            String upperLimitInclusive = ObjCastUtil.castBoolean(map.get("upperLimitInclusive")) ? "<=" : "<";
            String upperLimit = null;
            String lowerLimit = ObjCastUtil.castString(map.get("lowerLimit"));
            String fee = null;
            float feeRatio = ObjCastUtil.castFloat(map.get("feeRatio")) / 100;
            float fixedFee = ObjCastUtil.castFloat(map.get("fixedFee"));
            if (feeRatio > 0.00001) {
                fee = StringUtil.formatRateString(feeRatio);
            } else if (fixedFee > 0.00001) {
                fee = StringUtil.formatMoneyString(fixedFee).replace(".00", "");
            } else {
                fee = "";
            }
            if ("INF".equals(map.get("upperLimit"))) {
                upperLimit = "";
            } else {
                upperLimit = String.format(Locale.CHINA, "%.0f万元", ObjCastUtil.castFloat(map.get("upperLimit")) / 10000.0);
            }
            if (ObjCastUtil.castInteger(lowerLimit) <= 0) {
                sb.append(String.format(Locale.CHINA, "申购金额%s%s：%s\n", upperLimitInclusive, upperLimit, fee));
            } else {
                sb.append(String.format(Locale.CHINA, "%.0f万元%s申购金额%s%s：%s\n",
                        ObjCastUtil.castFloat(lowerLimit) / 10000.0, lowerLimitInclusive, "INF".equals(map.get("upperLimit")) ? "" : upperLimitInclusive, upperLimit, fee));
            }
        }
        return sb.toString();
    }

    /**
     * 风险类型
     */
    public static String getRiskLevel(String level) {
        String str = null;
        switch (level) {
            case "0":
                str = "未评估过";
                break;
            case "1":
                str = "保守型";
                break;
            case "2":
                str = "稳健型";
                break;
            case "3":
                str = "进取型";
                break;
            default:
                str = "未评估过";
                break;
        }
        return str;
    }

    /**
     * 风险类型
     */
    public static String getRiskLevelGrade(String level) {
        String str = null;
        switch (level) {
            case "0":
                str = "未评估过";
                break;
            case "1":
                str = "10分以下";
                break;
            case "2":
                str = "11-25分";
                break;
            case "3":
                str = "26分以上";
                break;
            default:
                str = "未评估过";
                break;
        }
        return str;
    }

    /**
     * 风险类型
     */
    public static String getDividendMethod(Object type) {
        String str = null;
        switch (ObjCastUtil.castString(type)) {
            case "1":
                str = "现金分红";
                break;
            case "0":
                str = "红利资金再投";
                break;
            default:
                str = "暂无";
                break;
        }
        return str;
    }


    /**
     * 判断是什么币种,转成成英文
     */
    public static String getCurrency_En(String str) {
        if (str.equals("人民币")) {
            return "CNY";
        } else if (str.equals("澳大利亚元")) {
            return "AUD";
        } else if (str.equals("加拿大元")) {
            return "CAD";
        } else if (str.equals("瑞士法郎")) {
            return "AHF";
        } else if (str.equals("欧元")) {
            return "EUR";
        } else if (str.equals("英镑")) {
            return "GBP";
        } else if (str.equals("港币")) {
            return "HKD";
        } else if (str.equals("日元")) {
            return "JPY";
        } else if (str.equals("新西兰元")) {
            return "NZD";
        } else if (str.equals("瑞典克郎")) {
            return "SEK";
        } else if (str.equals("新加坡元")) {
            return "SGD";
        } else if (str.equals("美元")) {
            return "USD";
        } else {
            return "CNY";
        }
    }

    /**
     * 判断是什么币种,转成成中文
     */
    public static String getCurrency_Zh(String str) {
        if (str.equals("CNY")) {
            return "人民币";
        } else if (str.equals("AUD")) {
            return "澳大利亚元";
        } else if (str.equals("CAD")) {
            return "加拿大元";
        } else if (str.equals("AHF")) {
            return "瑞士法郎";
        } else if (str.equals("EUR")) {
            return "欧元";
        } else if (str.equals("GBP")) {
            return "英镑";
        } else if (str.equals("HKD")) {
            return "港币";
        } else if (str.equals("JPY")) {
            return "日元";
        } else if (str.equals("NZD")) {
            return "新西兰元";
        } else if (str.equals("SEK")) {
            return "瑞典克郎";
        } else if (str.equals("SGD")) {
            return "新加坡元";
        } else if (str.equals("USD")) {
            return "美元";
        } else {
            return "人民币";
        }
    }

    /**
     * 判断传入字符是否为空，全空格算作空 param str return
     */
    public static boolean isStringNull(String str) {
        if (str.equals("")) {
            return true;
        } else {
            return false;
        }
    }


    // 转换交易类型
    public static String getTransCode(String str) {
        if (str.equals("PCreditCardPreApply")) {
            return "信用卡预申请";
        } else if (str.equals("RelaActMng")) {
            return "注册账户管理";
        } else if (str.equals("UserCifMgmtPre")) {
            return "个人信息管理";
        } else if (str.equals("UserIdModifyPre")) {
            return "用户名修改";
        } else if (str.equals("UserInfoMgmtPre")) {
            return "客户信息管理";
        } else if (str.equals("UsrActLimitMng")) {
            return "账户限额设置";
        } else if (str.equals("WaterPrePayee")) {
            return "嘉定自来水";
        } else if (str.equals("p163net")) {
            return "16300上网账户管理";
        } else if (str.equals("paccountmanage")) {
            return "账户管理";
        } else {
            return "";
        }
    }

    // 转换交易状态vmkwdgiamaroajnk
    public static String getTransState(String str) {
        if (str.equals("0")) {
            return "成功";
        } else if (str.equals("1")) {
            return "通讯失败";
        } else if (str.equals("2")) {
            return "失败";
        } else if (str.equals("3")) {
            return "交易异常";
        } else if (str.equals("4")) {
            return "银行拒绝";
        } else if (str.equals("5")) {
            return "正在发送主机";
        } else if (str.equals("6")) {
            return "预约成功";
        } else if (str.equals("7")) {
            return "已经撤销";
        } else if (str.equals("9")) {
            return "状态未明";
        } else {
            return "交易失败";
        }
    }


    /**
     * 去掉金额的逗号
     *
     * @param str
     * @return
     */
    public static String getAmount(String str) {
        String s = "";
        if (str.contains(",")) {
            String ss[] = str.split("\\,");
            for (int i = 0; i < ss.length; i++) {
                s += ss[i];
            }
        } else {
            s = str;
        }

        return s;
    }

    /**
     * 去掉金钱后的元
     *
     * @param str
     * @return
     */
    public static String getdeleYuan(String str) {
        str = getAmount(str);
        if ("元".equals(str)) {
            return "0";
        }
        if ("".equals(str)) {
            return "0";
        }
        if (str.contains("元")) {
            String strs = str.substring(0, str.length() - 1);
            return strs;
        } else {
            return str;
        }
    }


    /**
     * 判断输入的字符串是否是空
     *
     * @param inStr 输入字符串
     * @return
     */
    public static boolean isNullOrEmpty(String inStr) {
        return (inStr == null || inStr.trim().length() == 0);
    }

    /**
     * 判断对象是否是null或者是""
     *
     * @param obj
     * @return
     */
    public static boolean isNullOrEmpty(Object obj) {
        if (obj instanceof Object[]) {
            Object[] o = (Object[]) obj;
            for (int i = 0; i < o.length; i++) {
                Object object = o[i];
                if (object instanceof Date) {
                    if (object.equals(new Date(0)))
                        return true;
                } else if ((object == null) || (("").equals(object))) {
                    return true;
                }
            }
        } else {
            if (obj instanceof Date) {
                if (obj.equals(new Date(0))) {
                    return true;
                }
            } else if ((obj == null) || (("").equals(obj.toString()))) {
                return true;
            }
        }
        return false;
    }

    public static String formatDouble(double d1) {
        DecimalFormat df = new DecimalFormat("0.##");
        return df.format(d1);
    }

    /**
     * 根据当前日期计算出与当前日期间隔天数的日期
     *
     * @return
     */
    public static Date rollDateByDay(Date currentDate, int day, boolean prev) {
        Calendar calendar = GregorianCalendar.getInstance(Locale.getDefault());
        if (currentDate != null) {
            calendar.setTime(currentDate);
        }
        calendar.add(Calendar.DATE, prev ? -day : day);
        return new Date(calendar.getTime().getTime());
    }

    public static Date rollDateByMonth(Date currentDate, int month, boolean prev) {
        Calendar calendar = GregorianCalendar.getInstance(Locale.getDefault());
        if (currentDate != null) {
            calendar.setTime(currentDate);
        }
        calendar.add(Calendar.MONTH, prev ? -month : month);
        if (month == 12) {
            calendar.add(Calendar.DATE,
                    (int) (prev ? +1 : (calendar.getTime().getTime())));
        }
        return new Date(calendar.getTime().getTime());
    }

    public static Date rollDateByMonth(String currentDate, int month,
                                       boolean prev) {
        Calendar calendar = GregorianCalendar.getInstance(Locale.getDefault());
        if (currentDate != "") {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = sdf.parse(currentDate);
                calendar.setTime(date);
            } catch (ParseException e) {
            }
        }
        calendar.add(Calendar.MONTH, prev ? -month : month);
        if (month == 12) {
            calendar.add(Calendar.DATE,
                    (int) (prev ? +1 : (calendar.getTime().getTime())));
        }
        return new Date(calendar.getTime().getTime());
    }

    public static boolean isAmountStyle(String value) {
        // 允许格式为：1000，1000.00或1,000,000,000，1,000,000,000.00
        String s = "^(\\d{1,10}|\\d{1,3}(,\\d{3})*)(\\.\\d{1,2})?$";
        Pattern p = Pattern.compile(s);
        String strAmount = value;
        if (!p.matcher(value).matches())
            return false;
        if (value.indexOf(",") > 0)
            strAmount = value.replaceAll(",", "");// 分割成整数与小数部分
        BigDecimal val = new BigDecimal(strAmount);
        if (!val.toString().equals(strAmount))
            return false;

        return true;
    }

    /**
     * 校验付款用途长度
     */
    public static String validateFieldLength(String field, int cnLength,
                                             int byteLength) {
        Pattern p = Pattern.compile("[^\\x00-\\xff]");
        if (field.indexOf('%') > -1 || field.indexOf(':') > -1
                || field.indexOf('"') > -1 || field.indexOf('\'') > -1) {
            return "1";// 不支持半角字符% : ' \" 输入！/throw new
            // ValidationRuntimeException(CHECKMSG.VALIDATION_CROSSBANK_STYLE_HALF,new
            // Object[]{fieldName});
        }
        int vallength = 0; // 字节数
        int valCnLength = 0; // 汉字数
        for (int i = 0; i < field.length(); i++) {
            String word = field.substring(i, i + 1);
            if (p.matcher(word).matches()) { // 判断是否为汉字，为汉字字节数加2，汉字数加1
                vallength += 2;
                valCnLength++;

            } else {
                vallength++;
            }
            if (valCnLength > cnLength) {
                return "2"; // 输入汉字长度大于cnLength！;
            } else if (vallength > byteLength) {
                return "3";// 输入字节数大于byteLength！;
            }
        }

        return "0";
    }


    /**
     * 获取屏幕大小
     */
    public static String getScreenSize(Context context) {
        Display dis = ((Activity) context).getWindowManager()
                .getDefaultDisplay();
        int screenWidth = dis.getWidth();
        int screenHeight = dis.getHeight();
        String string = screenWidth + "*" + screenHeight;
        return string;
    }

    /**
     * 获取IMEI
     */
    public static String getIMEI(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null) {
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                return tm.getDeviceId();
            }
        }
        return "";
    }

    public static String getWifiMac(Context context) {
        String macSerial = null;
        String str1 = getWifiMacByManager(context);
        if (!TextUtils.isEmpty(str1)) {
            macSerial = str1;
        } else {
            String str2 = getWifiMacByInterface();
            if (!TextUtils.isEmpty(str2)) {
                macSerial = str2;
            }
        }
        if (macSerial != null) {
            macSerial = macSerial.replace(":", "-");
        }
        return macSerial;
    }

    /**
     * 只要打开过wifi,就有效，不过对Android6.0无效
     *
     * @param context
     * @return
     */
    public static String getWifiMacByManager(Context context) {
        try {
            WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wi = wm.getConnectionInfo();
            if (wi == null || wi.getMacAddress() == null) {
                return null;
            }
            if ("02:00:00:00:00:00".equals(wi.getMacAddress())) {
                return null;
            } else {
                return wi.getMacAddress().trim();
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 亲测wifi开启情况下，均有效; wifi关闭下无效
     *
     * @return
     */
    public static String getWifiMacByInterface() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) {
                    continue;
                }
                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }
                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }
                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                if ("02:00:00:00:00:00".equals(res1.toString())) {
                    return null;
                } else {
                    return res1.toString();
                }
            }
        } catch (Exception ex) {
            return null;
        }
        return null;
    }

    public static String getSer() {
        try {
            return Build.SERIAL;
        } catch (Exception e) {
            return "";
        }
    }

    private static String getAndroidId(Context context) {
        try {
            return Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            return "";
        }
    }

    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class<?> clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(context, permission);
                if (rest == PackageManager.PERMISSION_GRANTED) {
                    result = true;
                } else {
                    result = false;
                }
            } catch (Exception e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }

    /**
     * 优先使用serial number && android id ,其次使用wifi mac(wifi开机没打开过会没有),再实在没有就随机生成
     *
     * @param context
     * @return
     */
    public static String getUDID(Context context) {
       /* String udid = DataCenter.GetSaveLocal("UDID", "", context).toString();
        if (!TextUtils.isEmpty(udid)) {
            Log.d("UDID", "c：" + udid);
            return udid;
        }*/
        StringBuilder sb = new StringBuilder();
        //获取serial number
        String serialNumber = getSer();
        if (!TextUtils.isEmpty(serialNumber) && !"unknown".equals(serialNumber)) {
            sb.append(serialNumber);
            Log.d("UDID", "s：" + serialNumber);
        }
        //获取android id
        String androidId = getAndroidId(context);
        if (!TextUtils.isEmpty(androidId) && !androidId.equals("null") && !"9774d56d682e549c".equals(androidId)) {
            sb.append(androidId);
            Log.d("UDID", "a：" + androidId);
        }
        if (TextUtils.isEmpty(sb.toString())) {
            //获取wifi mac
            String wifiMac = getWifiMac(context);
            if (!TextUtils.isEmpty(wifiMac)) {
                sb.append(wifiMac);
                Log.d("UDID", "w：" + wifiMac);
            }
        }
        if (TextUtils.isEmpty(sb.toString())) {
            sb.append(UUID.randomUUID().toString());
            Log.d("UDID", "u：" + UUID.randomUUID().toString());
        }
        Log.d("UDID", "f：" + sb.toString());
      /*  DataCenter.PutSaveLocal("UDID", MD5Util.md5(sb.toString()), context);*/
        return MD5Util.md5(sb.toString());
    }

    /*public static String getUDID(Context context) {
        StringBuilder sb = new StringBuilder();
        String deviceId = getDeviceId(context);
        if (!TextUtils.isEmpty(deviceId) && !deviceId.equals("null")) {
            sb.append(deviceId);
        }
        String mac = getMac(context);
        sb.append(mac);
        if (sb.length() <= mac.length() && sb.toString().contains("aa-aa")) {
            // 如果还是空就给个随机值
            sb.append(UUID.randomUUID().toString());
        }
        return MD5.getMD5(sb.toString());
    }*/

    /**
     * 加密账号
     */
    public static String getEncryptAcNo(String acNo) {

        return acNo.substring(0, 4) + "****"
                + acNo.substring(acNo.length() - 4, acNo.length());
    }

    public static ProgressDialog initProgressDialog(ProgressDialog progdialog) {
        progdialog.setTitle("请稍后...");
        progdialog.setMessage("数据获取中.....");
        progdialog.setIndeterminate(true);
        progdialog.setCancelable(false);
        return progdialog;
    }

    // 对返回的json串进行验证，返回JSONObject
    public static JSONObject validateReturnCode(final String jsonObjString) {
        JSONObject jsonObj = null;
        JSONArray jsonArray = null;
        String retString = jsonObjString.trim();
        try {
            if (retString == null || "".equals(retString.trim())) {
                retString = "{}";
            }
            if (retString.startsWith("[")) {
                jsonArray = new JSONArray(retString);
            } else {
                jsonObj = new JSONObject(retString);
            }
            if (jsonObj == null) {
                jsonObj = new JSONObject();
                jsonObj.put("List", jsonArray);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObj;
    }

    /**
     * 获取版本信息 by xuwei
     *
     * @param context
     * @return
     */
    public static String getAppInfo(Context context) {
        try {
            String pkName = context.getPackageName();
            String versionName = context.getPackageManager().getPackageInfo(
                    pkName, 0).versionName;
            int versionCode = context.getPackageManager().getPackageInfo(
                    pkName, 0).versionCode;
            // return pkName + "   " + versionName + "  " + versionCode;
            return versionName;
        } catch (Exception e) {
        }
        return null;
    }

    public static String getVersionName(Context context) {
        String versionName = null;
        try {
            String pkName = context.getPackageName();

            versionName = "" + context.getPackageManager().getPackageInfo(
                    pkName, 0).versionName;
            // return pkName + "   " + versionName + "  " + versionCode;
            return versionName;
        } catch (Exception e) {
        }
        return versionName;
    }

    public static Context getActivityByName(String name) {
        Context a = null;
        try {
            a = (Context) Class.forName(name).newInstance();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return a;
    }

    public static HashMap<String, String> jsonToMap(String upDataForTrs) {
        HashMap<String, String> upData = new HashMap<String, String>();
        if (upDataForTrs != null && !upDataForTrs.equals("")) {
            try {
                JSONObject json = new JSONObject(upDataForTrs);
                Iterator keyIter = json.keys();
                while (keyIter.hasNext()) {
                    String key = (String) keyIter.next();
                    String value = (String) json.get(key);
                    upData.put(key, value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return upData;
    }

    // 校验形式
    public static boolean patternStyle(String value, String pattern) {
        Pattern p = Pattern.compile(pattern);

        if (p.matcher(value).matches()) {
            return true;
        }
        return false;
    }

    /**
     * 初始化message 如果无数据则bundle传入null
     */
    public static Message getMessage(int what, Bundle bundle) {
        Message message = new Message();
        message.what = what;
        if (bundle != null) {
            message.setData(bundle);
        }
        return message;
    }

    public static boolean isNetWorkConnected(Context context) {
        if (context != null) {
            ConnectivityManager manager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (info != null) {
                return info.isAvailable();
            }
        }
        return false;
    }


    /**
     * 得到人民币大写 参数：多小为分（两位有效小数）
     */
    public static String RMBFormat(String money) {
        if ("".equals(money)) {
            return "";
        }
        double d = Double.parseDouble(money);
        money = String.valueOf(d);
        if (money.length() == 1) {
            if ("0".equals(money)) {
                return "零";
            } else if (".".equals(money)) {
                return "零";
            }
        }
        if ("0.".equals(money)) {
            return "零";
        }
        if ("0.0".equals(money)) {
            return "零";
        }
        char[] hunit = {'拾', '佰', '仟'};// 段内位置表示
        char[] vunit = {'万', '亿'}; // 段名表示
        char[] digit = {'零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖'}; // 数字表示
        double d_money = Double.parseDouble(money);
        long midVal = (long) (d_money * 100); // 转化成整形
        String valStr = String.valueOf(midVal); // 转化成字符串
        String head = "";
        String rail = "";
        if (valStr.length() >= 3) {
            head = valStr.substring(0, valStr.length() - 2); // 取整数部分
            rail = valStr.substring(valStr.length() - 2); // 取小数部分
        } else {
            head = "0";
            rail = valStr;
        }

        String prefix = ""; // 整数部分转化的结果
        String suffix = ""; // 小数部分转化的结果

        if (valStr.length() > 17) {
            return "数值过大！";// 解决问题1,超过千亿的问题。
        }

        // 处理小数点后面的数
        if (rail.equals("00")) { // 如果小数部分为0
            suffix = "整";
        } else if (rail.length() == 1) { // 如果小数部分为1位
            suffix = digit[rail.charAt(0) - '0'] + "分"; // 否则把分转化出来
        } else {
            if ("0".equals(String.valueOf(rail.charAt(1)))) {
                suffix = digit[rail.charAt(0) - '0'] + "角";
            } else {
                suffix = digit[rail.charAt(0) - '0'] + "角"
                        + digit[rail.charAt(1) - '0'] + "分"; // 否则把角分转化出来
            }
        }
        // 处理小数点前面的数
        char[] chDig = head.toCharArray(); // 把整数部分转化成字符数组
        char zero = '0'; // 标志'0'表示出现过0
        byte zeroSerNum = 0; // 连续出现0的次数
        for (int i = 0; i < chDig.length; i++) { // 循环处理每个数字
            int idx = (chDig.length - i - 1) % 4; // 取段内位置
            int vidx = (chDig.length - i - 1) / 4; // 取段位置
            if (chDig[i] == '0') { // 如果当前字符是0
                zeroSerNum++; // 连续0次数递增
                if (zero == '0' && idx != 0) { // 标志 ,连续零，仅读一次零，
                    zero = digit[0]; // 解决问题2,当一个零位于第0位时，不输出“零”，仅输出“段名”.
                } else if (idx == 0 && vidx > 0 && zeroSerNum < 4) {
                    prefix += vunit[vidx - 1];
                    zero = '0';
                }
                continue;
            }
            zeroSerNum = 0; // 连续0次数清零
            if (zero != '0') { // 如果标志不为0,则加上,例如万,亿什么的
                prefix += zero;
                zero = '0';
            }

            // 取到该位对应数组第几位。
            int position = chDig[i] - '0';
            if (position == 1 && i == 0 && idx == 1)// 解决问题3 ,即处理10读"拾",而不读"壹拾"

            {
            } else {
                prefix += digit[position]; // 转化该数字表示
            }

            if (idx > 0) // 段内位置表示的值
                prefix += hunit[idx - 1];
            if (idx == 0 && vidx > 0) { // 段名表示的值
                prefix += vunit[vidx - 1]; // 段结束位置应该加上段名如万,亿
            }
        }

        if (prefix.length() > 0)
            prefix += '元'; // 如果整数部分存在,则有圆的字样
        return prefix + suffix; // 返回正确表示
    }

    /**
     * 得到人民币大写 参数：多小为分（两位有效小数）
     */
    public static String RMBFormats(String money) {

        if ("0".equals(money)) {
            return "";
        }

        if ("0.".equals(money)) {
            return "";
        }

        if ("0.0".equals(money)) {
            return "";
        }

        if ("0.00".equals(money)) {
            return "";
        }

        if (".0".equals(money)) {
            return "";
        }

        if (".00".equals(money)) {
            return "";
        }

        if ("".equals(money)) {
            return "";
        }
        if (money.length() == 1) {
            if ("0".equals(money)) {
                return "0";
            } else if (".".equals(money)) {
                return "0.";
            }
        }
        if ("0.".equals(money)) {
            return "0.";
        }
        if ("0.0".equals(money)) {
            return "0.0";
        }
        char[] hunit = {'十', '百', '千'};// 段内位置表示
        char[] vunit = {'万', '亿'}; // 段名表示
        char[] digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'}; // 数字表示
        double d_money = Double.parseDouble(money);
        long midVal = (long) (d_money * 100); // 转化成整形
        String valStr = String.valueOf(midVal); // 转化成字符串
        String head = valStr.substring(0, valStr.length() - 2); // 取整数部分
        String rail = valStr.substring(valStr.length() - 2); // 取小数部分

        String prefix = ""; // 整数部分转化的结果
        String suffix = ""; // 小数部分转化的结果

        if (valStr.length() > 17) {
            return "数值过大！";// 解决问题1,超过千亿的问题。
        }

        // 处理小数点后面的数
        if (rail.equals("00")) { // 如果小数部分为0
            suffix = "";
        } else {
            suffix = digit[rail.charAt(0) - '0'] + "角"
                    + digit[rail.charAt(1) - '0'] + "分"; // 否则把角分转化出来
        }
        // 处理小数点前面的数
        char[] chDig = head.toCharArray(); // 把整数部分转化成字符数组
        char zero = '0'; // 标志'0'表示出现过0
        byte zeroSerNum = 0; // 连续出现0的次数
        for (int i = 0; i < chDig.length; i++) { // 循环处理每个数字
            int idx = (chDig.length - i - 1) % 4; // 取段内位置
            int vidx = (chDig.length - i - 1) / 4; // 取段位置
            if (chDig[i] == '0') { // 如果当前字符是0
                zeroSerNum++; // 连续0次数递增
                if (zero == '0' && idx != 0) { // 标志 ,连续零，仅读一次零，
                    zero = digit[0]; // 解决问题2,当一个零位于第0位时，不输出“零”，仅输出“段名”.
                } else if (idx == 0 && vidx > 0 && zeroSerNum < 4) {
                    prefix += vunit[vidx - 1];
                    zero = '0';
                }
                continue;
            }
            zeroSerNum = 0; // 连续0次数清零
            if (zero != '0') { // 如果标志不为0,则加上,例如万,亿什么的
                prefix += zero;
                zero = '0';
            }

            // 取到该位对应数组第几位。
            int position = chDig[i] - '0';
            if (position == 1 && i == 0 && idx == 1)// 解决问题3 ,即处理10读"拾",而不读"壹拾"

            {
            } else {
                prefix += digit[position]; // 转化该数字表示
            }

            if (idx > 0) // 段内位置表示的值
                prefix += hunit[idx - 1];
            if (idx == 0 && vidx > 0) { // 段名表示的值
                prefix += vunit[vidx - 1]; // 段结束位置应该加上段名如万,亿
            }
        }

        // if (prefix.length() > 0)
        // prefix += '元'; // 如果整数部分存在,则有圆的字样
        return prefix + suffix; // 返回正确表示
    }

    /**
     * @return 得到当前时间
     */
    public static String getTime() {
        SimpleDateFormat matter1;
        Calendar cd;
        Date date1;
        cd = Calendar.getInstance();
        date1 = new Date();
        cd.setTime(date1);
        matter1 = new SimpleDateFormat("yyyy-MM-dd");
        return matter1.format(date1);
    }

    /**
     * @return 得到当前时间
     */
    public static String getWholeTime() {
        SimpleDateFormat matter1;
        Calendar cd;
        Date date1;
        cd = Calendar.getInstance();
        date1 = new Date();
        cd.setTime(date1);
        matter1 = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");
        return matter1.format(date1);
    }

    /**
     * @return 得到当前时间
     */
    public static String getTimeH() {
        SimpleDateFormat matter1;
        Calendar cd;
        Date date1;
        cd = Calendar.getInstance();
        date1 = new Date();
        cd.setTime(date1);
        matter1 = new SimpleDateFormat("HH:MM:SS");
        return matter1.format(date1);
    }

    /**
     * 获取当前日期n天后的日期
     *
     * @param n :返回当天后的第N天
     * @return 返回的日期
     */
    public static String getAfterDate(int n) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, n);
        SimpleDateFormat matter1 = new SimpleDateFormat("yyyy-MM-dd");
        ;
        return matter1.format(c.getTime());
    }


    public String getState(String state) {
        if ("N".equals(state)) {
            return "正常";
        } else if ("C".equals(state)) {
            return "挂失";
        } else {
            return "";
        }
    }

    /**
     * 将list转换成map格式
     *
     * @param list
     * @return
     */
    public static HashMap<String, Object> getMap(List<Map<String, Object>> list) {

        HashMap<String, Object> map1 = new HashMap<String, Object>();
        for (int i = 0; i < list.size(); i++) {
            Iterator keyIteratorOfMap = list.get(i).keySet().iterator();
            while (keyIteratorOfMap.hasNext()) {
                Object key = keyIteratorOfMap.next();
                map1.put("List[" + i + "]." + key,
                        list.get(i).get(key.toString()));
            }
        }
        map1.put("Counter", list.size());
        return map1;
    }

    /**
     * @param str 缴费类型字符
     * @return 缴费类型汉字
     */
    public static String getPaymentTypetos(String str) {
        String s = "";
        if ("SF".equals(str)) {
            s = "保险费";
        }
        if ("WF".equals(str)) {
            s = "水费";
        }
        if ("HF".equals(str)) {
            s = "供暖费";
        }
        if ("TF".equals(str)) {
            s = "电视费";
        }
        if ("GF".equals(str)) {
            s = "燃气费";
        }
        if ("MF".equals(str)) {
            s = "通讯费";
        }
        if ("EF".equals(str)) {
            s = "电费";
        }
        return s;
    }

    /**
     * @param str 缴费类型字符
     * @return 缴费类型汉字
     */
    public static String getPaymentTypetos2(String str) {
        String s = "";
        if ("SF".equals(str)) {
            s = "保险费";
        }
        if ("MW".equals(str)) {
            s = "水费";
        }
        if ("GF".equals(str)) {
            s = "燃气费";
        }
        if ("HF".equals(str)) {
            s = "供暖费";
        }
        if ("TF".equals(str)) {
            s = "电视费";
        }
        if ("MF".equals(str)) {
            s = "通讯费";
        }
        if ("EF".equals(str)) {
            s = "电费";
        }
        if ("ALL".equals(str)) {
            s = "全部";
        }
        return s;
    }

    /**
     * @param str 缴费类型汉字
     * @return 缴费类型字符
     */
    public static String getPaymentTypetoc2(String str) {
        String s = "";
        if ("保险费".equals(str)) {
            s = "SF";
        }
        if ("水费".equals(str)) {
            s = "MW";
        }
        if ("供暖费".equals(str)) {
            s = "HF";
        }
        if ("电视费".equals(str)) {
            s = "TF";
        }
        if ("燃气费".equals(str)) {
            s = "CG";
        }
        if ("通讯费".equals(str)) {
            s = "MF";
        }
        if ("电费".equals(str)) {
            s = "EF";
        }
        if ("全部".equals(str)) {
            s = "ALL";
        }
        return s;
    }

    /**
     * @param str 缴费类型汉字
     * @return 缴费类型字符
     */
    public static String getPaymentTypetoc(String str) {
        String s = "";
        if ("保险费".equals(str)) {
            s = "SF";
        }
        if ("水费".equals(str)) {
            s = "WF";
        }
        if ("供暖费".equals(str)) {
            s = "HF";
        }
        if ("燃气费".equals(str)) {
            s = "GF";
        }
        if ("电视费".equals(str)) {
            s = "TF";
        }
        if ("通讯费".equals(str)) {
            s = "MF";
        }
        if ("电费".equals(str)) {
            s = "EF";
        }
        return s;
    }

    /**
     * @param str 城市编号
     * @return 城市名
     */
    public static String getCitytos(String str) {
        String s = "";
        if ("221001".equals(str)) {
            s = "辽阳市内";
        }
        if ("221002".equals(str)) {
            s = "辽阳市弓长岭地区";
        }
        if ("221003".equals(str)) {
            s = "辽阳市灯塔地区";
        }
        return s;
    }

    /**
     * @param str 城市名
     * @return 城市编号
     */
    public static String getCitytoc(String str) {
        String s = "";
        if ("辽阳市内".equals(str)) {
            s = "221001";
        }
        if ("辽阳市弓长岭地区".equals(str)) {
            s = "221002";
        }
        if ("辽阳市灯塔地区".equals(str)) {
            s = "221003";
        }
        return s;
    }

    /**
     * 交易权限
     *
     * @param str
     * @return
     */
    public static String getJuris(String str) {
        String s = "";
        if ("1".equals(str)) {
            s = "开通";
        } else {
            s = "关闭";
        }
        return s;
    }

    /**
     * 账户类型转换
     *
     * @param str 账户类型名
     * @return
     */
    public static String getAccountTypec(String str) {

        String str1 = null;
        if (str.equals("电子借记卡")) {
            str1 = "PDBC";
        } else if (str.equals("信用卡")) {
            str1 = "PCRC";
        } else if (str.equals("活期存折")) {
            str1 = "PMCS";
        } else if (str.equals("存单")) {
            str1 = "PNDA";
        } else if (str.equals("ic卡")) {
            str1 = "PIC";
        }
        return str1;
    }

    /**
     * 账户类型转换
     *
     * @param str 账户类型
     * @return
     */
    public static String getAccountType(String str) {

        String str1 = null;
        if (str.equals("PDBC")) {
            str1 = "电子借记卡";
        } else if (str.equals("PCRC")) {
            str1 = "信用卡";
        } else if (str.equals("PMCS")) {
            str1 = "活期存折";
        } else if (str.equals("PMCT")) {
            str1 = "存单";
        } else if (str.equals("PPDC")) {
            str1 = "ic卡";
        }
        return str1;
    }

    public static String getStartDate(String str_starttime) {
        // TODO Auto-generated method stub
        String str1 = null;
        if ("周一".equals(str_starttime)) {
            str1 = "1";
        } else if ("周二".equals(str_starttime)) {
            str1 = "2";
        } else if ("周三".equals(str_starttime)) {
            str1 = "3";
        } else if ("周四".equals(str_starttime)) {
            str1 = "4";
        } else if ("周五".equals(str_starttime)) {
            str1 = "5";
        } else if ("周六".equals(str_starttime)) {
            str1 = "6";
        } else if ("周日".equals(str_starttime)) {
            str1 = "7";
        } else {
            str1 = str_starttime;
        }
        return str1;
    }

    public static String getStartDates(String str_starttime) {
        // TODO Auto-generated method stub
        String str1 = null;
        if ("1".equals(str_starttime)) {
            str1 = "周一";
        } else if ("2".equals(str_starttime)) {
            str1 = "周二";
        } else if ("3".equals(str_starttime)) {
            str1 = "周三";
        } else if ("4".equals(str_starttime)) {
            str1 = "周四";
        } else if ("5".equals(str_starttime)) {
            str1 = "周五";
        } else if ("6".equals(str_starttime)) {
            str1 = "周六";
        } else {
            str1 = "周日";
        }
        return str1;
    }

    /**
     * 证件类型
     *
     * @param str
     * @return
     */
    public static String getPapersType(String str) {
        String str1 = null;
        if ("身份证".equals(str)) {
            str1 = "00";
        } else if ("户口簿".equals(str)) {
            str1 = "07";
        } else if ("军人证".equals(str)) {
            str1 = "01";
        } else if ("护照".equals(str)) {
            str1 = "05";
        } else if ("武警证".equals(str)) {
            str1 = "17";
        } else if ("港澳台胞证".equals(str)) {
            str1 = "06";
        } else if ("边境通行证".equals(str)) {
            str1 = "08";
        } else {
            str1 = "22";
        }
        return str1;
    }

    // 判断手是否是字母，不分大小写
    public static boolean isFolat(String str) {
        Pattern p = Pattern.compile("^[A-Za-z]+$");
        Matcher m = p.matcher(str);

        return m.matches();
    }

    // 判断手机格式是否正确
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);

        return m.matches();
    }

    /**
     * 别名修改
     *
     * @param str
     * @return
     */
    public static boolean isAlias(String str) {
        Pattern p = Pattern
                .compile("[A-Za-z0-9\\u4E00-\\u9FBF\\u007B\\u005B\\u0028\\u0029\\u005D]{1,5}");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 别名修改
     *
     * @param str
     * @return
     */
    public static boolean isYLXX(String str) {
        Pattern p = Pattern
                .compile("[A-Za-z0-9\\u4E00-\\u9FBF\\u007B\\u005B\\u0028\\u0029\\u005D]{1,20}");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    // 判断email格式是否正确
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }

    // 判断是否全是数字
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 用户名，字母数字和—
     *
     * @param str
     * @return
     */
    public static boolean isLoginName(String str) {
        Pattern pattern = Pattern.compile("[a-zA-z]{1}[a-zA-Z0-9_]{5,19}");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 密码控件
     *
     * @param str
     * @return
     */
    public static boolean isPassword(String str) {
        Pattern pattern = Pattern.compile("^[!@#^*-_+=a-zA-Z0-9]{6,20}");
        Matcher isN = pattern.matcher(str);
        if (!isN.matches()) {
            return false;
        }
        return true;
    }

    // 判断是否是汉字
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    public static String getExp(String expirefee) {
        // TODO Auto-generated method stub
        String str = "";
        if ("1".equals(expirefee)) {
            str = "是";
        } else {
            str = "否";
        }
        return str;
    }

    public static String getGmqdje(String strs) {
        // TODO Auto-generated method stub
        String str = "";
        if ("5万".equals(strs)) {
            str = "50000";
        } else if ("10万".equals(strs)) {
            str = "100000";
        } else if ("20万以上".equals(strs)) {
            str = "200000";
        } else {
            str = "";
        }
        return str;
    }

    public static String getFxdj(String strs) {
        // TODO Auto-generated method stub
        String str = "";
        if ("低风险".equals(strs)) {
            str = "1";
        } else if ("中低风险".equals(strs)) {
            str = "2";
        } else if ("中风险".equals(strs)) {
            str = "3";
        } else if ("中高风险".equals(strs)) {
            str = "4";
        } else if ("高风险".equals(strs)) {
            str = "5";
        } else {
            str = "6";
        }
        return str;
    }

    public static String getXsqd(String strs) {
        // TODO Auto-generated method stub
        String str = "";
        if ("柜面专属".equals(strs)) {
            str = "0";
        } else if ("电子专属".equals(strs)) {
            str = "1";
        } else {
            str = "";
        }
        return str;
    }

    public static String getCpqx(String strs) {
        // TODO Auto-generated method stub
        String str = "";
        if ("30天以下".equals(strs)) {
            str = "1";
        } else if ("30-90天".equals(strs)) {
            str = "2";
        } else if ("90-180天".equals(strs)) {
            str = "3";
        } else if ("180天以上".equals(strs)) {
            str = "4";
        } else {
            str = "";
        }
        return str;
    }

    public static Date getDate() {
        SimpleDateFormat matter1;
        Calendar cd;
        Date date1;
        cd = Calendar.getInstance();
        date1 = new Date();
        cd.setTime(date1);
        matter1 = new SimpleDateFormat("yyyy-MM-dd");
        return date1;
    }

    public static String getFloat(float f) {
        // TODO Auto-generated method stub
        String ss = "";
        boolean flag = false;
        char s[] = String.valueOf(f).toCharArray();
        int a = 0;
        for (int i = 0; i < s.length; i++) {
            String d = String.valueOf(s[i]);
            if (".".equals(d)) {
                flag = true;
            }
            if (flag) {
                a++;
            }
            ss += d;
            if (a == 4) {
                break;
            }
        }
        if (a == 0) {
            ss += ".000";
        }
        if (a == 1) {
            ss += "000";
        }
        if (a == 2) {
            ss += "00";
        }
        if (a == 3) {
            ss += "0";
        }
        // NumberFormat n = NumberFormat.getInstance();
        // n.setMaximumFractionDigits(2);
        // s = n.format(f);
        return ss;
    }

    /**
     * 小数点后保留2位
     *
     * @param f
     * @return
     */
    public static String getFloatc(float f) {
        // TODO Auto-generated method stub
        String ss = "";
        boolean flag = false;
        char s[] = String.valueOf(f).toCharArray();
        int a = 0;
        for (int i = 0; i < s.length; i++) {
            String d = String.valueOf(s[i]);
            if (".".equals(d)) {
                flag = true;
            }
            if (flag) {
                a++;
            }
            ss += d;
            if (a == 3) {
                break;
            }
        }
        if (a == 0) {
            ss += ".00";
        }
        if (a == 1) {
            ss += "00";
        }
        if (a == 2) {
            ss += "0";
        }
        return ss;
    }

    /**
     * 小数点后保留3位
     *
     * @return
     */
    public static String getFloats(String sstr) {
        // TODO Auto-generated method stub
        String ss = "";
        boolean flag = false;
        char s[] = sstr.toCharArray();
        int a = 0;
        for (int i = 0; i < s.length; i++) {
            String d = String.valueOf(s[i]);
            if (".".equals(d)) {
                flag = true;
            }
            if (flag) {
                a++;
            }
            ss += d;
            if (a == 4) {
                break;
            }
        }
        if (a == 0) {
            ss += ".000";
        }
        if (a == 1) {
            ss += "000";
        }
        if (a == 2) {
            ss += "00";
        }
        if (a == 3) {
            ss += "0";
        }
        return ss;
    }

    /**
     * 小数点后保留3位
     *
     * @return
     */
    public static String getFloatss(String sstr) {
        // TODO Auto-generated method stub
        String ss = "";
        boolean flag = false;
        char s[] = sstr.toCharArray();
        int a = 0;
        for (int i = 0; i < s.length; i++) {
            String d = String.valueOf(s[i]);
            if (".".equals(d)) {
                flag = true;
            }
            if (flag) {
                a++;
            }
            ss += d;
            if (a == 3) {
                break;
            }
        }
        if (a == 0) {
            ss += ".00";
        }
        if (a == 1) {
            ss += "00";
        }
        if (a == 2) {
            ss += "0";
        }
        return ss;
    }


    /**
     * 从Assets文件夹中读取证书
     *
     * @param context  上下文对象
     * @param fileName 文件路径
     */
    public static InputStream getInputStreamFromAssets(Context context, String fileName) {
        InputStream inputStream = null;
        AssetManager am = context.getResources().getAssets();
        try {
            inputStream = am.open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;

    }


    /**
     * 根据正则判断指定字符串
     *
     * @param regex
     * @param string
     * @return
     */
    private static boolean isMatch(String regex, String string) {
        if (string == null || string.trim().equals("")) {
            return false;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher isNum = pattern.matcher(string);
        return isNum.matches();
    }

    public static boolean validateMoney(String string) {
        return isMatch("[1-9]\\d*\\.?\\d* ", string);
    }

    /**
     * 获取顶层Activity实例
     *
     * @param app
     * @return
     */
    public static Activity getTopActivity(Application app) {
        Object obj = null;
        Field f;

        try {
            f = Application.class.getDeclaredField("mLoadedApk");
            f.setAccessible(true);
            obj = f.get(app); // obj => LoadedApk
            f = obj.getClass().getDeclaredField("mActivityThread");
            f.setAccessible(true);
            obj = f.get(obj); // obj => ActivityThread
            f = obj.getClass().getDeclaredField("mActivities");
            f.setAccessible(true);
            Map map = (Map) f.get(obj); //  obj => HashMap=<IBinder, ActivityClientRecord>
            if (map.values().size() == 0) {
                return null;
            }
            obj = map.values().toArray()[map.values().size() - 1]; // obj => ActivityClientRecord
            f = obj.getClass().getDeclaredField("activity");
            f.setAccessible(true);
            obj = f.get(obj); // obj => Activity
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!(obj instanceof Activity)) {
            return null;
        }

        return (Activity) obj;
    }

    /**
     * 获取指定Activity实例
     *
     * @param app
     * @param name
     * @return
     */
    public static Activity getActivityByClsName(Application app, String name) {
        Object obj = null;
        Field f;

        try {
            f = Application.class.getDeclaredField("mLoadedApk");
            f.setAccessible(true);
            obj = f.get(app); // obj => LoadedApk
            f = obj.getClass().getDeclaredField("mActivityThread");
            f.setAccessible(true);
            obj = f.get(obj); // obj => ActivityThread
            f = obj.getClass().getDeclaredField("mActivities");
            f.setAccessible(true);
            Map map = (Map) f.get(obj); //  obj => HashMap=<IBinder, ActivityClientRecord>
            if (map.values().size() == 0) {
                return null;
            }
            Activity activity = null;
            for (Object o : map.values().toArray()) {
                f = o.getClass().getDeclaredField("activity");
                f.setAccessible(true);
                o = f.get(o);
                if (o.getClass().getSimpleName().equals(name)) {
                    obj = o; // obj => Activity
                    activity = (Activity) obj;
                }
            }
            return activity;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取Activity根节点
     *
     * @param context
     * @return
     */
    public static View getRootView(Activity context) {
        ViewGroup vp = (ViewGroup) context.findViewById(android.R.id.content);
        return vp.getChildAt(0);
    }

    /**
     * 获取状态栏高度——方法
     */
    public static int getStatusBarHeight(Activity context) {
        int statusBarHeight1 = 0;
        //获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = context.getResources().getDimensionPixelSize(resourceId);
        }
        Log.e("WangJ", "状态栏-方法1:" + statusBarHeight1);
        return statusBarHeight1;
    }

    public static int getNavigationBarHeight(Activity context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        Log.v("dbw", "Navi height:" + height);
        return height;
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param context
     * @param className 某个界面名称
     */
    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        return list != null && list.size() > 0 &&
                className.equals(list.get(0).topActivity.getClassName());
    }

    public static void makeCall(Context mContext, String num) {
        Intent intent = new Intent(Intent.ACTION_DIAL,
                Uri.parse("tel:" + num));
        mContext.startActivity(intent);
    }

    public static void sendEmailPage(Context mContext, String email,
                                     String failMsg) {
        try {
            Intent data = new Intent(Intent.ACTION_SENDTO);
            data.setData(Uri.parse("mailto:" + email));
            data.addCategory(Intent.CATEGORY_DEFAULT);
            mContext.startActivity(data);
        } catch (Exception e) {
        }
    }


    /**
     * 把金额阿拉伯数字转换为汉字表示，小数点后四舍五入保留两位
     * 还有一种方法可以在转换的过程中不考虑连续0的情况，然后对最终的结果进行一次遍历合并连续的零
     */
    private static final String[] ChineseNum = new String[]{"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};

    public static String NumToChinese(double num) {
        if (num > 99999999999999.99 || num < -99999999999999.99)
            throw new IllegalArgumentException("参数值超出允许范围 (-99999999999999.99 ～ 99999999999999.99)！");
        boolean negative = false;//正负标号
        if (num < 0) {
            negative = true;
            num = num * (-1);
        }
        long temp = Math.round(num * 100);
        int numFen = (int) (temp % 10);//分
        temp = temp / 10;
        int numJiao = (int) (temp % 10);//角
        temp = temp / 10;
        //此时temp只包含整数部分
        int[] parts = new int[20];//将金额整数部分分为在0-9999之间数的各个部分
        int numParts = 0;//记录把原来金额整数部分分割为几个部分
        for (int i = 0; ; i++) {
            if (temp == 0)
                break;
            int part = (int) (temp % 10000);
            parts[i] = part;
            temp = temp / 10000;
            numParts++;
        }
        boolean beforeWanIsZero = true;//标志位，记录万的下一级是否为0
        String chineseStr = "";
        for (int i = 0; i < numParts; i++) {
            String partChinese = partConvert(parts[i]);
            if (i % 2 == 0) {
                if ("".equals(partChinese))
                    beforeWanIsZero = true;
                else
                    beforeWanIsZero = false;
            }
            if (i != 0) {
                if (i % 2 == 0)//亿的部分
                    chineseStr = "亿" + chineseStr;
                else {
                    if ("".equals(partChinese) && !beforeWanIsZero)// 如果“万”对应的 part 为 0，而“万”下面一级不为 0，则不加“万”，而加“零”
                        chineseStr = "零" + chineseStr;
                    else {
                        if (parts[i - 1] < 1000 && parts[i - 1] > 0)//如果万的部分不为0，而万前面的部分小于1000大于0，则万后面应该跟零
                            chineseStr = "零" + chineseStr;
                        chineseStr = "万" + chineseStr;
                    }
                }
            }
            chineseStr = partChinese + chineseStr;
        }
        if ("".equals(chineseStr))//整数部分为0，则表示为零元
            chineseStr = ChineseNum[0];
        else if (negative)//整数部分部位0，但是为负数
            chineseStr = "负" + chineseStr;
        chineseStr = chineseStr + "元";
        if (numFen == 0 && numJiao == 0) {
            chineseStr = chineseStr + "整";
        } else if (numFen == 0) {//0分
            chineseStr = chineseStr + ChineseNum[numJiao] + "角";
        } else {
            if (numJiao == 0)
                chineseStr = chineseStr + "零" + ChineseNum[numFen] + "分";
            else
                chineseStr = chineseStr + ChineseNum[numJiao] + "角" + ChineseNum[numFen] + "分";
        }
        return chineseStr;
    }

    //转换拆分后的每个部分，0-9999之间
    private static String partConvert(int partNum) {
        if (partNum < 0 || partNum > 10000) {
            throw new IllegalArgumentException("参数必须是大于等于0或小于10000的整数");
        }
        String[] units = new String[]{"", "拾", "佰", "仟"};
        int temp = partNum;
        String partResult = new Integer(partNum).toString();
        int partResultLength = partResult.length();
        boolean lastIsZero = true;//记录上一位是否为0
        String chineseStr = "";
        for (int i = 0; i < partResultLength; i++) {
            if (temp == 0)//高位无数字
                break;
            int digit = temp % 10;
            if (digit == 0) {
                if (!lastIsZero)//如果前一个数字不是0则在当前汉字串前加零
                    chineseStr = "零" + chineseStr;
                lastIsZero = true;
            } else {
                chineseStr = ChineseNum[digit] + units[i] + chineseStr;
                lastIsZero = false;
            }
            temp = temp / 10;
        }
        return chineseStr;
    }

    public static boolean validateTransationPwd(String string) {
        return isMatch("[0-9]", string);
    }

    public static String getAppVersionName(Context context) {
        String versionName = null;
        try {
            String pkName = context.getPackageName();

            versionName = "" + context.getPackageManager().getPackageInfo(
                    pkName, 0).versionCode;
            // return pkName + "   " + versionName + "  " + versionCode;
            return versionName;
        } catch (Exception e) {
        }
        return versionName;
    }

    public static String omit4Float(float f) {
        BigDecimal b = new BigDecimal(f);
        float f1 = b.setScale(4, BigDecimal.ROUND_HALF_UP).floatValue();
        DecimalFormat decimalFormat = new DecimalFormat("##0.0000");
        String s = decimalFormat.format(f1);
        return s;
    }

    public static String changeDateFormat(String strDate, String oldPatten,
                                          String newPatten) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(oldPatten, Locale.CHINA);
            SimpleDateFormat sdf = new SimpleDateFormat(newPatten, Locale.CHINA);
            Date date = formatter.parse(strDate);
            return sdf.format(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }

    public static boolean isInternetAvaiable(Context context) {
        ConnectivityManager mannager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = mannager.getActiveNetworkInfo();
        if (info == null || !info.isAvailable() || !info.isConnected()) {
            return false;
        }

        return true;
    }

    @SuppressWarnings("deprecation")
    public static int getAndroidSDKVersion() {
        int version = 0;
        try {
            version = Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 保留两位小数，四舍五入的一个老土的方法
     *
     * @param d
     * @return
     */
    public static double formatDouble1(double d) {
        return (double) Math.round(d * 100) / 100;
    }

    /**
     * 获取AndroidManifest中Application的meta data
     *
     * @param context
     * @param key
     * @return
     */
    public static String getApplicationConfigMetaData(Application context, String key) {
        ApplicationInfo info;
        try {
            info = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            return info.metaData.getString(key);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean isGooglePlay() {
        return false;
    }

    public static boolean isBackground() {
        return background;
    }


    public static void setBackground(boolean back) {
        background = back;
    }


    public static boolean isXposedExists(Throwable thr) {
        StackTraceElement[] stackTraces = thr.getStackTrace();
        for (StackTraceElement stackTrace : stackTraces) {
            final String clazzName = stackTrace.getClassName();
            if (clazzName != null && clazzName.contains("de.robv.android.xposed.XposedBridge")) {
                return true;
            }
        }
        return false;
    }


    public static String getExceptionCauseString(final Throwable ex) {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final PrintStream ps = new PrintStream(bos);

        try {
            // print directly
            Throwable t = ex;
            while (t.getCause() != null) {
                t = t.getCause();
            }
            t.printStackTrace(ps);
            return toVisualString(bos.toString());
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static class ScreenState {
        public interface IOnScreenOff {
            void onScreenOff();
        }

        public ScreenState(final Context context, final IOnScreenOff onScreenOffInterface) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_OFF);

            context.registerReceiver(new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent in) {
                    String action = in == null ? "" : in.getAction();

                    if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                        if (onScreenOffInterface != null) {
                            onScreenOffInterface.onScreenOff();
                        }
                    }
                    context.unregisterReceiver(this);
                }
            }, filter);
        }
    }


    @Deprecated
    public static boolean checkRomSpaceEnough(long limitSize) {
        long allSize;
        long availableSize = 0;
        try {
            File data = Environment.getDataDirectory();
            StatFs sf = new StatFs(data.getPath());
            availableSize = (long) sf.getAvailableBlocks() * (long) sf.getBlockSize();
            allSize = (long) sf.getBlockCount() * (long) sf.getBlockSize();
        } catch (Exception e) {
            allSize = 0;
        }

        if (allSize != 0 && availableSize > limitSize) {
            return true;
        }
        return false;
    }


    private static String toVisualString(String src) {
        boolean cutFlg = false;

        if (null == src) {
            return null;
        }

        char[] chr = src.toCharArray();
        if (null == chr) {
            return null;
        }

        int i = 0;
        for (; i < chr.length; i++) {
            if (chr[i] > 127) {
                chr[i] = 0;
                cutFlg = true;
                break;
            }
        }

        if (cutFlg) {
            return new String(chr, 0, i);
        } else {
            return src;
        }
    }

    /**
     * 从相册获取图片
     *
     * @param context     activity
     * @param requestCode requestCode
     */
    public static void pickPhotoFromLocal(Activity context, int requestCode) {
        Intent intent = null;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        } else {
            intent = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * 拍照
     *
     * @param context     activity
     * @param file        获取照片后的存放文件
     * @param requestCode requestCode
     */
    public static void doTakePhoto(Activity context, File file, int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * 实现文本复制功能 add by wangqianzhou
     *
     * @param context 上下文
     * @param content 复制内容
     */
    public static void copyTxt(Context context, String content) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData textCd = ClipData.newPlainText("content", content);
        cmb.setPrimaryClip(textCd);
    }

    /**
     * 实现文本粘贴功能 add by wangqianzhou
     *
     * @param context 上下文
     * @return 黏贴的字符串
     */
    public static String pasteTxt(Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        if (!cmb.hasPrimaryClip()
                || !cmb.getPrimaryClipDescription().hasMimeType(
                ClipDescription.MIMETYPE_TEXT_PLAIN)) {
            return "";
        }
        ClipData cdText = cmb.getPrimaryClip();
        ClipData.Item item = cdText.getItemAt(0);
        if (item.getText() == null) {
            return "";
        }
        return item.getText().toString();
    }


}
