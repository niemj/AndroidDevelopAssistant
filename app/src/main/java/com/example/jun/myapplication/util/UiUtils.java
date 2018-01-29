package com.example.jun.myapplication.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * 提供ui操作的帮助类
 *
 * @author jimdear
 */
public class UiUtils {

    private static boolean bLogoutDialogShowing = false;
    private static boolean bErrorShowing = false;

    /**
     * 正则表达式：验证手机号
     */
    private static final String REGEX_MOBILE = "^1(3[0-9]|4[57]|5[0-35-9]|7[01678]|8[0-9])\\d{8}$";

    /**
     * 正则表达式：验证手机号
     */
    private static final String ID_MOBILE = "^\\d{4}-((0?[1-9])|(1[0-2]))-((0?[1-9])|([1-2][0-9])" +
            "|(3[01]))$";

    private static WindowManager mWindowManager;

    private static View mPopupErrorMessage;

    // 返回context对象
    public static Context getContext() {
        return getContext();
    }


    /**
     * 是否有可用网络
     */
    public static boolean hasNetwork() {
        boolean result = false;
        ConnectivityManager cm = (ConnectivityManager) UiUtils.getContext().getSystemService
                (Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isConnected()) {
            result = true;
        }
        return result;
    }

    public static String getValueByName(String url, String name) {
        String result = "";
        int index = url.indexOf("?");
        String temp = url.substring(index + 1);
        String[] keyValue = temp.split("&");
        for (String str : keyValue) {
            if (str.contains(name)) {
                result = str.replace(name + "=", "");
                break;
            }
        }
        return result;
    }


    /**
     * 判断请求是否成功
     *
     * @param map
     * @return
     */
    public static boolean JsonIsOk(Map<String, Object> map) {
        boolean pass = false;
        if (map == null || map.size() == 0)
            return false;
        if (map.get("_RejCode") != null && map.get("_RejCode").equals("000000") || ObjCastUtil
                .castString(map.get("code")).equals("200")) {
            pass = true;
        }
        return pass;
    }

    public static boolean canApplyHistoryOpen(String state) {
        if (state.equals("0")) {
            return true;
        } else if (state.equals("1")) {
            return true;
        } else if (state.equals("6")) {
            return true;
        } else if (state.equals("B")) {
            return true;
        } else if (state.equals("C")) {
            return true;
        } else if (state.equals("G")) {
            return true;
        } else if (state.equals("D")) {
            return true;
        } else
            return false;
    }



    /**
     * dip转px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转dip
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (spValue * scale + 0.5f);
    }


    public static String getImgUrl(Map<String, Object> dataMap) {
        String string = "";
        Object obj = dataMap.get("PrdLogoImgUrlList");
        if (obj != null && obj instanceof List) {
            List<Map<String, String>> list = (List<Map<String, String>>) obj;
            if (list.size() > 0) {
                string = list.get(0).get("PrdImgUrl");
            }
        }
        return string;
    }


    public static void hideSoftInput(Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context
                .INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(context.getWindow().getDecorView().getWindowToken(), 0);
    }

    public static void showSoftInput(EditText editText) {
        InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText, 0);

    }

    /**
     * 校验手机号
     *
     * @param phone 目标输入框
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isMobile(EditText phone) {
        return phone.getText().toString().matches(REGEX_MOBILE);
    }

    /**
     * 校验身份签发日期
     *
     * @param phone 目标输入框
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isIDData(String phone) {
        return phone.matches(ID_MOBILE);
    }

    public static String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间

        return sdf.format(curDate);
    }

    public static String getTime(String p) {
        SimpleDateFormat sdf = new SimpleDateFormat(p, Locale.CHINA);
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        return sdf.format(curDate);
    }

    public static String getTime(String time, String p) {
        SimpleDateFormat sdf = new SimpleDateFormat(p, Locale.CHINA);
        Date curDate = new Date(ObjCastUtil.castLong(time));//获取当前时间

        return sdf.format(curDate);
    }


    public static float[] parseCurrentChartData(List<Map<String, String>> list, boolean
            bNoCurrencyFund) {
        if (list == null || list.size() < 1) {
            float[] f = new float[1];
            f[0] = 0f;
            return f;
        }
        float[] f = new float[list.size()];
        int i = 0;
        for (Map<String, String> map : list) {
            Object o = map.get(!bNoCurrencyFund ? "nav" : "yearlyRoe");
            f[i] = ObjCastUtil.castFloat(o);
            i++;
        }
        return f;
    }

    public static String[] parseCurrentChartTime(List<Map<String, String>> list) {
        String[] strings = new String[2];
        if (list == null || list.size() < 2) {
            strings[0] = "1900/1/1";
            strings[1] = "1900/1/1";
        } else {
            strings[0] = datePatten(list.get(0).get("date"));
            strings[1] = datePatten(list.get(list.size() - 1).get("date"));
        }
        return strings;
    }

    private static String datePatten(Object s) {
        return Utils.changeDateFormat(ObjCastUtil.castString(s), "yyyy-MM-dd", "yyyy/MM/dd");
    }


    /**
     * 组装银行卡名
     *
     * @param map
     * @return
     */
    public static String appendBankName(Map<String, Object> map) {
        final String code = ObjCastUtil.castString(map.get("walletId"));
        if ("".equals(code)) {
            // 银行卡支付
            return String.format(Locale.CHINA, "%s (%s) 支付", ObjCastUtil.castString(map.get
                    ("name")), ObjCastUtil.castString(map.get("paymentNo")).replace("*", ""));
        } else {
            // 中邮宝
            return "中邮宝 支付";
        }
    }

