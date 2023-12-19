package com.cbq.brushlessons.core.action.canghui;


import com.cbq.brushlessons.core.action.canghui.entity.exam.ConverterExam;
import com.cbq.brushlessons.core.action.canghui.entity.exam.ExamJson;
import com.cbq.brushlessons.core.action.canghui.entity.examsubmit.ConverterExamSubmit;
import com.cbq.brushlessons.core.action.canghui.entity.examsubmit.TopicRequest;
import com.cbq.brushlessons.core.action.canghui.entity.examsubmitrespose.ConverterExamSubmitResponse;
import com.cbq.brushlessons.core.action.canghui.entity.examsubmitrespose.ExamSubmitResponse;
import com.cbq.brushlessons.core.action.canghui.entity.startexam.ConverterStartExam;
import com.cbq.brushlessons.core.action.canghui.entity.startexam.StartExam;
import com.cbq.brushlessons.core.entity.AccountCacheCangHui;
import com.cbq.brushlessons.core.entity.User;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

/**
 * @description: TODO 考试相关
 * @author 长白崎
 * @date 2023/11/28 1:21
 * @version 1.0
 */
@Slf4j
public class ExamAction {

    /**
     * 获取对应课程考试列表
     * @param user 用户
     * @param courseId 课程Id
     * @return
     */
    public static ExamJson getExamList(User user,String courseId){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n    \"pageSize\": 99,\r\n    \"courseId\": \""+courseId+"\",\r\n    \"page\": 1\r\n}");
        AccountCacheCangHui cache = (AccountCacheCangHui) user.getCache();
        Request request = new Request.Builder()
                .url("https://kkzxsx.lidapoly.edu.cn/api/v1/course/study/my/exam")
                .method("POST", body)
                .addHeader("member-token", cache.getToken())
                .addHeader("Origin", user.getUrl())
                .addHeader("sec-ch-ua", "\"Not.A/Brand\";v=\"8\",\"Chromium\";v=\"114\",\"Microsoft Edge\";v=\"114\"")
                .addHeader("sec-ch-ua-platform", "Windows")
                .addHeader("Cookie", "SESSION="+cache.getSession())
                .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "*/*")
                .addHeader("Host", user.getUrl().replace("https://","").replace("http://","").replace("/",""))
                .addHeader("Connection", "keep-alive")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            ExamJson examJson = ConverterExam.fromJsonString(string);
            return examJson;
        } catch(SocketTimeoutException socketTimeoutException){
            return null;
        }catch (JsonParseException e){
            return null;
        } catch (IOException e) {
            log.error("");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 开始考试，先要执行这个才能开始考试
     * @param user 用户
     * @param examId 考试试卷Id，注意是试卷ID！！！！
     * @return
     */
    public static StartExam startExam(User user,String examId){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"examId\":\""+examId+"\"}");
        AccountCacheCangHui cache = (AccountCacheCangHui)user.getCache();
        Request request = new Request.Builder()
                .url(user.getUrl()+"/api/v1/course/study/start/exam")
                .method("POST", body)
                .addHeader("member-token", cache.getToken())
                .addHeader("Origin", user.getUrl())
                .addHeader("sec-ch-ua", "\"Not.A/Brand\";v=\"8\",\"Chromium\";v=\"114\",\"Microsoft Edge\";v=\"114\"")
                .addHeader("sec-ch-ua-platform", "Windows")
                .addHeader("Cookie", "SESSION="+cache.getSession())
                .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "*/*")
                .addHeader("Host", user.getUrl().replace("https://","").replace("http://","").replace("/",""))
                .addHeader("Connection", "keep-alive")
                .build();
        try {
            Response response = client.newCall(request).execute();
            if(!response.isSuccessful()){//当响应失败时
                response.close();
                return null;
            }
            String string = response.body().string();
            StartExam startExam = ConverterStartExam.fromJsonString(string);
            return startExam;
        } catch(SocketTimeoutException socketTimeoutException){
            return null;
        } catch (IOException e) {
            log.error("");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 提交考试
     * @param user
     * @param topicRequest
     * @return
     */
    public static ExamSubmitResponse submitExam(User user, TopicRequest topicRequest){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = null;
        try {
            body = RequestBody.create(mediaType, ConverterExamSubmit.toJsonString(topicRequest));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        AccountCacheCangHui cache = (AccountCacheCangHui)user.getCache();
        Request request = new Request.Builder()
                .url("https://kkzxsx.lidapoly.edu.cn/api/v1/course/study/submit/exam")
                .method("POST", body)
                .addHeader("member-token", cache.getToken())
                .addHeader("Origin", user.getUrl())
                .addHeader("sec-ch-ua", "\"Not.A/Brand\";v=\"8\",\"Chromium\";v=\"114\",\"Microsoft Edge\";v=\"114\"")
                .addHeader("sec-ch-ua-platform", "Windows")
                .addHeader("Cookie", "SESSION="+cache.getSession())
                .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "*/*")
                .addHeader("Host", user.getUrl().replace("https://","").replace("http://","").replace("/",""))
                .addHeader("Connection", "keep-alive")
                .build();
        try {
            Response response = client.newCall(request).execute();
            if(!response.isSuccessful()){//当响应失败时
                response.close();
                return null;
            }
            String string = response.body().string();
            ExamSubmitResponse examSubmitResponse = ConverterExamSubmitResponse.fromJsonString(string);
            return examSubmitResponse;
        } catch(SocketTimeoutException socketTimeoutException){
            return null;
        } catch (IOException e) {
            log.error("");
            e.printStackTrace();
        }
        return null;
    }
}
