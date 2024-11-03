package com.cbq.yatori.core.action.bilibili;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author ChangBaiQi
 * @version 1.0
 * @description TODO B站登录逆向接口
 * @date 2024/11/3 10:56
 */
public class LoginAction {

    /**
     * 用于获取参数签名
     * @param params 对应需要签名的参数
     * @return
     */
    public static String getSign(Map<String, String> params) {
        params.put("appkey", "1d8b6e7d45233436");
        TreeMap<String, String> stringStringTreeMap = new TreeMap<>(params);
        StringBuffer stringBuffer = new StringBuffer();
        for (Map.Entry<String, String> entry : stringStringTreeMap.entrySet()) {
            if (stringBuffer.length() > 0) {
                stringBuffer.append("&");
            }
            stringBuffer
                    .append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8)).append("=")
                    .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
        }
        stringBuffer.append("560c52ccd288fed045859ed18bffd973");
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] digest = messageDigest.digest(stringBuffer.toString().getBytes(StandardCharsets.UTF_8));
            StringBuffer stringBuffer2 = new StringBuffer();
            for(byte b : digest) {
                stringBuffer2.append(String.format("%02x", b));
            }
            return stringBuffer2.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
