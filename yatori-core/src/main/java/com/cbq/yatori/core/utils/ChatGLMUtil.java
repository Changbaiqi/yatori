package com.cbq.yatori.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.*;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @description: ChatGLM工具类
 * @author 长白崎
 * @date 2024/2/5 20:45
 * @version 1.0
 */
@Slf4j
public class ChatGLMUtil {
    private static final String API_KEY = "";

    private static final String API_SECRET = "";

    private static final ClientV4 client = new ClientV4.Builder(API_KEY, API_SECRET).build();
    // 请自定义自己的业务id
    private static final String requestIdTemplate = "mycompany-%d";




    /**
     * ChatGLM直接聊天获取信息
     * @param message
     * @return
     */
    public static String getChatMessage(String API_KEY, String API_SECRET,String message) {
        ChatGLMUtil.client.setConfig(new ClientV4.Builder(API_KEY, API_SECRET).build().getConfig());
        // 请自定义自己的业务id
        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(), message);
        messages.add(chatMessage);
        String requestId = String.format(requestIdTemplate, System.currentTimeMillis());
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(Constants.ModelChatGLM4)
                .stream(Boolean.FALSE)
                .invokeMethod(Constants.invokeMethod)
                .messages(messages)
                .requestId(requestId)
                .build();
        ModelApiResponse invokeModelApiResp = client.invokeModelApi(chatCompletionRequest);

        List<Choice> choices = invokeModelApiResp.getData().getChoices();
        StringBuilder response = new StringBuilder();
        for (Choice choice : choices) {
            response.append(choice.getMessage().getContent()).append("\n");
        }

//        System.out.println("model output:"+ JSON.toJSONString(invokeModelApiResp));

        return response.toString();
    }

    /**
     * 新智普清言ChatGLM4访问接口
     * @param apiKey apikey
     * @param chatGLMChat
     * @return
     */
    public static synchronized String getChatMessage(String apiKey,ChatGLMChat chatGLMChat){
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String url = "https://open.bigmodel.cn/api/paas/v4/chat/completions";
        ObjectMapper objectMapper = new ObjectMapper();
        String messages ="[]";
        try {
            messages = objectMapper.writeValueAsString(chatGLMChat.messages);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        String json = "{\"model\": \"glm-4\",\"messages\": "+messages+"}";

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
    /**
     * ChatGLM直接聊天获取信息
     * @param message
     * @return
     */
    public static String getChatMessage(String message) {
        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(), message);
        messages.add(chatMessage);
        String requestId = String.format(requestIdTemplate, System.currentTimeMillis());
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(Constants.ModelChatGLM4)
                .stream(Boolean.FALSE)
                .invokeMethod(Constants.invokeMethod)
                .messages(messages)
                .requestId(requestId)
                .build();
        ModelApiResponse invokeModelApiResp = client.invokeModelApi(chatCompletionRequest);

        List<Choice> choices = invokeModelApiResp.getData().getChoices();
        StringBuilder response = new StringBuilder();
        for (Choice choice : choices) {
            response.append(choice.getMessage().getContent()).append("\n");
        }
//        System.out.println("model output:"+ JSON.toJSONString(invokeModelApiResp));

        return response.toString();
    }
}
