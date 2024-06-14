package com.cbq.yatori.core.action.canghui;


import com.cbq.yatori.core.action.canghui.entity.exam.ConverterExam;
import com.cbq.yatori.core.action.canghui.entity.exam.ExamJson;
import com.cbq.yatori.core.action.canghui.entity.examsubmit.ConverterExamSubmit;
import com.cbq.yatori.core.action.canghui.entity.examsubmit.TopicRequest;
import com.cbq.yatori.core.action.canghui.entity.examsubmitrespose.ConverterExamSubmitResponse;
import com.cbq.yatori.core.action.canghui.entity.examsubmitrespose.ExamSubmitResponse;
import com.cbq.yatori.core.action.canghui.entity.startexam.ConverterStartExam;
import com.cbq.yatori.core.action.canghui.entity.startexam.StartExam;
import com.cbq.yatori.core.entity.AccountCacheCangHui;
import com.cbq.yatori.core.entity.User;
import com.cbq.yatori.core.utils.CustomTrustManager;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

/**
 * @author 长白崎
 * @version 1.0
 * @description: TODO 考试相关
 * @date 2023/11/28 1:21
 */
@Slf4j
public class ExamAction {

    /**
     * 获取对应课程考试列表
     *
     * @param user     用户
     * @param courseId 课程Id
     * @return
     */
    public static ExamJson getExamList(User user, String courseId) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .sslSocketFactory(CustomTrustManager.getSSLContext().getSocketFactory(), new CustomTrustManager())
                .hostnameVerifier((hostname, session) -> true) // Bypass hostname verification
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n    \"pageSize\": 99,\r\n    \"courseId\": \"" + courseId + "\",\r\n    \"page\": 1\r\n}");
        AccountCacheCangHui cache = (AccountCacheCangHui) user.getCache();
        String urlSuffix = "/api/v1/course/study/my/exam";
        Request request = getRequest(user, body, cache, urlSuffix);
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            return ConverterExam.fromJsonString(string);
        } catch (Exception e) {
            log.error("");
        }
        return null;
    }

    /**
     * 开始考试，先要执行这个才能开始考试
     *
     * @param user   用户
     * @param examId 考试试卷Id，注意是试卷ID！！！！
     * @return
     */
    public static StartExam startExam(User user, String examId) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .sslSocketFactory(CustomTrustManager.getSSLContext().getSocketFactory(), new CustomTrustManager())
                .hostnameVerifier((hostname, session) -> true) // Bypass hostname verification
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"examId\":\"" + examId + "\"}");
        AccountCacheCangHui cache = (AccountCacheCangHui) user.getCache();
        String urlSuffix = "/api/v1/course/study/start/exam";
        Request request = getRequest(user, body, cache, urlSuffix);
        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {//当响应失败时
                response.close();
                return null;
            }
            String string = response.body().string();
            return ConverterStartExam.fromJsonString(string);
        } catch (Exception e) {
            log.error("出现问题 = {}", e.getMessage());
        }
        return null;
    }

    /**
     * 提交考试
     *
     * @param user
     * @param topicRequest
     * @return
     */
    public static ExamSubmitResponse submitExam(User user, TopicRequest topicRequest) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .sslSocketFactory(CustomTrustManager.getSSLContext().getSocketFactory(), new CustomTrustManager())
                .hostnameVerifier((hostname, session) -> true) // Bypass hostname verification
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        try {
            RequestBody body = RequestBody.create(mediaType, ConverterExamSubmit.toJsonString(topicRequest));
            AccountCacheCangHui cache = (AccountCacheCangHui) user.getCache();
            String urlSuffix = "/api/v1/course/study/submit/exam";
            Request request = getRequest(user, body, cache, urlSuffix);
            Response response = client.newCall(request).execute();
            //当响应失败时
            if (!response.isSuccessful()) {
                response.close();
                return null;
            }
            String string = response.body().string();
            return ConverterExamSubmitResponse.fromJsonString(string);
        } catch (Exception e) {
            log.error("提交考试出现问题 = {}", e.getMessage());
        }
        return null;
    }

    /**
     * 获取相应
     * @param user 用户
     * @param body 请求体
     * @param cache token
     * @param urlSuffix 请求地址后缀
     * @return Request
     */
    private static Request getRequest(User user, RequestBody body, AccountCacheCangHui cache, String urlSuffix) {
        return new Request.Builder()
                .url(user.getUrl() + urlSuffix)
                .method("POST", body)
                .addHeader("member-token", cache.getToken())
                .addHeader("Origin", user.getUrl())
                .addHeader("sec-ch-ua", "\"Not.A/Brand\";v=\"8\",\"Chromium\";v=\"114\",\"Microsoft Edge\";v=\"114\"")
                .addHeader("sec-ch-ua-platform", "Windows")
                .addHeader("Cookie", "SESSION=" + cache.getSession())
                .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "*/*")
                .addHeader("Host", user.getUrl().replace("https://", "").replace("http://", "").replace("/", ""))
                .addHeader("Connection", "keep-alive")
                .build();
    }
}
