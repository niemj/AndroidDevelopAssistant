/**
 *
 */
package com.example.jun.myapplication.util;


import android.text.TextUtils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串处理、校验工具类
 */
public class StringUtil {

    public static String formatMoneyByFixed(Object money) {
        return formatMoneyByFixed(money, 2);
    }

    public static String formatMoneyByFixed(Object money, int fixed) {

        StringBuilder sb = new StringBuilder("");
        for (int i = 1; i < fixed + 2; i++) {
            sb.append("0");
        }

        if (money == null) {
            return "0." + sb.toString().substring(0, sb.length() - 1);
        }

        String strMoney = ObjCastUtil.castString(money);
        if (!strMoney.contains(".")) {
            return moneyFormatDot(strMoney + "." + sb.toString().substring(0, sb.length() - 1));
        } else {
            String[] strings = strMoney.split("\\.");
            if (strings.length > 1) {
                String right = strings[1];
                if (right.length() <= fixed) {
                    StringBuilder s = new StringBuilder();
                    for (int i = 1; i < fixed - right.length() + 1; i++) {
                        s.append("0");
                    }
                    return moneyFormatDot(strings[0] + "." + strings[1] + s.toString());
                }
            }
        }

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(fixed + 1);
        df.setGroupingSize(0);
        df.applyPattern("0." + sb.toString());
        df.setRoundingMode(RoundingMode.FLOOR);
        String result = moneyFormatDot(df.format(ObjCastUtil.castFloat(money)));
        return result.substring(0, result.length() - 1);
    }

    public static String formatMoneyByFixedNoDot(Object money) {
        return formatMoneyByFixed(money).replace(",", "").replace(".00", "");
    }

    public static String formatMoneyByTS(Object money, boolean bRepay) {
        return String.format(Locale.CHINA, bRepay ? "%s元" : "+%s元", formatMoneyByFixed(money));
    }

    public static String formatMoneyString(Object money) {
        return String.format(Locale.CHINA, "%s元", formatMoneyByFixed(money));
    }

    public static String formatRateString(float f, int fixed) {
        return String.format(Locale.CHINA, f < 0 ? "-%." + fixed + "f" : "%." + fixed + "f", Math.abs(f * 100)) + "%";
    }

    public static String formatRateString(float f) {
        return formatRateString(f, 2);
    }

    public static String getIntResult(String temp) {
        String result = null;
        String temp1 = null;
        if (temp.contains(".")) {
            String[] strings = temp.split("\\.");
            temp = strings[0];
            if (strings.length > 1) {
                temp1 = strings[1];
            }
        }
        if (temp.equals("")) {
            result = "";
        } else if (temp.contains("0") && ObjCastUtil.castInteger(temp) == 0) {
            result = "0";
        } else if (Long.valueOf(temp) > 0 && Long.valueOf(temp) < 1000) {
            result = String.valueOf(Long.valueOf(temp));
        } else if (Long.valueOf(temp) > 999 && Long.valueOf(temp) < 1000000) {
            result = String.valueOf(Long.valueOf(temp)).substring(0,
                    String.valueOf(Long.valueOf(temp)).length() - 3)
                    + ","
                    + String.valueOf(Long.valueOf(temp)).substring(
                    String.valueOf(Long.valueOf(temp)).length() - 3,
                    String.valueOf(Long.valueOf(temp)).length());
        } else if (Long.valueOf(temp) > 999999
                && Long.valueOf(temp) < 100000000) {
            result = String.valueOf(Long.valueOf(temp)).substring(0,
                    String.valueOf(Long.valueOf(temp)).length() - 6)
                    + ","
                    + String.valueOf(Long.valueOf(temp)).substring(
                    String.valueOf(Long.valueOf(temp)).length() - 6,
                    String.valueOf(Long.valueOf(temp)).length() - 3)
                    + ","
                    + String.valueOf(Long.valueOf(temp)).substring(
                    String.valueOf(Long.valueOf(temp)).length() - 3,
                    String.valueOf(Long.valueOf(temp)).length());
        } else if (Long.valueOf(temp) > 99999999) {
            result = "100,000,000";
        }
        return result;
    }

    private static String moneyFormatDot(String temp) {
        boolean isZeroDown = ObjCastUtil.castFloat(temp) < 0;
        String[] strings = temp.replace("-", "").replace(".", "!").split("!");
        if (strings.length < 2) {
            return temp.equals("") ? "0.00" : temp;
        } else {
            return (isZeroDown ? "-" : "") + getIntResult(strings[0]) + "." + strings[1];
        }
    }

    public static String formatMoneySymbol(Object money) {
        return String.format(Locale.CHINA, "¥%s", formatMoneyByFixed(money));
    }

    public static String formatMoneyAndTitleString(String title, Object money) {
        return String.format(Locale.CHINA, title + "%s元", formatMoneyByFixed(money));
    }

