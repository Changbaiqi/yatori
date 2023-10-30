package com.cbq.brushlessons.core.action.yinghua;

import com.cbq.brushlessons.core.entity.AccountCacheYingHua;
import com.cbq.brushlessons.core.entity.User;
import com.cbq.brushlessons.core.utils.FileUtils;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginAction {
    public static String getSESSION(User user){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url(user.getUrl()+"/service/code?r={time()}")
                .method("GET", null)
                .addHeader("User-Agent", "Apifox/1.0.0 (https://www.apifox.cn)")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String session = response.header("Set-Cookie");
//            System.out.println(session);
            return session;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
            File file = FileUtils.saveFile(bytes, "ccc.png");
            return file;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 登录
     * @param user
     */
    public static void toLogin(User user){
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
                .url(user.getUrl()+"/user/login")
                .method("POST", body)
                .addHeader("Cookie", ((AccountCacheYingHua)user.getCache()).getSession())
                .addHeader("User-Agent", "Apifox/1.0.0 (https://www.apifox.cn)")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String cookies=response.header("Cookie");
            //记录token
            Pattern pattern = Pattern.compile("token=([^;]+);");
            Matcher matcher= pattern.matcher(((AccountCacheYingHua) user.getCache()).getSession());
            if(matcher.find()){
                ((AccountCacheYingHua)user.getCache()).setToken(matcher.group(1));
            }
            System.out.println(response.body().string());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
