package com.cbq.yatori.core.utils;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * @description: TODO 主要用于学习通加密登录
 * @author 长白崎
 * @date 2024/2/29 0:53
 * @version 1.0
 */
public class EncryptByDESUtils {
//    private static final String KEY = "Hello Ketty";

    /**
     * 加密
     * @param content 待加密内容
     * @param secretKeyStr 加密使用的 AES 密钥，BASE64 编码后的字符串
     * @param iv 初始化向量，长度为 16 个字节
     * @return 加密后的密文,进行 BASE64 处理之后返回
     */
    public static String encryptAES(byte[] content, String secretKeyStr, String iv) {
        // 获得一个加密规则 SecretKeySpec
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKeyStr.getBytes(), "AES");
        // 获得加密算法实例对象 Cipher
        Cipher cipher = null; //"算法/模式/补码方式"
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
        // 获得一个 IvParameterSpec
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());  // 使用 CBC 模式，需要一个向量 iv, 可增加加密算法的强度
        // 根据参数初始化算法
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }

        // 执行加密并返回经 BASE64 处助理之后的密文
        try {
            return Base64.getEncoder().encodeToString(cipher.doFinal(content));
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * DES 加密
     * @param content
     * @return
     */
    public static byte[] desEncrypt(String KEY,String content) {
        try {
            KeyGenerator kGen = KeyGenerator.getInstance("DES");
            kGen.init(56, new SecureRandom(KEY.getBytes()));
            SecretKey secretKey = kGen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "DES");
            Cipher cipher = Cipher.getInstance("DES");// 创建密码器
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(byteContent);
            return result; // 加密
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * DES 解密
     * @param content
     * @return
     */
    public static byte[] desDecrypt(String KEY,byte[] content) throws Exception {
        KeyGenerator kGen = KeyGenerator.getInstance("DES");
        kGen.init(56, new SecureRandom(KEY.getBytes()));
        SecretKey secretKey = kGen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, "DES");
        Cipher cipher = Cipher.getInstance("DES"); // 创建密码器
        cipher.init(Cipher.DECRYPT_MODE, key); // 初始化
        byte[] result = cipher.doFinal(content); // 解密
        return result;
    }


    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }
}
