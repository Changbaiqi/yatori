package com.cbq.yatori.core.action.canghui;

import com.cbq.yatori.core.action.canghui.entity.loginresponse.ConverterLoginResponse;
import com.cbq.yatori.core.action.canghui.entity.loginresponse.LoginResponseRequest;
import com.cbq.yatori.core.action.canghui.entity.tologin.ConverterToLogin;
import com.cbq.yatori.core.action.canghui.entity.tologin.ToLoginRequest;
import com.cbq.yatori.core.entity.AccountCacheCangHui;
import com.cbq.yatori.core.entity.User;
import com.cbq.yatori.core.utils.FileUtils;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.*;
import java.net.SocketTimeoutException;
import java.util.Map;

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

        Request request = new Request.Builder()
                .url(user.getUrl() + "/api/v1/home/school_info")
                .addHeader("User-Agent", "Apifox/1.0.0 (https://www.apifox.cn)")
                .build();

        try {
            Response response = client.newCall(request).execute();
            if(!response.isSuccessful()){
                response.close();
                return null;
            }
            if (response.header("set-cookie") != null) {
                //System.out.println(response.header("set-cookie").toString());
                String[] split = response.header("set-cookie").split(";");
                for (String s : split) {
                    if (s.startsWith("SESSION")) {
                        return s.split("=")[1];
                    }
                }
            }
        }catch (SocketTimeoutException e){
            return null;
        }catch (JsonParseException jsonParseException){
            //这种一般是访问过于频繁造成，这边延迟一一下
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return null;
        } catch (IOException e) {
            log.error("");
            e.printStackTrace();
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
                .url(user.getUrl() + "/api/v1/Kaptcha?v=%7Btime%28%29%7D")
                .addHeader("Cookie", "SESSION=" + ((AccountCacheCangHui)user.getCache()).getSession())
                .addHeader("User-Agent", "Apifox/1.0.0 (https://www.apifox.cn)")
                .build();

        try {

            Response response = client.newCall(request).execute();
            byte[] bytes = response.body().bytes();
            File file =FileUtils.saveFile(bytes,user.getAccountType().name()+"_"+user.getAccount()+"_"+(int)Math.random()*99999+".png");
            return file;
        } catch (SocketTimeoutException e){
            return null;
        }catch (JsonParseException jsonParseException){
            //这种一般是访问过于频繁造成，这边延迟一一下
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return null;
        }catch (IOException e) {
            log.error("");
            e.printStackTrace();
        }
        //CodeUtil.getData(bytes);
        return null;
    }


    /**
     * -1002代表验证码错误
     * 登录
     */
    public static LoginResponseRequest toLogin(User user) {

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
            if(!response.isSuccessful()){//当响应失败时
                response.close();
                return null;
            }
            String json = response.body().string();
            LoginResponseRequest loginResponseRequest = ConverterLoginResponse.fromJsonString(json);

            if (loginResponseRequest.getCode() == -1002) {
                return loginResponseRequest;
            }
            if (loginResponseRequest.getCode() == -1001) {
//                System.out.println(jsonObject.get("msg"));
                log.info(loginResponseRequest.getMsg());
                return loginResponseRequest;
            }
//            log.info("登录成功");
//            return loginResponseRequest.getData().getToken();
            return loginResponseRequest;
        } catch (SocketTimeoutException e){
            return null;
        }catch (JsonParseException jsonParseException){
            //这种一般是访问过于频繁造成，这边延迟一一下
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return null;
        }catch (IOException e) {
            log.error("");
            e.printStackTrace();
        }
        return null;
    }


    /**
     * token续航
     * @param user
     * @return
     */
    public static Map online(User user){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        AccountCacheCangHui cache = (AccountCacheCangHui)user.getCache();
        Request request = new Request.Builder()
                .url(user.getUrl()+"/api/v1/report/submit")
                .method("GET", null)
                .addHeader("member-token", cache.getToken())
                .addHeader("Origin", user.getUrl())
                .addHeader("sec-ch-ua", "\"Not.A/Brand\";v=\"8\",\"Chromium\";v=\"114\",\"Microsoft Edge\";v=\"114\"")
                .addHeader("sec-ch-ua-platform", "Windows")
                .addHeader("Cookie", "SESSION="+cache.getSession())
                .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "*/*")
                .addHeader("Host", user.getUrl().replace("https://","").replace("http://","").replace("/",""))
                .addHeader("Connection", "keep-alive")
                .build();
        try {
            Response response = client.newCall(request).execute();
            if(!response.isSuccessful()){//当响应失败时
                response.close();
                return null;
            }
            ResponseBody body = response.body();
            String json = body.string();
            body.close();
            ObjectMapper objectMapper = new ObjectMapper();
            Map map = objectMapper.readValue(json, Map.class);

        } catch (SocketTimeoutException e){
            return null;
        }catch (JsonParseException jsonParseException){
            //这种一般是访问过于频繁造成，这边延迟一一下
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return null;
        } catch (IOException e) {
            log.error("");
            e.printStackTrace();
        }
        return null;
    }

}
