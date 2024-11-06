package com.cbq.yatori.core.action.xuexitong;

import com.cbq.yatori.core.action.canghui.entity.mycourselistrequest.ConverterMyCourseRequest;
import com.cbq.yatori.core.action.canghui.entity.mycourselistrequest.MyCourseRequest;
import com.cbq.yatori.core.action.canghui.entity.mycourselistresponse.MyCourseData;
import com.cbq.yatori.core.entity.AccountCacheXueXiTong;
import com.cbq.yatori.core.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.net.SocketTimeoutException;

@Slf4j
public class CourseAction {


    /**
     * 获取需要学习的课程列表
     * @param user
     * @return
     */
    public static Object myCourseList(User user) {
        AccountCacheXueXiTong cache = (AccountCacheXueXiTong) user.getCache();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");


        Request request = new Request.Builder()
                .url("https://mooc1-api.chaoxing.com/mycourse/backclazzdata")
                .addHeader("Cookie", cache.getSession())
                .addHeader("User-Agent", "Apifox/1.0.0 (https://www.apifox.cn)")
                .build();
        try {
            Response response = client.newCall(request).execute();
            if(!response.isSuccessful()){//当响应失败时
                response.close();
                return null;
            }
            String json = response.body().string();
            log.info(json);

        } catch (SocketTimeoutException e){
            return null;
        } catch (Exception e) {
            log.error("");
            e.printStackTrace();
        }
        return null;
    }
}
