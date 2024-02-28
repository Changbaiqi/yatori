package com.cbq.yatori.core.aggregate.yinghua;

import com.cbq.yatori.core.action.yinghua.CourseAction;
import com.cbq.yatori.core.action.yinghua.entity.allvideo.NodeList;
import com.cbq.yatori.core.action.yinghua.entity.allvideo.VideoList;
import com.cbq.yatori.core.action.yinghua.entity.submitstudy.ConverterSubmitStudyTime;
import com.cbq.yatori.core.action.yinghua.entity.submitstudy.SubmitResult;
import com.cbq.yatori.core.action.yinghua.entity.submitstudy.SubmitStudyTimeRequest;
import com.cbq.yatori.core.action.yinghua.entity.videomessage.VideoInformRequest;
import com.cbq.yatori.core.action.yinghua.entity.videomessage.VideoInformStudyTotal;
import com.cbq.yatori.core.entity.AccountCacheYingHua;
import com.cbq.yatori.core.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class YingHuaVideoOperation {

//    private Long isLock; //视屏是否被锁，1代表被锁，0代表 正常
//
//    private Long videoState; //代表当前视屏的状态了，2代表当前视屏 已看完。

    private User user; //当前对应视屏用户对象
    private NodeList videoInform; //代表视屏对象原始数据

    private long studyInterval = 5;//学时提交间隔秒数

    private long studyId = 0; //当前学习ID（随时可能变化）

    private Thread thread = null; //当前刷视屏的线程

    private long videoDuration = 0; //当前视屏总时长

    private long studyTime; //当前视屏学习已学习时长

    /**
     * 用于初始化
     */
    public YingHuaVideoOperation(User user,NodeList videoInform) {
        this.user = user;
        this.videoInform = videoInform;
        if (thread == null)//初始化视屏线程任务
            videoAction();
    }


    

    /**
     * 开始刷课
     */
    public void startBrushAction() {
        if (thread != null) {
            thread.notify();
        }
    }



    /**
     * 暂停视屏刷课
     */
    public void stopBrushAction() {
        try {
            thread.wait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 视屏活动
     */
    private void videoAction() {
        AccountCacheYingHua cache = (AccountCacheYingHua) user.getCache(); //账号缓存信息

        //获取到视屏观看信息
        VideoInformRequest videMessage = null;
        while ((videMessage = CourseAction.getVideMessage(user, videoInform)) == null) ;

        //视屏总时长
        videoDuration = videMessage.getResult().getData().getVideoDuration();

        //当前学习进度
        VideoInformStudyTotal studyTotal = videMessage.getResult().getData().getStudyTotal();

        //获取视屏观看时长
        studyTime = Long.parseLong(studyTotal.getDuration());


        thread = new Thread(() -> {
            //当视屏被锁时
            if (videoInform.getNodeLock() == 0) {
                throw new RuntimeException(videoInform.getName() + "视屏被锁");
            }

            //如果此视屏看完了则直接跳过
            if (videoInform.getVideoState() == 2) {
                return;
            }


            log.info("正在学习视屏：{}", videoInform.getName());

            //开始看视屏---------------
            //循环进行学习
            do {
                studyTime += studyInterval;//增加学时

                //提交学时
                SubmitStudyTimeRequest submitStudyTimeRequest = null;
                while ((submitStudyTimeRequest = CourseAction.submitStudyTime(user, videoInform, studyTime, studyId)) == null)
                    ;
                //成功提交
                SubmitResult result = submitStudyTimeRequest.getResult();
                //根据反馈内容修改studyId
                if (result != null)
                    if (result.getData() != null)
                        studyId = result.getData() != null ? result.getData().getStudyId() : studyId;

                try {
                    log.info("\n服务器端信息：>>>{}\n学习账号>>>{}\n学习平台>>>{}\n视屏名称>>>{}\n视屏总长度>>>{}\n当前学时>>>{}",
                            ConverterSubmitStudyTime.toJsonString(submitStudyTimeRequest),
                            user.getAccount(),
                            user.getAccountType().name(),
                            videoInform.getName(),
                            videoDuration,
                            studyTime);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }


                //延时指定秒数秒
                if (studyTime < videoDuration) {
                    try {
                        Thread.sleep(1000 * studyInterval);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

            } while (studyTime < videoDuration);
            log.info("视屏：{}学习完毕！！！", videoInform.getName());
        });
        thread.start();
        try {
            thread.wait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 获取视屏进度
     * @return
     */
    public double getProgress(){
        return (double) studyTime/videoDuration;
    }


    public static YingHuaVideoOperationBuilder builder() {
        return new YingHuaVideoOperationBuilder();
    }

    public static class YingHuaVideoOperationBuilder {

        private User user;
        private NodeList node; //识破节点原始数据


        public YingHuaVideoOperationBuilder user(User user){
            this.user = user;
            return this;
        }

        public YingHuaVideoOperationBuilder node(NodeList node) {
            this.node = node;
            return this;
        }


        public YingHuaVideoOperation build() {
            return new YingHuaVideoOperation(user,node);
        }


    }

}











/*老版本提交学时代码*/

/*
            while ((studyTime += studyInterval) < videoDuration + studyInterval) {//如果学习总时长超过了视屏总时长那么就跳过
                //这里根据账号账号登录状态进行策划行为
                switch (cache.getStatus()) {//未登录则跳出
                    case 0 -> {//如果账号未登录

                        log.info("账号未登录，禁止刷课！");
//                        return;
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
                        if (result != null)
                            if (result.getData() != null)
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
                    if (submitStudyTimeRequest == null)
                        studyTime -= studyInterval;
                }
            }
* */