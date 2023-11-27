package com.cbq.brushlessons.core.action.canghui;


import com.cbq.brushlessons.core.entity.AccountCacheCangHui;
import com.cbq.brushlessons.core.entity.User;
import okhttp3.*;

import java.io.IOException;

public class ExamAction {
    public void getExamList(User user){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n    \"pageSize\": 99,\r\n    \"courseId\": \"500\",\r\n    \"page\": 1\r\n}");
        Request request = new Request.Builder()
                .url(user.getUrl()+"/api/v1/course/study/my/exam")
                .method("POST", body)
                .addHeader("member-token", ((AccountCacheCangHui)user.getCache()).getToken())
                .addHeader("Origin", "https://kkzxsx.lidapoly.edu.cn")
                .addHeader("sec-ch-ua", "\"Not.A/Brand\";v=\"8\",\"Chromium\";v=\"114\",\"Microsoft Edge\";v=\"114\"")
                .addHeader("sec-ch-ua-platform", "Windows")
                .addHeader("Cookie", "SESSION="+((AccountCacheCangHui)user.getCache()).getSession())
                .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "*/*")
                .addHeader("Host", user.getUrl().replace("https://","").replace("http://",""))
                .addHeader("Connection", "keep-alive")
                .build();
        try {
            Response response = client.newCall(request).execute();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
