package com.cbq.yatori.core.utils;

import java.util.ArrayList;
import java.util.List;

public class ChatGLMChat {
    List<ChatGLMMessage> messages;


    public ChatGLMChatBuilder builder(){
        return new ChatGLMChatBuilder();
    }
    public class ChatGLMChatBuilder{
        private List<ChatGLMMessage> messages=new ArrayList<>();

        public ChatGLMChatBuilder addMessage(ChatGLMMessage message){
            messages.add(message);
            return this;
        }

        public List<ChatGLMMessage> getMessages() {
            return messages;
        }

        public ChatGLMChat build(){
            ChatGLMChat chatGLMChat=new ChatGLMChat();
            chatGLMChat.messages=messages;
            return chatGLMChat;
        }
    }

}
