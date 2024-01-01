package com.cbq.yatori.core.action.yinghua;

import com.cbq.yatori.core.entity.AccountCacheYingHua;
import com.cbq.yatori.core.entity.User;
import okhttp3.*;

import java.io.IOException;

/**
 * @description: TODO 考试相关
 * @author 长白崎
 * @date 2023/12/1 12:11
 * @version 1.0
 */
public class ExamAction {

    /**
     * 获取考试相关的一些信息
     * @param user
     * @param nodeId
     */
    public static void getExam(User user,String nodeId){
        AccountCacheYingHua cache = (AccountCacheYingHua) user.getCache();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("multipart/form-data; boundary=--------------------------393670526422134055864302");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("platform","Android")
                .addFormDataPart("version","1.4.8")
                .addFormDataPart("nodeId",nodeId)
                .addFormDataPart("token",cache.getToken())
                .addFormDataPart("terminal","Android")
                .build();
        Request request = new Request.Builder()
                .url(user.getUrl()+"/api/node/exam.json")
                .method("POST", body)
                .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .addHeader("Accept", "*/*")
                .addHeader("Host", user.getUrl().replace("https://","").replace("http://","").replace("/",""))
                .addHeader("Connection", "keep-alive")
                .addHeader("Content-Type", "multipart/form-data; boundary=--------------------------393670526422134055864302")
                .build();
        try {
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 开始考试接口
     * @param user 用户
     * @param courseId 课程ID
     * @param nodeId 对应章节结点Id
     * @param examId 考试Id
     */
    public static void startExam(User user,String courseId,String nodeId,String examId){
        AccountCacheYingHua cache = (AccountCacheYingHua)user.getCache();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("multipart/form-data; boundary=--------------------------244120585315146738764763");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("platform","Android")
                .addFormDataPart("version","1.4.8")
                .addFormDataPart("nodeId",nodeId)
                .addFormDataPart("token",cache.getToken())
                .addFormDataPart("terminal","Android")
                .addFormDataPart("examId",examId)
                .addFormDataPart("courseId",courseId)
                .build();
        Request request = new Request.Builder()
                .url(user.getUrl()+"/api/exam/start.json")
                .method("POST", body)
                .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .addHeader("Accept", "*/*")
                .addHeader("Host", user.getUrl().replace("https://","").replace("http://","").replace("/",""))
                .addHeader("Connection", "keep-alive")
                .addHeader("Content-Type", "multipart/form-data; boundary=--------------------------244120585315146738764763")
                .build();
        try {
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 提交答题的接口
     * @param user 用户
     * @param examId 答题的考试试卷Id
     * @param answerId 答题的题目Id
     * @param answer 所提交的答案或选项，比如A，B等
     * @param finish 是否是最后提交并且结束考试，0代表不是，1代表是
     */
    public static void submitExam(User user,String examId,String answerId, String answer,String finish){
        AccountCacheYingHua cache = (AccountCacheYingHua) user.getCache();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("multipart/form-data; boundary=--------------------------326388482122783598484776");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("platform","Android")
                .addFormDataPart("version","1.4.8")
                .addFormDataPart("examId",examId)
                .addFormDataPart("terminal","Android")
                .addFormDataPart("answerId",answerId)
                .addFormDataPart("finish",finish)
                .addFormDataPart("token",cache.getToken())
                .addFormDataPart("answer",answer)
                .build();
        Request request = new Request.Builder()
                .url(user.getUrl()+"/api/exam/submit.json")
                .method("POST", body)
                .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .addHeader("Accept", "*/*")
                .addHeader("Host", user.getUrl().replace("https://","").replace("http://","").replace("/",""))
                .addHeader("Connection", "keep-alive")
                .addHeader("Content-Type", "multipart/form-data; boundary=--------------------------326388482122783598484776")
                .build();
        try {
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
