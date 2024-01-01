package com.cbq.yatori.core.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
    public static String getMD5Str(String str) {
        byte[] digest = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            digest = md5.digest(str.getBytes("utf-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//16是表示转换为16进制数
        String md5Str = new BigInteger(1, digest).toString(16);
        return md5Str;
    }
    public static String getMD5Str(String str,int bit){
        String md5Str = getMD5Str(str);
        if(md5Str.length()<bit){
            for(int i=md5Str.length()+1; i<=bit;++i){
                md5Str="0"+md5Str;
            }
        }
        return md5Str;
    }
}
