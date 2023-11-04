package com.cbq.brushlessons.core.action.yinghua;

import com.cbq.brushlessons.core.entity.AccountCacheYingHua;
import com.cbq.brushlessons.core.entity.User;
import com.cbq.brushlessons.core.utils.FileUtils;
import com.cbq.brushlessons.core.utils.VerificationCodeUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class LoginAction {
    public static String getSESSION(User user){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url(user.getUrl()+"/service/code?r={time()}")
                .method("GET", null)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String session = response.header("Set-Cookie");
//            System.out.println(session);
            return session;
        }catch (SocketTimeoutException e){
            return null;
        } catch (IOException e) {
            log.error("");
            e.printStackTrace();
        }
        return null;
    }

    public static File getCode(User user){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url(user.getUrl()+"/service/code?r={time()}")
                .method("GET", null)
                .addHeader("User-Agent", "Apifox/1.0.0 (https://www.apifox.cn)")
                .addHeader("Cookie", ((AccountCacheYingHua)user.getCache()).getSession())
                .build();
        try {
            Response response = client.newCall(request).execute();
            byte[] bytes = response.body().bytes();
            File file = FileUtils.saveFile(bytes, user.getAccountType().name()+"_"+user.getAccount()+"_"+(int)Math.random()*99999+".png");
            return file;
        }catch (SocketTimeoutException e){
            return null;
        } catch (IOException e) {
            log.error("");
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 登录
     * @param user
     */
    public static Map<String,Object> toLogin(User user){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("username",user.getAccount())
                .addFormDataPart("password",user.getPassword())
                .addFormDataPart("code",((AccountCacheYingHua)user.getCache()).getCode())
                .addFormDataPart("redirect","")
                .build();
        Request request = new Request.Builder()
                .url(user.getUrl()+"/user/login.json")
                .method("POST", body)
                .addHeader("Cookie", ((AccountCacheYingHua)user.getCache()).getSession())
                .addHeader("User-Agent", "Apifox/1.0.0 (https://www.apifox.cn)")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String json = response.body().string();
            String cookies=response.header("Cookie");
            //记录token
            Pattern pattern = Pattern.compile("token=([^;]+);");
            Matcher matcher= pattern.matcher(((AccountCacheYingHua) user.getCache()).getSession());
            if(matcher.find()){
                ((AccountCacheYingHua)user.getCache()).setToken(matcher.group(1));
            }
//            System.out.println(response.body().string());
            Map<String,Object> result = new ObjectMapper().readValue(json,Map.class);
            return result;
        }catch (SocketTimeoutException e){
            return null;
        } catch (IOException e) {
            log.error("");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 用于维持登录状态
     * @param user
     * @return 如果返回true则表示正常登录状态，false表示登录失败
     */
    public static Map online(User user){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("platform","Android")
                .addFormDataPart("version","1.4.8")
                .addFormDataPart("token",((AccountCacheYingHua)user.getCache()).getToken())
                .addFormDataPart("schoolId","0")
                .build();
        Request request = new Request.Builder()
                .url(user.getUrl()+"/api/online.json")
                .method("POST", body)
                .addHeader("User-Agent", "Apifox/1.0.0 (https://www.apifox.cn)")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String json = response.body().string();
            ObjectMapper objectMapper = new ObjectMapper();
            Map map = objectMapper.readValue(json, Map.class);
            return map;
        }catch (SocketTimeoutException e){
            return null;
        } catch (IOException e) {
            log.error("");
            e.printStackTrace();
        }
        return null;
    }

}