    /**
     * 组装银行卡限额描述
     *
     * @param map
     * @return
     */
    public static String appendBankDesc(Map<String, Object> map) {
        final int perTime = (int) ObjCastUtil.castFloat(map.get("maxRapidPayAmountPerTxn")) / 10000;
        final int perDay = (int) ObjCastUtil.castFloat(map.get("maxRapidPayAmountPerDay")) / 10000;
        return String.format(Locale.CHINA, "每笔%d万元，每日%d万元", perTime, perDay);
    }

    /**
     * 组装银行卡尾号
     *
     * @param no
     * @return
     */
    public static String appendBankLastNo(String no) {
        if (!"".equals(no) && no.length() > 4) {
            return String.format(Locale.CHINA, "尾号%s", no.substring(no.length() - 4, no.length()));
        } else {
            return "";
        }
    }


    public static void backgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        if (bgAlpha == 1.0) {
            //不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        } else {
            //此行代码主要是解决在华为手机上半透明效果无效的bug
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
        activity.getWindow().setAttributes(lp);
    }

    /**
     * 中间数字变星号
     *
     * @param pNumber
     * @return
     */
    public static String setNumber(String pNumber, int left, int right) {
        String a = "";
        if (!TextUtils.isEmpty(pNumber) && pNumber.length() > 6) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < pNumber.length(); i++) {
                char c = pNumber.charAt(i);
                if (i >= left && i <= pNumber.length() - right) {
                    sb.append('*');
                } else {
                    sb.append(c);
                }
            }
            a = sb.toString();
        }
        return a;
    }

    /**
     * 中间数字变星号,每隔4个空格一个
     *
     * @param pNumber
     * @return
     */
    public static String setNumberSpace(String pNumber, int left, int right) {
        String a = "";
        if (!TextUtils.isEmpty(pNumber) && pNumber.length() > 6) {
            String sb = pNumber.substring(0, left) + "********" + pNumber.substring(pNumber.length() - right, pNumber.length());
            a = spaceAt4(sb.toString());
        }
        return a;
    }

    public static String spaceAt4(String str) {

        StringBuilder sb = new StringBuilder();
        int length = str.length();
        for (int i = 0; i < length; i += 4) {
            if (length - i <= 8) {      //防止ArrayIndexOutOfBoundsException
                sb.append(str.substring(i, i + 4)).append("  ");
                sb.append(str.substring(i + 4));
                break;
            }
            sb.append(str.substring(i, i + 4)).append(" ");
        }

        return sb.toString();
    }

    public static HashMap<String, String> getUrlParams(String url) {

        if (url == null || url.length() == 0 || !url.contains("?")) {
            return null;
        }

        url = url.substring(url.indexOf("?") + 1);
        String[] paramList = url.split("&");

        HashMap<String, String> params = new HashMap<String, String>();
        for (String str : paramList) {

            String[] tempL = str.split("=");
            if (tempL.length == 2) {
                params.put(tempL[0], tempL[1]);
            }
        }

        return params;
    }

    public static String TimeCompare(String str1, String str2)//A--"=";B---"<;C--->
    {
        String str = "";
        java.text.DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Calendar c1 = java.util.Calendar.getInstance();
        java.util.Calendar c2 = java.util.Calendar.getInstance();
        try {
            c1.setTime(df.parse(str1));
            c2.setTime(df.parse(str2));
        } catch (java.text.ParseException e) {
            System.err.println("格式不正确");
        }
        int result = c1.compareTo(c2);
        if (result == 0)
            str = "A";
        else if (result < 0)
            str = "B";
        else
            str = "C";
        return str;
    }

    /***
     * 获取文件个数
     ***/
    public static int getAllFiles(File root) {
        int i = 0;
        File files[] = root.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    getAllFiles(f);
                } else {
                    i = i + 1;
                    System.out.println(f);
                }
            }
        }
        return i;
    }

/*
    校验过程：
    1、从卡号最后一位数字开始，逆向将奇数位(1、3、5等等)相加。
    2、从卡号最后一位数字开始，逆向将偶数位数字，先乘以2（如果乘积为两位数，将个位十位数字相加，即将其减去9），再求和。
    3、将奇数位总和加上偶数位总和，结果应该可以被10整除。
    */

    /**
     * 校验银行卡卡号
     */
    public static boolean checkBankCard(String bankCard) {
        if (bankCard.length() < 15 || bankCard.length() > 19) {
            return false;
        }
        char bit = getBankCardCheckCode(bankCard.substring(0, bankCard.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return bankCard.charAt(bankCard.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeBankCard
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeBankCard) {
        if (nonCheckCodeBankCard == null || nonCheckCodeBankCard.trim().length() == 0
                || !nonCheckCodeBankCard.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeBankCard.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    /**
     * @author jimdear
     * @time 2017/10/10  11:14
     * @Description context的软引用，解决Task 未finish 时界面改变导致crash的问题
     */
    public static boolean isExist_Living(WeakReference<Context> weakReference) {
        if (weakReference != null) {
            Context context = weakReference.get();
            if (context == null) {
                return false;
            }
            if (context instanceof Activity) {
                Activity activity = (Activity) context;
                if (activity.isFinishing())
                    return false;
            }
            return true;
        }
        return false;
    }


}
