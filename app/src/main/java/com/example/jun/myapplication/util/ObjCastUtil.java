package com.example.jun.myapplication.util;

/**
 * 基本类型转换类
 */
public class ObjCastUtil {

    public static int toInt(byte[] bRefArr) {
        int iOutcome = 0;
        for (int i = 0; i < bRefArr.length; i++) {
            iOutcome += (bRefArr[i] & 0xFF) << (8 * i);
        }
        return iOutcome;
    }

    public static String castString(Object o) {
        if (o == null) {
            return "";
        }

        return String.valueOf(o);
    }

    public static String castNumberString(Object o) {
        if (castString(o).equals("")) {
            return "0";
        } else
            return castString(o);

    }

    public static String castNumberDoubleString(Object o) {
        if (castString(o).contains(".")) {
            return castNumberString(o);
        } else
            return castNumberString(o) + ".00";
    }

    public static String castNumberPoint(Object o) {
        String[] number = castNumberString(o).split("\\.");
        if (number.length > 1) {
            if (number[1].equals("0"))
                return number[0];
            else if (number[1].equals("00"))
                return number[0];
            else
                return castNumberString(o);
        } else {
            return castNumberString(o);
        }


    }

    public static boolean castBoolean(Object o) {

        if (o == null) {
            return false;
        } else {
            return String.valueOf(o).equals("1")
                    || String.valueOf(o).equals("true");
        }
    }

    public static float castFloat(Object o) {
        if (o == null) {
            return 0;
        }
        String value = String.valueOf(o);
        try {
            return Float.valueOf(value);
        } catch (NumberFormatException e) {
            // TODO: handle exception
            return 0;
        }
    }

    public static int castInteger(Object o) {
        int i = 0;
        if (o == null) {
            return 0;
        }
        String value = String.valueOf(o);
        if (o instanceof Integer) {
            i = Integer.valueOf(value);
        } else if (o instanceof Float) {
            i = Float.valueOf(value).intValue();
        } else if (o instanceof Double) {
            i = Double.valueOf(value).intValue();
        } else if (o instanceof String) {
            if (value.contains(".")) {
                String[] strings = value.split("\\.");
                i = Integer.valueOf(strings[0]);
            } else if (value.equals("")) {
                i = 0;
            } else {
                try {
                    i = Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    // TODO: handle exception
                    return 0;
                }
            }
        }
        return i;
    }

    public static double castDouble(Object o) {
        double i = 0;
        if (o == null) {
            return 0;
        }
        String value = String.valueOf(o);
        if (o instanceof Double) {
            i = Double.valueOf(value);
        } else if (o instanceof Float) {
            i = Float.valueOf(value).doubleValue();
        } else if (o instanceof Integer) {
            i = Integer.valueOf(value).doubleValue();
        } else if (o instanceof String) {
            try {
                i = Double.valueOf(value);
            } catch (NumberFormatException e) {
                // TODO: handle exception
                return 0;
            }
        }
        return i;
    }

    public static long castLong(Object o) {
        long i;
        if (o == null) {
            return 0;
        }
        String value = String.valueOf(o);
        try {
            i = Long.parseLong(value);
        } catch (NumberFormatException e) {
            // TODO: handle exception
            return 0;
        }
        return i;
    }
}