    /**
     * 字符串反转
     */
    public static String reverse(String str) {
        return new StringBuffer(str).reverse().toString();
    }


    /**
     * 格式化String
     * 例如:12345678901格式后123****8901
     *
     * @return
     */
    public static String formatString(String data) {
        if (data != null && !"".equals(data) && data.length() > 7) {
            return data.substring(0, 3) + "****" + data.substring(data.length() - 4);
        } else if (data == null) {
            return "0000****0000";
        } else {
            return data;
        }
    }

    /**
     * 返回银行卡号4位尾数
     *
     * @param data 银行卡号
     * @return 尾数
     */
    public static String parseCardEndNumber(String data) {
        if (TextUtils.isEmpty(data) || data.length() < 4) {
            return "0000";
        }
        return data.substring(data.length() - 4);
    }

    public static boolean isUrl(String str) {
        String ex = "((http|ftp|https)://)(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})?[^\\s]+";
        Pattern pattern = Pattern.compile(ex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static boolean isColor(String color) {
        if (TextUtils.isEmpty(color)) {
            return false;
        }
        String ex = "^#([0-9a-fA-F]{6}|[0-9a-fA-F]{3})$";
        Pattern pattern = Pattern.compile(ex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(color);
        return matcher.matches();
    }

    /**
     * 判断是否为纯汉字
     *
     * @param str 字符串
     * @return 是否纯汉字
     */
    public static boolean isAllChinese(String str) {
        String ex = "[\\u4e00-\\u9fa5]+";
        Pattern pattern = Pattern.compile(ex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * 格式化账单编号
     * 例如12345678901234567格式化后1234 5678 9012 34567
     */
    public static String formatBillNumber(String data) {
        StringBuilder sb = new StringBuilder();
        if (data != null && !"".equals(data)) {
            final int dataLength = data.length();
            int size = dataLength / 4;
            for (int i = 0; i < size - 1; i++) {
                sb.append(data.substring(i * 4, i * 4 + 4));
                sb.append(" ");
            }
            sb.append(data.substring((size - 1) * 4));
        }
        return sb.toString();
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
                if (i >= left && i <= right) {
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
     * 对金额的格式调整到分
     *
     * @param money
     * @return
     */
    public static String moneyFormat(String money) {//23->23.00
        StringBuffer sb = new StringBuffer();
        if (money == null) {
            return "0.00";
        }
        int index = money.indexOf(".");
        if (index == -1) {
            return money + ".00";
        } else {
            String s0 = money.substring(0, index);//整数部分
            String s1 = money.substring(index + 1);//小数部分
            if (s1.length() == 1) {//小数点后一位
                s1 = s1 + "0";
            } else if (s1.length() > 2) {//如果超过3位小数，截取2位就可以了
                s1 = s1.substring(0, 2);
            }
            sb.append(s0);
            sb.append(".");
            sb.append(s1);
        }
        return sb.toString();
    }

    public static String parseMoney(Integer number) {
        DecimalFormat df = new DecimalFormat("#,##,###,###");
        return df.format(number);
    }

    public static String parseMoneyDouble(Double number) {
        DecimalFormat df = new DecimalFormat("#,##,###,###");
        return df.format(number);
    }

    public static String insertStringInParticularPosition(String src, String dec, int position) {

        StringBuffer stringBuffer = new StringBuffer(src);
        return stringBuffer.insert(position, dec).toString();

    }

    public static String addComma(String str) {
        boolean neg = false;
        if (str.startsWith("-")) {  //处理负数
            str = str.substring(1);
            neg = true;
        }
        String tail = null;
        if (str.indexOf('.') != -1) { //处理小数点
            tail = str.substring(str.indexOf('.'));
            str = str.substring(0, str.indexOf('.'));
        }
        StringBuilder sb = new StringBuilder(str);
        sb.reverse();
        for (int i = 3; i < sb.length(); i += 4) {
            sb.insert(i, ',');
        }
        sb.reverse();
        if (neg) {
            sb.insert(0, '-');
        }
        if (tail != null) {
            sb.append(tail);
        }
        return sb.toString();
    }

    public static boolean getIDDate(String date) {

        String a[] = date.split("-");
        if (a.length != 3)
            return false;
        if (a[0].length() != 4)
            return false;
        if (a[1].length() != 2)
            return false;
        if (a[2].length() != 2)
            return false;
        return true;
    }

    public static boolean isMatch(String regex, String target) {
        if (target == null || target.trim().equals("")) {
            return false;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher isNum = pattern.matcher(target);
        return isNum.matches();
    }

    public static String getHostFromUrl(String url) {
        String reg = "(?<=//|)((\\w)+\\.)+\\w+";
        if (url == null || url.trim().equals("")) {
            return "";
        }
        String host = "";
        Pattern p = Pattern.compile(reg);
        Matcher matcher = p.matcher(url);
        if (matcher.find()) {
            host = matcher.group();
        }
        return host;
    }
}
