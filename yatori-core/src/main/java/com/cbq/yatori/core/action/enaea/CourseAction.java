package com.cbq.yatori.core.action.enaea;

import com.cbq.yatori.core.action.enaea.entity.ccvideo.CCVideoConverter;
import com.cbq.yatori.core.action.enaea.entity.ccvideo.CCVideoRequest;
import com.cbq.yatori.core.action.enaea.entity.coursevidelist.CourseVideListConverter;
import com.cbq.yatori.core.action.enaea.entity.coursevidelist.CourseVideoListRequest;
import com.cbq.yatori.core.action.enaea.entity.requirecourselist.RequiredCourseListConverter;
import com.cbq.yatori.core.action.enaea.entity.requirecourselist.RequiredCourseListRequest;
import com.cbq.yatori.core.action.enaea.entity.submitlearntime.SubmitLearnTimeConverter;
import com.cbq.yatori.core.action.enaea.entity.submitlearntime.SubmitLearnTimeRequest;
import com.cbq.yatori.core.action.enaea.entity.underwayproject.UnderwayProjectConverter;
import com.cbq.yatori.core.action.enaea.entity.underwayproject.UnderwayProjectRquest;
import com.cbq.yatori.core.entity.AccountCacheEnaea;
import com.cbq.yatori.core.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CourseAction {

    /**
     * 获取正在进行的课程项目
     * @param user 用户
     * @return
     */
    public static UnderwayProjectRquest getUnderwayProject(User user){
        AccountCacheEnaea cache = (AccountCacheEnaea)user.getCache();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        Request request = new Request.Builder()
                .url("https://study.enaea.edu.cn/assessment.do?action=getMyCircleCourses&start=0&limit=200&isFinished=false&_="+System.currentTimeMillis())
                .method("GET", null)
                .addHeader("Cookie", "ASUSS="+cache.getASUSS()+";")
                .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .addHeader("Accept", "*/*")
                .addHeader("Host", "study.enaea.edu.cn")
                .addHeader("Connection", "keep-alive")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            UnderwayProjectRquest underwayProjectRquest = UnderwayProjectConverter.fromJsonString(string);
            return underwayProjectRquest;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 用于获取必修课程的syllabusId
     * @param user 用户对象
     * @param circleId 对应circledId
     * @return
     */
    public static String getRequiredCoursesSyllabusId(User user,String circleId){
        AccountCacheEnaea cache =(AccountCacheEnaea) user.getCache();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        Request request = new Request.Builder()
                .url("https://study.enaea.edu.cn/circleIndexRedirect.do?action=toCircleIndex&circleId="+circleId+"&ct="+System.currentTimeMillis())
                .method("GET", null)
                .addHeader("Cookie", "ASUSS="+cache.getASUSS()+";")
                .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .addHeader("Accept", "*/*")
                .addHeader("Host", "study.enaea.edu.cn")
                .addHeader("Connection", "keep-alive")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            Pattern pattern = Pattern.compile("<a title=\"必修课程\" href=\"circleIndexRedirect.do\\?action=toNewMyClass&type=course&circleId="+circleId+"&syllabusId=([^&]*?)&isRequired=true&studentProgress=[\\d]+\">必修课程</a>");
            Matcher matcher = pattern.matcher(string);
            if(matcher.find()){
                return matcher.group(1);
            }
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 获取对应必修项目的必修课程
     * @param user 用户
     * @param circleId 项目ID
     */
    public static RequiredCourseListRequest getRequiredCourseList(User user,String circleId){
        String syllabusId = getRequiredCoursesSyllabusId(user, circleId); //获取syllabusId
        AccountCacheEnaea cache =(AccountCacheEnaea) user.getCache();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        Request request = new Request.Builder()
                .url("https://study.enaea.edu.cn/circleIndex.do?action=getMyClass&start=0&limit=200&isCompleted=&circleId="+circleId+"&syllabusId="+syllabusId+"&categoryRemark=all&_="+System.currentTimeMillis())
                .method("GET", null)
                .addHeader("Cookie", "ASUSS="+cache.getASUSS()+";")
                .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .addHeader("Accept", "*/*")
                .addHeader("Host", "study.enaea.edu.cn")
                .addHeader("Connection", "keep-alive")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            RequiredCourseListRequest requiredCourseListRequest = RequiredCourseListConverter.fromJsonString(string);
            return requiredCourseListRequest;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 获取对应课程的视屏列表
     * @param user 用户对象
     * @param circledId 项目ID
     * @param courseId 课程ID
     */
    public static CourseVideoListRequest getCourseVideList(User user, String circledId, String courseId){
        AccountCacheEnaea cache = (AccountCacheEnaea)user.getCache();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
//        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://study.enaea.edu.cn/course.do?action=getCourseContentList&courseId="+courseId+"&circleId="+circledId+"&_="+System.currentTimeMillis())
                .method("GET", null)
                .addHeader("Cookie", "ASUSS="+cache.getASUSS()+";")
                .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .addHeader("Accept", "*/*")
                .addHeader("Host", "study.enaea.edu.cn")
                .addHeader("Connection", "keep-alive")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            CourseVideoListRequest courseVideoListRequest = CourseVideListConverter.fromJsonString(string);
            return courseVideoListRequest;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 在观看视屏前一定要先调用这个函数，相当于告诉后端，我要看这个视屏了，请对这个视屏开始计时
     * @param user 用户
     * @param courseId 课程Id
     * @param coursecontentId 当前视屏ID
     * @param circleId 项目ID
     * @return
     */
    public static CCVideoRequest statisticForCCVideo(User user, Long courseId, Long coursecontentId, Long circleId){
        AccountCacheEnaea cache = (AccountCacheEnaea) user.getCache();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
//        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://study.enaea.edu.cn/course.do?action=statisticForCCVideo&courseId="+courseId+"&coursecontentId="+coursecontentId+"&circleId="+circleId+"&_=1703512855563")
                .method("GET", null)
                .addHeader("Cookie", "ASUSS="+cache.getASUSS()+";")
                .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .addHeader("Accept", "*/*")
                .addHeader("Host", "study.enaea.edu.cn")
                .addHeader("Connection", "keep-alive")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            CCVideoRequest ccVideoRequest = CCVideoConverter.fromJsonString(string);
            List<String> headers = response.headers("Set-Cookie");
            for (String header : headers) {
                if (header.startsWith("SCFUCKP")) {
                    String scfuckp = header.split(";")[0];
                    String key = scfuckp.split("=")[0];
                    String val = scfuckp.split("=")[1];
                    ccVideoRequest.setSCFUCKPKey(key);
                    ccVideoRequest.setSCFUCKPValue(val);
                }
            }
            return ccVideoRequest;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 提交学时
     * @param user 用户
     * @param SCFUCKPKey Cookie的key 通过statisticForCCVideo方法获取
     * @param SCFUCKPValue Cookie的value 通过statisticForCCVideo方法获取
     * @param circleId 项目ID
     * @param id 视屏ID
     * @param time 提交的学时，这里注意，要每隔1分钟提交一次，不能快,这个time直接填当前毫秒时间戳即可
     * @return 返回求结果
     */
    public static SubmitLearnTimeRequest submitLearnTime(User user,String SCFUCKPKey,String SCFUCKPValue,String circleId,String id,Long time){
        AccountCacheEnaea cache = (AccountCacheEnaea)user.getCache();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "id="+id+"&circleId="+circleId+"&ct="+time+"&finish=false");
        Request request = new Request.Builder()
                .url("https://study.enaea.edu.cn/studyLog.do")
                .method("POST", body)
                .addHeader("Cookie", "ASUSS="+cache.getASUSS()+";"+SCFUCKPKey+"="+SCFUCKPValue)
                .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .addHeader("Accept", "*/*")
                .addHeader("Host", "study.enaea.edu.cn")
                .addHeader("Connection", "keep-alive")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            SubmitLearnTimeRequest submitLearnTimeRequest = SubmitLearnTimeConverter.fromJsonString(string);
            return submitLearnTimeRequest;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
