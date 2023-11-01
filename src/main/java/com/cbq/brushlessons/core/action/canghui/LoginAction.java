package com.cbq.brushlessons.core.action.canghui;

import com.alibaba.fastjson.JSONObject;
import com.cbq.brushlessons.core.action.canghui.entity.loginresponse.ConverterLoginResponse;
import com.cbq.brushlessons.core.action.canghui.entity.loginresponse.LoginResponseRequest;
import com.cbq.brushlessons.core.action.canghui.entity.tologin.ConverterToLogin;
import com.cbq.brushlessons.core.action.canghui.entity.tologin.ToLoginRequest;
import com.cbq.brushlessons.core.entity.AccountCacheCangHui;
import com.cbq.brushlessons.core.entity.User;
import com.cbq.brushlessons.core.utils.FileUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.*;

/**
 * @description: 登录相关的Action
 * @author 长白崎
 * @date 2023/10/23 15:19
 * @version 1.0
 */
@Slf4j
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
            File file =FileUtils.saveFile(bytes,user.getAccountType().name()+"_"+user.getAccount()+"_"+(int)Math.random()*99999+".png");
            return file;
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

        //构建登录json
        ToLoginRequest loginRequest = new ToLoginRequest();
        loginRequest.setCode(((AccountCacheCangHui)user.getCache()).getCode());
        loginRequest.setAccount(user.getAccount());
        loginRequest.setPassword(user.getPassword());
        RequestBody body = null;
        try {
            body = RequestBody.create(mediaType, ConverterToLogin.toJsonString(loginRequest));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Request request = new Request.Builder()
                .url(user.getUrl() + "/api/v1/auth")
                .method("POST", body)
                .addHeader("Cookie", "SESSION=" + ((AccountCacheCangHui)user.getCache()).getSession())
                .addHeader("User-Agent", "Apifox/1.0.0 (https://www.apifox.cn)")
                .addHeader("Content-Type", "application/json")
                .build();

        try {
            Response response = client.newCall(request).execute();
            String json = response.body().string();
            LoginResponseRequest loginResponseRequest = ConverterLoginResponse.fromJsonString(json);

            if (loginResponseRequest.getCode() == -1002) {
                return "-1002";
            }
            if (loginResponseRequest.getCode() == -1001) {
//                System.out.println(jsonObject.get("msg"));
                log.info(loginResponseRequest.getMsg());
                return "-1001";
            }
            log.info("登录成功");
            return loginResponseRequest.getData().getToken();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
