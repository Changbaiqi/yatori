package com.cbq.brushlessons.core.action.yinghua;


import com.cbq.brushlessons.core.action.yinghua.entity.allcourse.CourseInform;
import com.cbq.brushlessons.core.action.yinghua.entity.allvideo.NodeList;
import com.cbq.brushlessons.core.action.yinghua.entity.allvideo.VideoList;
import com.cbq.brushlessons.core.action.yinghua.entity.allvideo.VideoRequest;
import com.cbq.brushlessons.core.action.yinghua.entity.submitstudy.SubmitData;
import com.cbq.brushlessons.core.action.yinghua.entity.submitstudy.SubmitStudyTimeRequest;
import com.cbq.brushlessons.core.action.yinghua.entity.videomessage.VideoInformStudyTotal;
import com.cbq.brushlessons.core.action.yinghua.entity.videomessage.VideoInformRequest;
import com.cbq.brushlessons.core.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Slf4j
public class CourseStudyAction {

    private User user;
    private CourseInform courseInform;

    private VideoRequest courseVideosList;
    //需要看的视屏集合
    private List<NodeList> videoInforms=new ArrayList<>();
    //学习Id
    private long studyId=0;

    public void toStudy(){
        for (int i = 0; i < videoInforms.size(); i++) {
            NodeList videoInform = videoInforms.get(i);
            //当视屏没有被锁时
            if(videoInform.getNodeLock()==0){
                //如果此视屏看完了则直接跳过
                if (videoInform.getVideoState()==2)
                    continue;
                //获取到视屏观看信息
                VideoInformRequest videMessage = CourseAction.getVideMessage(user, videoInform);
                //视屏总时长
                long videoDuration = videMessage.getResult().getData().getVideoDuration();
                //当前学习进度
                VideoInformStudyTotal studyTotal = videMessage.getResult().getData().getStudyTotal();
                //如果学习总时长超过了视屏总时长那么就跳过
                log.info("正在学习视屏：{}",videoInform.getName());
                //开始看视屏---------------
                long studyTime= Long.parseLong(studyTotal.getDuration());


                //循环进行学习
                while((studyTime+=8)<=videoDuration){
                    SubmitStudyTimeRequest submitStudyTimeRequest = CourseAction.submitStudyTime(user, videoInform, studyTime, studyId);
                    SubmitData data = submitStudyTimeRequest.getResult().getData();
                    studyId=data!=null?data.getStudyId():0;
                    System.out.println(submitStudyTimeRequest);

                    //延时8秒
                    try {
                        Thread.sleep(8000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    if(studyTime>=videoDuration){update();}
                }

            }
        }
    }


    private void update(){
        //初始化视屏列表
        courseVideosList = CourseAction.getCourseVideosList(user, courseInform);
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


    public static Builder builder(){
        return new Builder();
    }

    public static class Builder{
        private CourseStudyAction courseStudyAction=new CourseStudyAction();

        public Builder user(User user){
            courseStudyAction.user=user;
            return this;
        }
        public Builder courseInform(CourseInform courseInform){
            courseStudyAction.courseInform=courseInform;
            return this;
        }
        public CourseStudyAction build(){
            //初始化视屏列表
            courseStudyAction.courseVideosList = CourseAction.getCourseVideosList(courseStudyAction.user, courseStudyAction.courseInform);
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
