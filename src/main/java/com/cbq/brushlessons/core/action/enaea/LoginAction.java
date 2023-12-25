package com.cbq.brushlessons.core.action.enaea;

import com.cbq.brushlessons.core.action.enaea.entity.LoginAblesky;
import com.cbq.brushlessons.core.entity.AccountCache;
import com.cbq.brushlessons.core.entity.AccountCacheEnaea;
import com.cbq.brushlessons.core.entity.User;
import com.cbq.brushlessons.core.utils.MD5Utils;
import okhttp3.*;

import java.io.IOException;
import java.util.List;

public class LoginAction {

    /**
     * 登录请求
     * @param user 用户对象
     * @return
     */
    public static LoginAblesky toLogin(User user){
        AccountCacheEnaea cache =(AccountCacheEnaea) user.getCache();
        String simTime = String.valueOf(System.currentTimeMillis()); //获取当前时间戳
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
//        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://passport.enaea.edu.cn/login.do?ajax=true&jsonp=ablesky_"+simTime+"&j_username="+user.getAccount()+"&j_password="+ MD5Utils.getMD5Str(user.getPassword(),32)+"&_acegi_security_remember_me=false&_="+simTime)
                .method("GET", null)
                .addHeader("Referer", "https://study.enaea.edu.cn/login.do")
                .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .addHeader("Accept", "*/*")
                .addHeader("Host", "passport.enaea.edu.cn")
                .addHeader("Connection", "keep-alive")
                .build();
        try {
            Response response = client.newCall(request).execute();
            if(!response.isSuccessful()){//当响应失败时
                response.close();
                return null;
            }
            String string = response.body().string();
            LoginAblesky loginAblesky = LoginAblesky.toLoginAblesky(string);
//            cache.setASUSS(response.header());
            List<String> headers = response.headers("Set-Cookie");
            //获取ASUSS（也就是token）
            for (String header : headers) {
                if(header.startsWith("ASUSS=")){
                    String asuss = header.split(";")[0].replace("ASUSS=","");
                    cache.setASUSS(asuss);
                    break;
                }
            }
            return loginAblesky;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
