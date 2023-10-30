package com.cbq.brushlessons.core.action.canghui;

import com.alibaba.fastjson.JSONObject;
import com.cbq.brushlessons.core.entity.AccountCacheCangHui;
import com.cbq.brushlessons.core.entity.User;
import okhttp3.*;

import java.io.*;

/**
 * @description: 登录相关的Action
 * @author 长白崎
 * @date 2023/10/23 15:19
 * @version 1.0
 */
public class LoginAction {

    /**
     * 获取SESSION
     *
     * @return
     */
    public static String getSESSION(User user) {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        //https://kkzxsx.bwgl.cn/api/v1/home/school_info
        //https://zxsx.canghuikeji.com/api
        Request request = new Request.Builder()
                .url(user.getUrl() + "/api/v1/home/school_info")
                .addHeader("User-Agent", "Apifox/1.0.0 (https://www.apifox.cn)")
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.header("set-cookie") != null) {
                //System.out.println(response.header("set-cookie").toString());
                String[] split = response.header("set-cookie").split(";");
                for (String s : split) {
                    if (s.startsWith("SESSION")) {
                        return s.split("=")[1];
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    /**
     * 获取验证码
     */
    public static File getCode(User user) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        //RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url(user.getUrl() + "/api/v1/Kaptcha?v=0.5168877616823098")
                .addHeader("Cookie", "SESSION=" + ((AccountCacheCangHui)user.getCache()).getSession())
                .addHeader("User-Agent", "Apifox/1.0.0 (https://www.apifox.cn)")
                .build();

        try {

            Response response = client.newCall(request).execute();
            byte[] bytes = response.body().bytes();
            InputStream in = new ByteArrayInputStream(bytes);
            FileOutputStream file = new FileOutputStream("code_" + user.getAccount() + ".jpeg");
            int j;
            while ((j = in.read()) != -1) {
                file.write(j);
            }
            file.flush();
            file.close();
            in.close();
            File file1 = new File("code_" +user.getAccount()+ ".jpeg");
            return file1;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //CodeUtil.getData(bytes);
    }


    /**
     * -1002代表验证码错误
     * 登录
     */
    public static String getToKen(User user) {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n" +
                "    \"code\": \"" + ((AccountCacheCangHui)user.getCache()).getCode() + "\",\n" +
                "    \"account\": \"" + user.getAccount() + "\",\n" +
                "    \"password\": \"" + user.getPassword() + "\"\n" +
                "}");

        Request request = new Request.Builder()
                .url(user.getUrl() + "/api/v1/auth")
                .method("POST", body)
                .addHeader("Cookie", "SESSION=" + ((AccountCacheCangHui)user.getCache()).getSession())
                .addHeader("User-Agent", "Apifox/1.0.0 (https://www.apifox.cn)")
                .addHeader("Content-Type", "application/json")
                .build();
        try {
            Response response = client.newCall(request).execute();
            JSONObject jsonObject = JSONObject.parseObject(response.body().string());
            if (jsonObject.getInteger("code") == -1002) {
                return "-1002";
            }
            if (jsonObject.getInteger("code") == -1001) {
                System.out.println(jsonObject.get("msg"));
                return "-1001";
            }
            //System.out.println(jsonObject.toString());
            System.out.println("登录成功！");
            return jsonObject.getJSONObject("data").getString("token");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
