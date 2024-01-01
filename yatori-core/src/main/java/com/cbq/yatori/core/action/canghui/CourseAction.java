package com.cbq.yatori.core.action.canghui;

import com.cbq.yatori.core.action.canghui.entity.coursedetail.ConverterCourseDetail;
import com.cbq.yatori.core.action.canghui.entity.coursedetail.CourseDetailData;
import com.cbq.yatori.core.action.canghui.entity.coursedetail.CourseDetailRequest;
import com.cbq.yatori.core.action.canghui.entity.mycourselistrequest.ConverterMyCourseRequest;
import com.cbq.yatori.core.action.canghui.entity.mycourselistrequest.MyCourseRequest;
import com.cbq.yatori.core.action.canghui.entity.mycourselistresponse.*;
import com.cbq.yatori.core.action.canghui.entity.submitstudy.ConverterSubmitStudyTime;
import com.cbq.yatori.core.action.canghui.entity.submitstudy.SubmitStudyTimeRequest;
import com.cbq.yatori.core.action.canghui.entity.upload.ConverterUpload;
import com.cbq.yatori.core.action.canghui.entity.upload.UploadRequest;
import com.cbq.yatori.core.entity.AccountCacheCangHui;
import com.cbq.yatori.core.entity.User;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CourseAction {

    /**
     * 获取我需要刷的课程列表
     */
    public static MyCourseData myCourseList(User user) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");

        MyCourseRequest courseRequest = new MyCourseRequest();
        courseRequest.setPageSize(15);
        courseRequest.setStatus(1);
        courseRequest.setPage(1);

        RequestBody body = null;
        try {
            body = RequestBody.create(mediaType, ConverterMyCourseRequest.toJsonString(courseRequest));
        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
            log.error("");
            e.printStackTrace();
        }
        Request request = new Request.Builder()
                .url(user.getUrl() + "/api/v1/course/study/my")
                .method("POST", body)
                .addHeader("Member-Token", ((AccountCacheCangHui)user.getCache()).getToken())
                .addHeader("Origin", ((AccountCacheCangHui)user.getCache()).getToken())
                .addHeader("Cookie", "SESSION=" + ((AccountCacheCangHui)user.getCache()).getSession() + ";")
                .addHeader("User-Agent", "Apifox/1.0.0 (https://www.apifox.cn)")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "*/*")
                .addHeader("Host", "kkzxsx.bwgl.cn")
                .addHeader("Connection", "keep-alive")
                .build();
        try {
            Response response = client.newCall(request).execute();
            if(!response.isSuccessful()){//当响应失败时
                response.close();
                return null;
            }
            String json = response.body().string();
            MyCourseDataRequest dataRequest = ConverterMyCourseResponse.fromJsonString(json);
            //System.out.println(jsonObject.toString());
            if (dataRequest.getCode() != 0) {
//                throw new Exception(json);
                log.error(json);
            }
            if (dataRequest.getMsg().equals("暂无数据")) {
//                System.out.println(user.getAccount() + "已刷完");
                log.info("{}已刷完",user.getAccount());
                return new MyCourseData();
            }

            return dataRequest.getData();

        } catch (SocketTimeoutException e){
            return null;
        } catch (Exception e) {
            log.error("");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取详细课程信息
     *
     * @param user
     * @param courseId
     * @return
     */
    public static CourseDetailData getCourseDetail(User user, Long courseId) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n    \"id\": \"" + courseId + "\"\r\n}");
        Request request = new Request.Builder()
                .url(user.getUrl()+ "/api/v1/home/course_detail")
                .method("POST", body)
                .addHeader("member-token", ((AccountCacheCangHui)user.getCache()).getToken())
                .addHeader("origin", user.getUrl())
                .addHeader("sec-ch-ua", "\"Not.A/Brand\";v=\"8\", \"Chromium\";v=\"114\", \"Microsoft Edge\";v=\"114\"")
                .addHeader("sec-ch-ua-platform", "\"Windows\"")
                .addHeader("User-Agent", "Apifox/1.0.0 (https://www.apifox.cn)")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "*/*")
                .addHeader("Host", "kkzxsx.bwgl.cn")
                .addHeader("Connection", "keep-alive")
                .addHeader("Cookie", ((AccountCacheCangHui)user.getCache()).getSession())
                .build();
        try {
            Response response = client.newCall(request).execute();
            if(!response.isSuccessful()){//当响应失败时
                response.close();
                return null;
            }
            String json = response.body().string();
            CourseDetailRequest courseDetailRequest = ConverterCourseDetail.fromJsonString(json);
            //System.out.println(jsonObject.toString());

            //网络繁忙
            if(courseDetailRequest.getCode()==-8000){
                return null;
            }
            //其他未知错误
            if (courseDetailRequest.getCode() != 0) {
                throw new Exception(json);
            }
            //装载
            //System.out.println(jsonObject.toString());
            return courseDetailRequest.getData();
        }catch (JsonParseException jsonParseException){
            //这种一般是访问过于频繁造成，这边延迟一一下
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return null;
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
     *
     * @param user    课程的semesterId
     * @param sectionId  视屏的sectionId，也就是id
     */
    public static SubmitStudyTimeRequest submitLearnTime(User user, MyCourse course,Long sectionId,Long studyTime) {
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\r\n    \"semesterId\": " + course.getSemesterId() + ",\r\n    \"sectionId\": \"" + sectionId + "\",\r\n    \"position\": " + studyTime + "\r\n}");
            //RequestBody body = RequestBody.create(mediaType,"");
            Request request = new Request.Builder()
                    .url(user.getUrl() + "/api/v1/course/study/upload/progress")
                    .method("POST", body)
                    .addHeader("Member-Token", ((AccountCacheCangHui)user.getCache()).getToken())
                    .addHeader("Origin", user.getUrl())
                    .addHeader("Cookie", "SESSION="+((AccountCacheCangHui)user.getCache()).getSession()+";"+
                            "Member-Token="+((AccountCacheCangHui) user.getCache()).getToken()+";"+
                            "Member-schoolId="+"0")
                    .addHeader("User-Agent", "Apifox/1.0.0 (https://www.apifox.cn)")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "*/*")
                    .addHeader("Connection", "keep-alive")
                    .build();
            Response response = client.newCall(request).execute();
            if(!response.isSuccessful()){//当响应失败时
                response.close();
                return null;
            }
            //System.out.println(response.body().string());
            String json= response.body().string();
//            UploadRequest upload= ConverterUpload.fromJsonString(json);
            SubmitStudyTimeRequest submit = ConverterSubmitStudyTime.fromJsonString(json);
//            if (submit.getCode() != 0) {
////                System.out.printf("!!!!!!%s课程!!!!!!\nid：%s\n状态：提交学时失败。\n失败原因：%s\n", user.getAccount(), detailDatum.getId(), upload.getMsg());
//                log.info("提交学时失败!!!");
//                return null;
//            }
            return submit;
//            System.out.printf("------%s课程------\nid：%s\n名称：%s\n状态：提交学时%s。当前学时：%d\n视屏总时长:%d\n", user.getAccount(), detailDatum.getId(),upload.getMsg(), detailDatum.getProgress(), detailDatum.getTotalProgress());
        } catch (SocketTimeoutException e){
            return null;
        }catch (JsonParseException jsonParseException){
            //这种一般是访问过于频繁造成，这边延迟一一下
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return null;
        } catch (IOException e) {
            log.error("");
            e.printStackTrace();
        }
        return null;
    }

}
