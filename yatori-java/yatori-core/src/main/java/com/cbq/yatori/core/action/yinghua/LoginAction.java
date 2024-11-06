package com.cbq.yatori.core.action.yinghua;

import com.cbq.yatori.core.entity.AccountCacheYingHua;
import com.cbq.yatori.core.entity.User;
import com.cbq.yatori.core.utils.CustomTrustManager;
import com.cbq.yatori.core.utils.FileUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class LoginAction {
    public static String getSESSION(User user){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .sslSocketFactory(CustomTrustManager.getSSLContext().getSocketFactory(), new CustomTrustManager())
                .hostnameVerifier((hostname, session) -> true) // Bypass hostname verification
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url(user.getUrl()+"/service/code?r=%7Btime%28%29%7D")
                .method("GET", null)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if(!response.isSuccessful()){//当响应失败时
                response.close();
                return null;
            }
            String session = response.header("Set-Cookie");
            if(session==null)
                session = response.header("Cookie");
            response.close();
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
                .sslSocketFactory(CustomTrustManager.getSSLContext().getSocketFactory(), new CustomTrustManager())
                .hostnameVerifier((hostname, session) -> true) // Bypass hostname verification
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
            if(!response.isSuccessful()){//当响应失败时
                response.close();
                return null;
            }
            byte[] bytes = new byte[0];
            if (response.body() != null) {
                bytes = response.body().bytes();
            }
            File file = FileUtils.saveFile(bytes, user.getAccountType().name()+"_"+user.getAccount()+"_"+ 0 +".png");
            response.close();
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
                .sslSocketFactory(CustomTrustManager.getSSLContext().getSocketFactory(), new CustomTrustManager())
                .hostnameVerifier((hostname, session) -> true) // Bypass hostname verification
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
            if(!response.isSuccessful()){//当响应失败时
                response.close();
                return null;
            }
            String json = null;
            if (response.body() != null) {
                json = response.body().string();
            }
            String cookies=response.header("Cookie");
            //记录token
            Pattern pattern = Pattern.compile("token=([^;]+);");
            Matcher matcher= pattern.matcher(((AccountCacheYingHua) user.getCache()).getSession());
            if(matcher.find()){
                ((AccountCacheYingHua)user.getCache()).setToken(matcher.group(1));
            }
//            System.out.println(response.body().string());
            Map<String,Object> result =new ObjectMapper().readValue(json,Map.class);
            response.close();
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
    public static Map<String,Object> online(User user){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .sslSocketFactory(CustomTrustManager.getSSLContext().getSocketFactory(), new CustomTrustManager())
                .hostnameVerifier((hostname, session) -> true) // Bypass hostname verification
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
            if(!response.isSuccessful()){//当响应失败时
                response.close();
                return null;
            }
            String json = null;
            if (response.body() != null) {
                json = response.body().string();
            }
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String,Object> map = objectMapper.readValue(json, Map.class);
            response.close();
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
