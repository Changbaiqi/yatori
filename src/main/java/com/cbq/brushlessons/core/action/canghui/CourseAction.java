package com.cbq.brushlessons.core.action.canghui;

import com.cbq.brushlessons.core.action.canghui.entity.coursedetail.ConverterCourseDetail;
import com.cbq.brushlessons.core.action.canghui.entity.coursedetail.CourseDetailRequest;
import com.cbq.brushlessons.core.action.canghui.entity.mycourselistrequest.ConverterMyCourseRequest;
import com.cbq.brushlessons.core.action.canghui.entity.mycourselistrequest.MyCourseRequest;
import com.cbq.brushlessons.core.action.canghui.entity.mycourselistresponse.ConverterMyCourseResponse;
import com.cbq.brushlessons.core.action.canghui.entity.mycourselistresponse.MyCourse;
import com.cbq.brushlessons.core.action.canghui.entity.mycourselistresponse.MyCourseData;
import com.cbq.brushlessons.core.action.canghui.entity.mycourselistresponse.MyCourseDataRequest;
import com.cbq.brushlessons.core.entity.AccountCacheCangHui;
import com.cbq.brushlessons.core.entity.User;
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
            throw new RuntimeException(e);
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

            String json = response.body().string();
            MyCourseDataRequest dataRequest = ConverterMyCourseResponse.fromJsonString(json);
            //System.out.println(jsonObject.toString());
            if (dataRequest.getCode() != 0) {
                throw new Exception(json);
            }
            if (dataRequest.getMsg().equals("暂无数据")) {
//                System.out.println(user.getAccount() + "已刷完");
                log.info("{}已刷完",user.getAccount());
                return new MyCourseData();
            }

//            JSONObject data = jsonObject.getJSONObject("data");
//            JSONArray lists = data.getJSONArray("lists");
//            //用于寄存详细课程内容
//            JSONArray courseDetailsArray = new JSONArray();
//            for (int i = 0; i < lists.size(); ++i) {
//                JSONObject resCourse = lists.getJSONObject(i);
//                Long courseId = resCourse.getLong("courseId");
//                //将相应的详细课表json加入列表中
//                courseDetailsArray.add(getCourseDetail(user, courseId));
//            }
            //装载
            //System.out.println(jsonObject.toString());
            return dataRequest.getData();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取详细课程信息
     *
     * @param user
     * @param courseId
     * @return
     */
    public static JSONObject getCourseDetail(User user, Long courseId) {
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
            String json = response.body().string();
            CourseDetailRequest courseDetailRequest = ConverterCourseDetail.fromJsonString(json);
            //System.out.println(jsonObject.toString());
            if (courseDetailRequest.getCode() != 0) {
                throw new Exception(json);
            }

            //装载
            //System.out.println(jsonObject.toString());
            return courseDetailRequest.getData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 提交学时
     *
     * @param user    课程的semesterId
     * @param course  课程的sectionId
     * @param section 课程需要提交的学时（单位s)
     */
    public static void submitLearnTime(User user, MyCourse course, Section section) {
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\r\n    \"semesterId\": " + course.getSemesterId() + ",\r\n    \"sectionId\": \"" + section.getId() + "\",\r\n    \"position\": " + section.getProgress() + "\r\n}");
            Request request = new Request.Builder()
                    .url(user.getUrl() + "/api/v1/course/study/upload/progress")
                    .method("POST", body)
                    .addHeader("Member-Token", ((AccountCacheCangHui)user.getCache()).getToken())
                    .addHeader("Origin", user.getUrl())
                    .addHeader("Cookie", ((AccountCacheCangHui)user.getCache()).getSession())
                    .addHeader("User-Agent", "Apifox/1.0.0 (https://www.apifox.cn)")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "*/*")
                    .addHeader("Connection", "keep-alive")
                    .build();
            Response response = client.newCall(request).execute();
            //System.out.println(response.body().string());
            JSONObject jsonObject = JSONObject.parseObject(response.body().string());
            if (jsonObject.getInteger("code") != 0) {
                System.out.printf("!!!!!!%s课程!!!!!!\nid：%s\n名称：%s\n状态：提交学时失败。\n失败原因：%s\n", user.getAccount(), section.getId(), section.getName(), jsonObject.getString("msg"));
                return;
            }
            System.out.printf("------%s课程------\nid：%s\n名称：%s\n状态：提交学时%s。当前学时：%d\n视屏总时长:%d\n", user.getAccount(), section.getId(), section.getName(), jsonObject.getString("msg"), section.getProgress(), section.getTotalProgress());
        } catch (SocketTimeoutException e){
            System.out.println("有一个提交请求连接超时。");
        } catch (IOException e) {
            System.out.println("检测到未知报错信息：");
            throw new RuntimeException(e);
        }
    }


    /**
     * 统计刷课量
     *
     * @param myCourse
     * @return
     */
//    public static int calcSections(MyCourseData myCourse) {
//        int ans = 0;
//        List<MyCourse> courseArrayList = myCourse.getLists();
//        for (int i = 0; i < courseArrayList.size(); ++i) {
//            if (courseArrayList.get(i).getSw().compareTo(Boolean.FALSE) == 0)
//                continue;
//            ArrayList<Section> sections = courseArrayList.get(i).getSections();
//            ans += sections.size();
//        }
//        return ans;
//    }


}
