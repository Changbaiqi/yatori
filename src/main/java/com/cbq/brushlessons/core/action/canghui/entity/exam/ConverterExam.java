package com.cbq.brushlessons.core.action.canghui.entity.exam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ConverterExam {
    public ExamJson fromJsonString(String jsonStr){
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String,Object> json = null;
        try {
            json = objectMapper.readValue(jsonStr, HashMap.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        LinkedHashMap<String,Object> data = (LinkedHashMap<String, Object>)  json.get("data");
        if(data==null)
            return null;

        if(!data.containsKey("lists"))
            return null;
        ArrayList<LinkedHashMap<String,Object>> lists = (ArrayList<LinkedHashMap<String,Object>>)data.get("lists");
        if(lists==null)
            return null;


        ExamJson examJson = new ExamJson();
        examJson.setExamCourses(new ArrayList<>());

        for (LinkedHashMap<String,Object> list : lists) {
            ExamCourse examCourse = new ExamCourse();//单个课程


            if(!list.containsKey("library"))
                continue;

            LinkedHashMap<String,Object> library = (LinkedHashMap<String,Object>)list.get("library");

            if(!library.containsKey("content"))
                continue;

            LinkedHashMap<String,Object> content = (LinkedHashMap<String,Object>)library.get("content");

            if(!content.containsKey("data"))
                continue;

            LinkedHashMap<String,Object> contentData = (LinkedHashMap<String,Object>)content.get("data");

            HashMap<String, ExamTopic> exams = new HashMap<>();
            for (String s1 : contentData.keySet()) {
                LinkedHashMap<String,Object> topicJson = ( LinkedHashMap<String,Object>)contentData.get(s1);

                ExamTopic topic = new ExamTopic();
                topic.setId((int)topicJson.get("id"));
                topic.setTitle((String) topicJson.get("title"));
                topic.setType((int)topicJson.get("type"));
                topic.setDifficulty((int)topicJson.get("difficulty"));
                topic.setItem(new ArrayList<>());

                ArrayList<LinkedHashMap<String,Object>> items =(ArrayList<LinkedHashMap<String,Object>>) topicJson.get("item");
                for (LinkedHashMap<String, Object> item : items) {
                    String key = (String) item.get("key");
                    String value = (String) item.get("value");
                    Integer score = (int)item.get("score");
                    Boolean isCorrect =(boolean) item.get("isCorrect");
                    topic.getItem().add(new ExamItem(key,value,score,isCorrect));
                }
                exams.put(s1,topic);
            }

            examCourse.setExamTopics(exams);
            examJson.getExamCourses().add(examCourse);
        }

        return examJson;
    }
}
