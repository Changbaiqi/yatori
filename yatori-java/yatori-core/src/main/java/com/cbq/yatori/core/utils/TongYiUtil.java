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
 * @description TODO 通义千问的调用工具类
 * @date 2024/11/1 22:33
 * @version 1.0
 */
@Slf4j
public class TongYiUtil {

    public static synchronized String getChatMessage(String apiKey,ChatGLMChat chatGLMChat){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(30, TimeUnit.SECONDS)//设置读取超时时间
                .build();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String url = "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions";
        ObjectMapper objectMapper = new ObjectMapper();
        String messages ="[]";
        try {
            messages = objectMapper.writeValueAsString(chatGLMChat.messages);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        String json = "{\"model\": \"qwen-plus\",\"messages\": "+messages+"}";

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
