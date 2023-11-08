package com.cbq.brushlessons.core.action.enaea;

import okhttp3.*;

import java.io.IOException;

public class LoginAction {

    public static void checkUser(){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://study.enaea.edu.cn/account.do?action=<action>&username=<username>&_=<_>")
                .method("GET", body)
                .addHeader("User-Agent", "Apifox/1.0.0 (https://www.apifox.cn)")
                .build();
        try {
            Response response = client.newCall(request).execute();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
