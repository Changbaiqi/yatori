package com.cbq.yatori.core.utils;

import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: ChatGLM工具类
 * @author 长白崎
 * @date 2024/2/5 20:45
 * @version 1.0
 */
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
