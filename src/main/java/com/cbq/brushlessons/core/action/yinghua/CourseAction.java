package com.cbq.brushlessons.core.action.yinghua;

import com.cbq.brushlessons.core.action.yinghua.entity.allcourse.ConverterAllCourse;
import com.cbq.brushlessons.core.action.yinghua.entity.allcourse.CourseInform;
import com.cbq.brushlessons.core.action.yinghua.entity.allcourse.CourseRequest;
import com.cbq.brushlessons.core.action.yinghua.entity.allvideo.ConverterVideo;
import com.cbq.brushlessons.core.action.yinghua.entity.allvideo.NodeList;
import com.cbq.brushlessons.core.action.yinghua.entity.allvideo.VideoRequest;
import com.cbq.brushlessons.core.action.yinghua.entity.submitstudy.ConverterSubmitStudyTime;
import com.cbq.brushlessons.core.action.yinghua.entity.submitstudy.SubmitStudyTimeRequest;
import com.cbq.brushlessons.core.action.yinghua.entity.videomessage.VideoInformStudyTotal;
import com.cbq.brushlessons.core.action.yinghua.entity.videomessage.ConverterVideoMessage;
import com.cbq.brushlessons.core.action.yinghua.entity.videomessage.VideoInformRequest;
import com.cbq.brushlessons.core.entity.AccountCacheYingHua;
import com.cbq.brushlessons.core.entity.User;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.time.Duration;

@Slf4j
public class CourseAction {
    /**
     * 获取课程信息
     * @param user
     * @return
     */
    public static CourseRequest getAllCourseRequest(User user) {
        //判断是否初始化
        if(user.getAccount()==null) user.setCache(new AccountCacheYingHua());

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("platform", "Android")
                .addFormDataPart("version", "1.4.8")
                .addFormDataPart("type", "0")
                .addFormDataPart("token", ((AccountCacheYingHua) user.getCache()).getToken())
                .build();
        Request request = new Request.Builder()
                .url(user.getUrl()+"/api/course/list.json")
                .method("POST", body)
                .addHeader("Cookie", "tgw_I7_route=3d5c4e13e7d88bb6849295ab943042a2")
                .addHeader("User-Agent", "Apifox/1.0.0 (https://www.apifox.cn)")
                .build();
        try {
            Response response = client.newCall(request).execute();
            if(!response.isSuccessful()){//当响应失败时
                response.close();
                return null;
            }
            String json = response.body().string();
            CourseRequest courseRequest = ConverterAllCourse.fromJsonString(json);
            return courseRequest;
        } catch (SocketTimeoutException e){
            return null;
        } catch (IOException e) {
            log.error("");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取对应课程的视屏章节等信息
     * @param user
     * @param courseInform
     * @return
     */
    public static VideoRequest getCourseVideosList(User user, CourseInform courseInform){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("platform","Android")
                .addFormDataPart("version","1.4.8")
                .addFormDataPart("token",((AccountCacheYingHua)user.getCache()).getToken())
                .addFormDataPart("courseId", String.valueOf(courseInform.getId()))
                .build();
        Request request = new Request.Builder()
                .url(user.getUrl()+"/api/course/chapter.json")
                .method("POST", body)
                .addHeader("User-Agent", "Apifox/1.0.0 (https://www.apifox.cn)")
                .build();
        try {
            Response response = client.newCall(request).execute();
            if(!response.isSuccessful()){//当响应失败时
                response.close();
                return null;
            }
            String json = response.body().string();
            VideoRequest videoRequest = ConverterVideo.fromJsonString(json);
            return videoRequest;
        }catch (SocketTimeoutException e){
            return null;
        } catch (IOException e) {
            log.error("");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取单个视屏的详细观看信息，比如观看的学习时长
     * @param user
     * @param videoInform
     * @return
     */
    public static VideoInformRequest getVideMessage(User user, NodeList videoInform){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("platform","Android")
                .addFormDataPart("version","1.4.8")
                .addFormDataPart("nodeId", String.valueOf(videoInform.getId()))
                .addFormDataPart("token",((AccountCacheYingHua)user.getCache()).getToken())
                .build();
        Request request = new Request.Builder()
                .url(user.getUrl()+"/api/node/video.json")
                .method("POST", body)
                .addHeader("User-Agent", "Apifox/1.0.0 (https://www.apifox.cn)")
                .build();
        try {
            Response response = client.newCall(request).execute();
            if(!response.isSuccessful()){//当响应失败时
                response.close();
                return null;
            }
            String json = response.body().string();
            VideoInformRequest videoInformRequest = ConverterVideoMessage.fromJsonString(json);
            if (videoInformRequest.getResult().getData().getStudyTotal()==null) {
                VideoInformStudyTotal videoInformStudyTotal = new VideoInformStudyTotal();
                videoInformStudyTotal.setDuration("0");
                videoInformStudyTotal.setState("0");
                videoInformStudyTotal.setDuration("0");
                videoInformRequest.getResult().getData().setStudyTotal(videoInformStudyTotal);
            }
            return videoInformRequest;
        } catch (SocketTimeoutException e){
            return null;
        } catch (Exception e) {
            log.error("");
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 提交学时
     * @param user
     * @param videoInform
     * @param studyTime
     * @param studyId
     * @return
     */
    public static SubmitStudyTimeRequest submitStudyTime(User user, NodeList videoInform, long studyTime, long studyId){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("platform","Android")
                .addFormDataPart("version","1.4.8")
                .addFormDataPart("nodeId", String.valueOf(videoInform.getId()))
                .addFormDataPart("token",((AccountCacheYingHua)user.getCache()).getToken())
                .addFormDataPart("terminal","Android")
                .addFormDataPart("studyTime", String.valueOf(studyTime))
                .addFormDataPart("studyId",String.valueOf(studyId))
                .build();
        Request request = new Request.Builder()
                .url(user.getUrl()+"/api/node/study.json")
                .method("POST", body)
                .addHeader("User-Agent", "Apifox/1.0.0 (https://www.apifox.cn)")
                .build();
        try {
            Response response = client.newCall(request).execute();
            if(!response.isSuccessful()){//当响应失败时
                response.close();
                return null;
            }
            String json = response.body().string();
            SubmitStudyTimeRequest submitStudyTimeRequest = ConverterSubmitStudyTime.fromJsonString(json);
            return submitStudyTimeRequest;
        }catch (SocketTimeoutException e){
            return null;
        } catch (IOException e) {
            log.error("");
            e.printStackTrace();
        }
        return null;
    }




}
