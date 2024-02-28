package com.cbq.yatori.core.action.xuexitong;

import com.cbq.yatori.core.entity.AccountCacheXueXiTong;
import com.cbq.yatori.core.entity.AccountCacheYingHua;
import com.cbq.yatori.core.entity.User;
import com.cbq.yatori.core.utils.EncryptByDESUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class LoginAction {

    /**
     * 学习通登录
     * @param user
     */
    public static Map<String,Object> toLogin(User user){

        String acc= EncryptByDESUtils.encryptAES(user.getAccount().getBytes(),"u2oh6Vu^HWe4_AES", "u2oh6Vu^HWe4_AES"); //模仿账号加密
        String pass = EncryptByDESUtils.encryptAES(user.getPassword().getBytes(), "u2oh6Vu^HWe4_AES", "u2oh6Vu^HWe4_AES"); //模仿密码加密

        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("fid","-1")
                .addFormDataPart("uname",acc)
                .addFormDataPart("password",pass)
                .addFormDataPart("refer","http%3A%2F%2Fi.mooc.chaoxing.com")
                .addFormDataPart("t","true")
                .addFormDataPart("forbidotherlogin","0")
                .addFormDataPart("validate","")
                .addFormDataPart("doubleFactorLogin","0")
                .addFormDataPart("independentId","0")
                .addFormDataPart("independentNameId","0")
                .build();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();

        Request request = new Request.Builder()
                .url("https://passport2.chaoxing.com/fanyalogin")
                .method("POST", body)
//                .addHeader("Cookie", ((AccountCacheXueXiTong)user.getCache()).getSession())
                .addHeader("User-Agent", "Apifox/1.0.0 (https://www.apifox.cn)")
                .build();
        try {
            Response response = client.newCall(request).execute();
            if(!response.isSuccessful()){//当响应失败时
                response.close();
                return null;
            }
            String json = response.body().string();
            System.out.println(json);
//            return result;
        }catch (SocketTimeoutException e){
            return null;
        } catch (IOException e) {
            log.error("");
            e.printStackTrace();
        }
        return null;
    }
}
