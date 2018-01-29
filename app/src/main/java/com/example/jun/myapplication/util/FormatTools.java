package com.example.jun.myapplication.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * <p>
 * Title: FormatTools.java
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * CreateTime：16/8/12
 * </p>
 *
 * @author 作者名
 * @version common v1.0
 */
public class FormatTools {
    final int BUFFER_SIZE = 4096;

    private static FormatTools tools;

    public static FormatTools getInstance() {
        if (tools == null) {
            tools = new FormatTools();
            return tools;
        }
        return tools;
    }

    /**
     * @param b byte数组
     * @return String
     * @throws Exception
     * @brief 将byte数组转换成String
     */
    public String Bytes2String(byte[] b) {
        InputStream is;
        try {
            is = Bytes2InputStream(b);
            return InputStream2String(is);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * @param b byte数组
     * @return InputStream
     * @brief 将byte[]转换成InputStream
     */
    public InputStream Bytes2InputStream(byte[] b) {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        return bais;
    }

    /**
     * @param in 数据流
     * @return String
     * @throws Exception
     * @brief 将InputStream转换成String
     */
    public String InputStream2String(InputStream in) throws Exception {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count = -1;
        while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
            outStream.write(data, 0, count);

        data = null;
        return new String(outStream.toByteArray(), "UTF-8");
    }

    /**
     * @param in 字符串
     * @return byte[]
     * @throws Exception
     * @brief 将String转换成byte[]
     */
    public byte[] String2Bytes(String in) {
        try {
            return in.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * @param is 数据流
     * @return byte[]
     * @brief 将InputStream转换成byte[]
     */
    public byte[] InputStream2Bytes(InputStream is) {
        String str = "";
        byte[] readByte = new byte[1024];
        int readCount = -1;
        try {
            while ((readCount = is.read(readByte, 0, 1024)) != -1) {
                str += new String(readByte).trim();
            }
            return str.getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
