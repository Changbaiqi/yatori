package com.cbq.brushlessons.core.action.yinghua;


import com.cbq.brushlessons.core.action.yinghua.entity.allcourse.CourseInform;
import com.cbq.brushlessons.core.action.yinghua.entity.allvideo.NodeList;
import com.cbq.brushlessons.core.action.yinghua.entity.allvideo.VideoList;
import com.cbq.brushlessons.core.action.yinghua.entity.allvideo.VideoRequest;
import com.cbq.brushlessons.core.action.yinghua.entity.submitstudy.ConverterSubmitStudyTime;
import com.cbq.brushlessons.core.action.yinghua.entity.submitstudy.SubmitData;
import com.cbq.brushlessons.core.action.yinghua.entity.submitstudy.SubmitResult;
import com.cbq.brushlessons.core.action.yinghua.entity.submitstudy.SubmitStudyTimeRequest;
import com.cbq.brushlessons.core.action.yinghua.entity.videomessage.VideoInformStudyTotal;
import com.cbq.brushlessons.core.action.yinghua.entity.videomessage.VideoInformRequest;
import com.cbq.brushlessons.core.entity.AccountCacheYingHua;
import com.cbq.brushlessons.core.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 长白崎
 * @version 1.0
 * @description: TODO
 * @date 2023/11/2 14:34
 */
@Slf4j
public class CourseStudyAction implements Runnable {

    private User user;
    private CourseInform courseInform;

    private VideoRequest courseVideosList;
    //需要看的视屏集合
    private List<NodeList> videoInforms = new ArrayList<>();
    //学习Id
    private long studyId = 0;
    private Boolean newThread = false;

    private long studyInterval = 5;

    public void toStudy() {
        if (newThread) {
            new Thread(this).start();
        } else {
            log.info("{}:正在学习课程>>>{}", user.getAccount(), courseInform.getName());
            study();
            log.info("{}:{}学习完毕！", user.getAccount(), courseInform.getName());
        }
    }

    @Override
    public void run() {
        log.info("{}:正在学习课程>>>{}", user.getAccount(), courseInform.getName());
        study();
        log.info("{}:{}学习完毕！", user.getAccount(), courseInform.getName());
    }

    private void study() {
        AccountCacheYingHua cache = (AccountCacheYingHua) user.getCache();
        for (int i = 0; i < videoInforms.size(); i++) {
            NodeList videoInform = videoInforms.get(i);
            //当视屏没有被锁时
            if (videoInform.getNodeLock() == 0) {
                //如果此视屏看完了则直接跳过
                if (videoInform.getVideoState() == 2)
                    continue;
                //获取到视屏观看信息
                VideoInformRequest videMessage = null;
                while ((videMessage = CourseAction.getVideMessage(user, videoInform)) == null) ;

                //视屏总时长
                long videoDuration = videMessage.getResult().getData().getVideoDuration();
                //当前学习进度
                VideoInformStudyTotal studyTotal = videMessage.getResult().getData().getStudyTotal();
                //如果学习总时长超过了视屏总时长那么就跳过
                log.info("正在学习视屏：{}", videoInform.getName());
                //开始看视屏---------------
                long studyTime = Long.parseLong(studyTotal.getDuration());


                //循环进行学习
                while ((studyTime += studyInterval) < videoDuration + studyInterval) {
                    //这里根据账号账号登录状态进行策划行为
                    switch (cache.getStatus()) {//未登录则跳出
                        case 0 -> {
                            log.info("账号未登录，禁止刷课！");
                            return;
                        }
                        case 2 -> {//如果登录超时，则堵塞等待
                            studyTime -= studyInterval;
                            continue;
                        }
                    }

                    SubmitStudyTimeRequest submitStudyTimeRequest = CourseAction.submitStudyTime(user, videoInform, studyTime, studyId);
                    try {
                        //如果未成功提交
                        if (submitStudyTimeRequest != null) {
                            //检测是否登录超时
                            if (submitStudyTimeRequest.getMsg().contains("登录超时")) {
                                cache.setStatus(2);
                                studyTime -= studyInterval;
                                continue;
                            }
                            //成功提交
                            SubmitResult result = submitStudyTimeRequest.getResult();
                            //根据反馈内容修改studyId
                            if(result!=null)
                                if(result.getData()!=null)
                                    studyId = result.getData() != null ? result.getData().getStudyId() : studyId;


                            log.info("\n服务器端信息：>>>{}\n学习账号>>>{}\n学习平台>>>{}\n视屏名称>>>{}\n视屏总长度>>>{}\n当前学时>>>{}",
                                    ConverterSubmitStudyTime.toJsonString(submitStudyTimeRequest),
                                    user.getAccount(),
                                    user.getAccountType().name(),
                                    videoInform.getName(),
                                    videoDuration,
                                    studyTime);
                        }

                        //延时8秒
                        if (studyTime < videoDuration) {
                            Thread.sleep(1000 * studyInterval);
                        }
                    } catch (JsonProcessingException e) {
                        log.error("");
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        log.error("");
                        e.printStackTrace();
                    }
                    //更新视屏信息列表
                    if (studyTime >= videoDuration) {
                        if(submitStudyTimeRequest==null)
                            studyTime-=studyInterval;
                        else
                            update();
                    }
                }

            }
        }
    }


    private void update() {
        //初始化视屏列表
        while ((courseVideosList = CourseAction.getCourseVideosList(user, courseInform)) == null) ;
        //章节
        List<VideoList> zList = courseVideosList.getResult().getList();
        //将所有视屏都加入到集合里面
        videoInforms.clear();
        for (VideoList videoList : zList) {
            for (NodeList videoInform : videoList.getNodeList()) {
                videoInforms.add(videoInform);
            }
        }
    }


    public static Builder builder() {
        return new Builder();
    }


    public static class Builder {
        private CourseStudyAction courseStudyAction = new CourseStudyAction();

        public Builder user(User user) {
            courseStudyAction.user = user;
            return this;
        }

        public Builder courseInform(CourseInform courseInform) {
            courseStudyAction.courseInform = courseInform;
            return this;
        }

        public Builder newThread(Boolean newThread) {
            courseStudyAction.newThread = newThread;
            return this;
        }

        public CourseStudyAction build() {
            //初始化视屏列表
            courseStudyAction.courseVideosList = null;
            while ((courseStudyAction.courseVideosList = CourseAction.getCourseVideosList(courseStudyAction.user, courseStudyAction.courseInform)) == null) ;

            //章节
            List<VideoList> zList = courseStudyAction.courseVideosList.getResult().getList();
            //将所有视屏都加入到集合里面
            for (VideoList videoList : zList) {
                for (NodeList videoInform : videoList.getNodeList()) {
                    courseStudyAction.videoInforms.add(videoInform);
                }
            }

            return courseStudyAction;
        }
    }
}
