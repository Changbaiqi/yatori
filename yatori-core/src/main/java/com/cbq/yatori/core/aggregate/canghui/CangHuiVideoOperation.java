package com.cbq.yatori.core.aggregate.canghui;

import com.cbq.yatori.core.action.canghui.CourseAction;
import com.cbq.yatori.core.action.canghui.entity.coursedetail.Process;
import com.cbq.yatori.core.action.canghui.entity.coursedetail.Section;
import com.cbq.yatori.core.action.canghui.entity.submitstudy.SubmitStudyTimeRequest;
import com.cbq.yatori.core.entity.AccountCacheCangHui;
import com.cbq.yatori.core.entity.User;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class CangHuiVideoOperation {
    private User user;
    private Section section; //原始数据
    private long studyInterval = 5; //单次提交学习时长

    private Thread thread;

    private String title;
    private long semesterId;
    private long videoDuration =0;
    private long studyTime =0; //当前学习进度时长

    public CangHuiVideoOperation(User user,long semesterId, Section section) {
        this.user = user;
        this.semesterId = semesterId;
        this.section = section;
    }

    /**
     * 开始刷课
     */
    public void startBrushAction(){
        if(thread!=null)
            thread.notify();
    }

    /**
     * 暂停刷课
     */
    public void stopBrushAction(){
        if(thread!=null) {
            try {
                thread.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void videoAction() {
        AccountCacheCangHui cache= (AccountCacheCangHui) user.getCache();
        thread = new Thread(()->{

            //获取学习进度
            Process process = section.getProcess();
            if(process!=null){
                studyTime = process.getProgress();
            }


            videoDuration = section.getVideoDuration(); //获取视屏总时长
            title = section.getName(); //获取视屏名称

            //循环学习
            do{
                studyTime+=studyInterval;
                //提交学时
                SubmitStudyTimeRequest submitStudyTimeRequest=null;
                while ((submitStudyTimeRequest= CourseAction.submitLearnTime(user, semesterId, section.getId(), studyTime))==null)

                if (studyTime < videoDuration) {
                    try {
                        Thread.sleep(1000 * studyInterval);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }while(studyTime<videoDuration);

        });
        thread.start();

        try {
            thread.wait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }


    public static CangHuiVideoOperationBuilder builder(){
        return new CangHuiVideoOperationBuilder();
    }

    public static class CangHuiVideoOperationBuilder{
        private User user;
        private long semesterId;
        private Section section; //章节对应视屏原始信息

        public CangHuiVideoOperationBuilder user(User user){
            this.user = user;
            return  this;
        }

        public CangHuiVideoOperationBuilder semesterId(long semesterId){
            this.semesterId = semesterId;
            return this;
        }

        public CangHuiVideoOperationBuilder section(Section section){
            this.section = section;
            return this;
        }


        public CangHuiVideoOperation build(){
            return new CangHuiVideoOperation(user,semesterId,section);
        }
    }
}
