package com.cbq.yatori.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author ChangBaiQi
 * @description TODO 用于对接星火的工具类
 * @date 2024/11/1 21:48
 * @version 1.0
 */
@Slf4j
public class XHAIUtil {
    /**
     * 星火访问接口
     * @param apiKey apikey 对应星火官网的APIKEY
     * @param chatGLMChat 对应加入的聊天记录
     * @return
     */
    public static synchronized String getChatMessage(String apiKey,ChatGLMChat chatGLMChat){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(30, TimeUnit.SECONDS)//设置读取超时时间
                .build();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String url = "https://spark-api-open.xf-yun.com/v1/chat/completions";
        ObjectMapper objectMapper = new ObjectMapper();
        String messages ="[]";
        try {
            messages = objectMapper.writeValueAsString(chatGLMChat.messages);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        String json = "{\"model\": \"generalv3.5\",\"messages\": "+messages+"}";

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();
        String result="";
        try (Response response = client.newCall(request).execute()) {
            result = response.body().string();
            HashMap<String,Object> hashMap = objectMapper.readValue(result, HashMap.class);

            return ((HashMap<String,Object>)(((ArrayList<HashMap<String, Object>>)hashMap.get("choices")).get(0).get("message"))).get("content").toString();
        } catch (NullPointerException e){
            log.error(result);
            e.printStackTrace();
        }
        catch (IOException e) {
            log.error(result);
            e.printStackTrace();
        }
        return "";
    }
}
