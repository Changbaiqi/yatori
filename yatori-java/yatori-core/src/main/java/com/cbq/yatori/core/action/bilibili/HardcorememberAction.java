package com.cbq.yatori.core.action.bilibili;

import okhttp3.*;

import java.io.IOException;
/**
 * @author ChangBaiQi
 * @description TODO 核心部分，用于调用B站获取当前题目和答复题目等
 * @date 2024/11/2 12:04
 * @version 1.0
 */
public class HardcorememberAction {

    /**
     * 获取下一题
     * @param access_key App对应的 access_key
     * @return 返回题目的json数据
     */
    public static String getProblem(String access_key){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
//        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://api.bilibili.com/x/senior/v1/question?access_key="+access_key+"&appkey=1d8b6e7d45233436&csrf=c2422b646fab6f04c19bc0ab587b1423&disable_rcmd=0&mobi_app=android&platform=android&statistics=%7B%22appId%22%3A1%2C%22platform%22%3A3%2C%22version%22%3A%228.18.0%22%2C%22abtest%22%3A%22%22%7D&ts=1730344569&web_location=333.790&sign=09551e023b005221482636304f6ec4db")
                .method("GET", null)
                .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .addHeader("Accept", "*/*")
                .addHeader("Host", "api.bilibili.com")
                .addHeader("Connection", "keep-alive")
                .build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 提交答案接口
     * @param acess_key 这个是APP端对应的access_key参数
     * @param problemID 对应答复题目的ID
     * @param ans_hash 对应回复答案的ans_hash值
     * @param ans_text 对应回复答案的文本，注意这里文本要用url编码之后填写
     * @return 返回结果json数据字符串
     */
    public static String submitAnswer(String acess_key,String problemID,String ans_hash,String ans_text){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "access_key="+acess_key+"&ans_hash="+ans_hash+"&ans_text="+ans_text+"&appkey=1d8b6e7d45233436&csrf=c2422b646fab6f04c19bc0ab587b1423&disable_rcmd=0&id="+problemID+"&mobi_app=android&platform=android&statistics=%7B%22appId%22%3A1%2C%22platform%22%3A3%2C%22version%22%3A%228.18.0%22%2C%22abtest%22%3A%22%22%7D&ts=1730345598&sign=255fd090ef030e1f0ff89ae7993a83e9");
        Request request = new Request.Builder()
                .url("https://api.bilibili.com/x/senior/v1/answer/submit")
                .method("POST", body)
                .addHeader("buvid", "XY93C0B9A240724EA3529004216C8C4F32C20")
                .addHeader("x-bili-ticket", "eyJhbGciOiJIUzI1NiIsImtpZCI6InMwMyIsInR5cCI6IkpXVCJ9.eyJleHAiOjE3MzAzNzIyMjcsImlhdCI6MTczMDM0MzEyNywiYnV2aWQiOiJYWTkzQzBCOUEyNDA3MjRFQTM1MjkwMDQyMTZDOEM0RjMyQzIwIn0.LiVa-5KyO0ldv38fA2xNYA01uurpTYRVbTjiEzuwphM")
                .addHeader("Cookie", "SESSDATA=5929ea83%2C1745893857%2Cca5507a1CjBUG8sC-HSl88AaN90vK6s4D226CT9R_mLPFf2ri0NOAa8Dhxkhw_eNJmW1Sw3IJ_ASVnJvUVZVWXEyYkdrbC1GOTZXMFlnRUxjbk5MUW5EcXo4ZmNXaTAtWEQ4Um1CdW9TQnpMSjZsX1UteVJGeW1xOWd1UzNOZ3NxdW5uZHdyMjc2am9LZHVnIIEC;bili_jct=226482ee912d85fa27cda66158197d39;DedeUserID=36987520;DedeUserID__ckMd5=50068272fe19a426;sid=n2q1z7hn;Buvid=XY93C0B9A240724EA3529004216C8C4F32C20")
                .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .addHeader("Accept", "*/*")
                .addHeader("Host", "api.bilibili.com")
                .addHeader("Connection", "keep-alive")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
