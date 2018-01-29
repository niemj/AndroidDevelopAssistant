package com.example.jun.myapplication.util.crypto;

import com.example.jun.myapplication.util.FormatTools;
import com.example.jun.myapplication.util.StringUtil;

import java.io.InputStream;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;


public class Base64Util {

    /**
     * 自定义 base64解密
     *
     * @param str 待解密串
     * @return 解密后串
     */
    public static String decipheringB64(String str) {
        if (str == null || "".equals(str)) {
            return "";
        }
        String oldUser = StringUtil.reverse(str);
        String result = FormatTools.getInstance().Bytes2String(
                Base64.decode(oldUser));
        return result;
    }



    /**
     * 自定义base64加密
     *
     * @param str 待加密的字串
     * @return 加密后字串
     */
    public static String encryptB64(String str) {
        if (str == null || "".equals(str)) {
            return "";
        }
        // base64加密
        String nativeUsername = Base64.encode(FormatTools.getInstance()
                .String2Bytes(str));
        // 字符串反转
        nativeUsername = StringUtil.reverse(nativeUsername);
        return nativeUsername;
    }

	/*
     * 将流通base64加密
	 */

    public static String encryStringB64(InputStream is) {

        // 讲流转换成byte
        byte[] byte_photo = FormatTools.getInstance().InputStream2Bytes(is);

        /**
         * CRLF 这个参数看起来比较眼熟，它就是Win风格的换行符，意思就是使用CR LF这一对作为一行的结尾而不是Unix风格的LF
         *
         * DEFAULT 这个参数是默认，使用默认的方法来加密
         *
         * NO_PADDING 这个参数是略去加密字符串最后的”=”
         *
         * NO_WRAP 这个参数意思是略去所有的换行符（设置后CRLF就没用了）
         *
         * URL_SAFE 这个参数意思是加密时不使用对URL和文件名有特殊意义的字符来作为加密字符，具体就是以-和_取代+和/
         */
        String photo_str = Base64.encode(byte_photo);
        return photo_str;
    }

    public static String encryStrB64(byte[] byte_photo) {

        // 讲流转换成byte
//		byte[] byte_photo = FormatTools.getInstance().InputStream2Bytes(is);

        /**
         * CRLF 这个参数看起来比较眼熟，它就是Win风格的换行符，意思就是使用CR LF这一对作为一行的结尾而不是Unix风格的LF
         *
         * DEFAULT 这个参数是默认，使用默认的方法来加密
         *
         * NO_PADDING 这个参数是略去加密字符串最后的”=”
         *
         * NO_WRAP 这个参数意思是略去所有的换行符（设置后CRLF就没用了）
         *
         * URL_SAFE 这个参数意思是加密时不使用对URL和文件名有特殊意义的字符来作为加密字符，具体就是以-和_取代+和/
         */
        String photo_str = Base64.encode(byte_photo);
        return photo_str;
    }

    public static String encryptPassword(String pwd) {
        final String p = "ZYXF1234";
        FormatTools tools = FormatTools.getInstance();
        byte[] des = encrypt(tools.String2Bytes(pwd), p);
        return Base64.encode(des);
    }

    /**
     * DES加密
     *
     * @param datasource byte[]
     * @param password   String
     * @return byte[]
     */
    public static byte[] encrypt(byte[] datasource, String password) {
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(password.getBytes());
//创建一个密匙工厂，然后用它把DESKeySpec转换成
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
//Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance("DES");
//用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
//现在，获取数据并加密
//正式执行加密操作
            return cipher.doFinal(datasource);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * DES解密
     *
     * @param src      byte[]
     * @param password String
     * @return byte[]
     * @throws Exception
     */
    public static byte[] decrypt(byte[] src, String password) throws Exception {
// DES算法要求有一个可信任的随机数源
        SecureRandom random = new SecureRandom();
// 创建一个DESKeySpec对象
        DESKeySpec desKey = new DESKeySpec(password.getBytes());
// 创建一个密匙工厂
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
// 将DESKeySpec对象转换成SecretKey对象
        SecretKey securekey = keyFactory.generateSecret(desKey);
// Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance("DES");
// 用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, random);
// 真正开始解密操作
        return cipher.doFinal(src);
    }


}
