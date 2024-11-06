package com.cbq.yatori.core.action.canghui.entity.exam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ConverterExam {
    public static ExamJson fromJsonString(String jsonStr) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String,Object> json = null;

        json = objectMapper.readValue(jsonStr, HashMap.class);


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

            Integer id = (Integer) list.get("id");
            examCourse.setId(id);
            String title = (String) list.get("title");
            examCourse.setTitle(title);

            LinkedHashMap<String, ExamTopic> exams = new LinkedHashMap<>(); //综合试题

            getLibrary(list,exams);
            getLibrary(list,exams);

            examCourse.setExamTopics(exams);
            examJson.getExamCourses().add(examCourse);
        }

        return examJson;
    }

    /**
     * library检测
     * @param list
     * @param exams
     */
    public static void getLibrary(LinkedHashMap<String,Object> list,LinkedHashMap<String,ExamTopic> exams){
        //以下为library里面的-----------------------------------------------------
        if(!list.containsKey("library"))
            return;

        LinkedHashMap<String,Object> library = (LinkedHashMap<String,Object>)list.get("library");

        if(!library.containsKey("content"))
            return;

        LinkedHashMap<String,Object> content = (LinkedHashMap<String,Object>)library.get("content");

        if(!content.containsKey("data"))
            return;

        LinkedHashMap<String,Object> contentData = (LinkedHashMap<String,Object>)content.get("data");

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


    }

    /**
     * result检测
     * @param list
     * @param exams
     */
    public static void getResult(LinkedHashMap<String,Object> list,LinkedHashMap<String,ExamTopic> exams){
//以下为result里面的-------------------------------------------
        if(!list.containsKey("result"))
            return;

        LinkedHashMap<String,Object> result = (LinkedHashMap<String,Object>)list.get("result");
        if(result==null || result.isEmpty())
            return;

        if(!result.containsKey("content"))
            return;

        LinkedHashMap<String,Object> contentResult = (LinkedHashMap<String,Object>)result.get("content");

        if(!contentResult.containsKey("data"))
            return;

        LinkedHashMap<String,Object> contentDataResult = (LinkedHashMap<String,Object>)contentResult.get("data");

        for (String s1 : contentDataResult.keySet()) {
            LinkedHashMap<String,Object> topicJson = ( LinkedHashMap<String,Object>)contentDataResult.get(s1);

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
    }
}
