package com.cbq.brushlessons.core.action.canghui.entity.startexam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;

public class ConverterStartExam {
    public static StartExam fromJsonString(String jsonStr){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            LinkedHashMap<String,Object> result  = (LinkedHashMap<String,Object>)objectMapper.readValue(jsonStr,LinkedHashMap.class);
            Integer code = (int) result.get("code");
            String msg =(String) result.get("msg");

            StartExam startExam = new StartExam(code,msg);
            return startExam;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
