package com.cbq.brushlessons.core.action.enaea;

import com.cbq.brushlessons.core.action.enaea.entity.underwayproject.UnderwayProjectConverter;
import com.cbq.brushlessons.core.action.enaea.entity.underwayproject.UnderwayProjectRquest;
import com.cbq.brushlessons.core.entity.AccountCacheEnaea;
import com.cbq.brushlessons.core.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;

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
     * 获取对应必修项目的必修课程
     * @param user 用户
     * @param circleId 项目ID
     */
    public static void requiredCourseList(User user,String circleId){
        AccountCacheEnaea cache =(AccountCacheEnaea) user.getCache();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        Request request = new Request.Builder()
                .url("https://study.enaea.edu.cn/circleIndex.do?action=getMyClass&start=0&limit=200&isCompleted=&circleId="+circleId+"&syllabusId=1328803&categoryRemark=all&_="+System.currentTimeMillis())
                .method("GET", null)
                .addHeader("Cookie", "ASUSS="+cache.getASUSS()+";")
                .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .addHeader("Accept", "*/*")
                .addHeader("Host", "study.enaea.edu.cn")
                .addHeader("Connection", "keep-alive")
                .build();
        try {
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
