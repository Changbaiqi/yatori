package com.cbq.yatori.core.action.yinghua;


import com.cbq.yatori.core.action.yinghua.entity.allcourse.CourseInform;
import com.cbq.yatori.core.action.yinghua.entity.allvideo.NodeList;
import com.cbq.yatori.core.action.yinghua.entity.allvideo.VideoList;
import com.cbq.yatori.core.action.yinghua.entity.allvideo.VideoRequest;
import com.cbq.yatori.core.action.yinghua.entity.examinform.ExamInformRequest;
import com.cbq.yatori.core.action.yinghua.entity.examinform.ListElement;
import com.cbq.yatori.core.action.yinghua.entity.examstart.ConverterStartExam;
import com.cbq.yatori.core.action.yinghua.entity.examstart.StartExamRequest;
import com.cbq.yatori.core.action.yinghua.entity.examtopic.ExamTopic;
import com.cbq.yatori.core.action.yinghua.entity.examtopic.ExamTopics;
import com.cbq.yatori.core.action.yinghua.entity.submitstudy.ConverterSubmitStudyTime;
import com.cbq.yatori.core.action.yinghua.entity.submitstudy.SubmitResult;
import com.cbq.yatori.core.action.yinghua.entity.submitstudy.SubmitStudyTimeRequest;
import com.cbq.yatori.core.action.yinghua.entity.videomessage.ConverterVideoMessage;
import com.cbq.yatori.core.action.yinghua.entity.videomessage.VideoInformStudyTotal;
import com.cbq.yatori.core.action.yinghua.entity.videomessage.VideoInformRequest;
import com.cbq.yatori.core.entity.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import com.cbq.yatori.core.utils.EmailUtil;

import javax.mail.MessagingException;
import java.util.*;

/**
 * @author 长白崎
 * @version 1.0
 * @description: TODO 各位改这里代码的时候一定要谨慎，这里代码一动很容易出BUG，所以不推荐对此处的代码进行优化。这坨代码已经是屎山了，等之后的重构版本吧。
 * @date 2023/11/2 14:34
 */
@Slf4j
public class CourseStudyAction implements Runnable {


    private User user;
    private Setting setting;
    private List<Topic> topics = new ArrayList<>(); //题库
    private HashMap<String, Integer> topicMd5 = new HashMap<>(); //题库md5映射

    private CourseInform courseInform; //当前课程的对象

    private VideoRequest courseVideosList; //视屏列表
    //需要看的视屏集合
    private List<NodeList> videoInforms = new ArrayList<>();

    private Boolean newThread = false;


    //    private static final Boolean IsOpenmail =ConfigUtils.loadingConfig().getSetting().getEmailInform().getEmail()!="";
    private long studyInterval = 5;

    private Long accoVideo = 0L;

    public void toStudy() {
        CoursesCustom coursesCustom = user.getCoursesCustom();

        //视屏刷课模式
        switch (coursesCustom.getVideoModel()) {
            case 0 -> {
//                accoVideo = (long) videoInforms.size();
                if (newThread) {
                    new Thread(this).start();
                } else {
                    log.info("{}:正在学习课程>>>{}", user.getAccount(), courseInform.getName());
                    study1();
                    log.info("{}:{}学习完毕！", user.getAccount(), courseInform.getName());
                }
            }
            //普通模式
            case 1 -> {
                if (newThread) {
                    new Thread(this).start();
                } else {
                    log.info("{}:正在学习课程>>>{}", user.getAccount(), courseInform.getName());
                    study1();
                    log.info("{}:{}学习完毕！", user.getAccount(), courseInform.getName());
                }
            }
            //暴力模式
            case 2 -> {
                log.info("{}:正在学习课程>>>{}", user.getAccount(), courseInform.getName());
                study2();
            }
        }

    }

    @Override
    public void run() {
        log.info("{}:正在学习课程>>>{}", user.getAccount(), courseInform.getName());
        study1();
        if (setting.getEmailInform().getSw() == 1) {
            try {
                EmailUtil.sendEmail(user.getAccount(), courseInform.getName());
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }

        log.info("{}:{}学习完毕！", user.getAccount(), courseInform.getName());
    }

    private void study1() {
        //学习Id
        long studyId = 0;
        AccountCacheYingHua cache = (AccountCacheYingHua) user.getCache();
        for (int i = 0; i < videoInforms.size(); i++) {
            NodeList videoInform = videoInforms.get(i);
            //当视屏没有被锁时
            if (videoInform.getNodeLock() == 2) {
                log.info("服务器端信息：>>>{}课程视屏未解锁，解锁时间为：{}", videoInform.getName(), videoInform.getUnlockTime());
            }
            if (videoInform.getNodeLock() == 0) {
                //如果此视屏看完了则直接跳过
                if (videoInform.getVideoState() == 2) {
                    addAcco();
                    continue;
                }
                //获取到视屏观看信息
                VideoInformRequest videMessage = null;
                while ((videMessage = CourseAction.getVideMessage(user, videoInform)) == null) ;
                //若视屏未到达开学时间则直接跳过
                if (videMessage.getCode() == 9 || videMessage.getMsg().contains("未开始")) {
                    try {
                        log.info("\n服务器端信息：>>>{}\n学习账号>>>{}\n学习平台>>>{}\n视屏名称>>>{}",
                                ConverterVideoMessage.toJsonString(videMessage),
                                user.getAccount(),
                                user.getAccountType().name(),
                                videoInform.getName());
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    continue;
                }
            if (videMessage.getCode() == 9 && videMessage.getMsg().contains("课程已经结束")) {
                try {
                    log.info("\n服务器端信息：>>>{}\n学习账号>>>{}\n学习平台>>>{}\n视屏名称>>>{}",
                            ConverterVideoMessage.toJsonString(videMessage),
                            user.getAccount(),
                            user.getAccountType().name(),
                            videoInform.getName());
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                continue;
            }


            //如果videoId为空那么就是考试的，直接自动考试即可
            if (videMessage.getResult().getData().getVideoId().equals("") && user.getCoursesCustom().getAutoExam() == 1) {
                ExamInformRequest exam = com.cbq.yatori.core.action.yinghua.ExamAction.getExam(user, String.valueOf(videoInform.getId()));
                //过滤误判的考试或者视屏
                if (exam.getExamInformResult().getList().size() != 0) {
                    if (exam.getExamInformResult().getList().size() != 0) {
                        for (ListElement listElement : exam.getExamInformResult().getList()) {
                            autoExamAction(courseInform, videoInform, String.valueOf(videoInform.getId()), String.valueOf(listElement.getId()));
                        }
                    }
                    continue;
                }
                exam = com.cbq.yatori.core.action.yinghua.ExamAction.getWork(user, String.valueOf(videoInform.getId()));
                if (exam.getExamInformResult().getList().size() != 0) {
                    for (ListElement listElement : exam.getExamInformResult().getList()) {
                        autoWorkAction(courseInform, videoInform, String.valueOf(videoInform.getId()), String.valueOf(listElement.getId()));
                    }
                    continue;
                }
            }
            if (user.getCoursesCustom().getVideoModel() == 0) continue; //如果模式不播放视频那么直接跳过

            //视屏总时长
            long videoDuration = videMessage.getResult().getData().getVideoDuration();
            //当前学习进度
            VideoInformStudyTotal studyTotal = videMessage.getResult().getData().getStudyTotal();
            //如果学习总时长超过了视屏总时长那么就跳过
            log.info("正在学习视屏：{}", videoInform.getName());
            //开始看视屏---------------
            long studyTime = Long.parseLong(studyTotal.getDuration());

            //直接先提交一次，这里是为了防止傻逼英华TM自己传的参数都搞错整成了视屏时长为0的问题（傻逼英华，连个参数都传错了）
            CourseAction.submitStudyTime(user, videoInform, studyTime + studyInterval, studyId);
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
                    else
                        update();
                }
            }
        }
        addAcco();
    }
}


private void study2() {
    AccountCacheYingHua cache = (AccountCacheYingHua) user.getCache();
    for (int i = 0; i < videoInforms.size(); i++) {
        NodeList videoInform = videoInforms.get(i);
        //当视屏没有被锁时
        new Thread(() -> {
            //学习Id
            long studyId = 0;
            //当视屏没有被锁时
            if (videoInform.getNodeLock() == 2) {
                log.info("服务器端信息：>>>{}课程视屏未解锁，解锁时间为：{}", videoInform.getName(), videoInform.getUnlockTime());
            }
            if (videoInform.getNodeLock() == 0) {
                //如果此视屏看完了则直接跳过
                if (videoInform.getVideoState() == 2) {
                    addAcco();
                    return;
                }
                //获取到视屏观看信息
                VideoInformRequest videMessage = null;
                while ((videMessage = CourseAction.getVideMessage(user, videoInform)) == null) ;

                if (videMessage.getCode() == 9 && videMessage.getMsg().contains("课程已经结束")) {
                    try {
                        log.info("\n服务器端信息：>>>{}\n学习账号>>>{}\n学习平台>>>{}\n视屏名称>>>{}",
                                ConverterVideoMessage.toJsonString(videMessage),
                                user.getAccount(),
                                user.getAccountType().name(),
                                videoInform.getName());
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    return;
                }


                //如果videoId为空那么就是考试的，直接自动考试即可
                if (videMessage.getResult().getData().getVideoId().equals("") && user.getCoursesCustom().getAutoExam() == 1) {
                    ExamInformRequest exam = ExamAction.getExam(user, String.valueOf(videoInform.getId()));
                    //过滤误判的考试或者视屏
                    if (exam.getExamInformResult().getList().size() != 0) {
                        for (ListElement listElement : exam.getExamInformResult().getList()) {
                            autoExamAction(courseInform, videoInform, String.valueOf(videoInform.getId()), String.valueOf(listElement.getId()));
                        }
                        return;
                    }
                    exam = ExamAction.getWork(user, String.valueOf(videoInform.getId()));
                    if (exam.getExamInformResult().getList().size() != 0) {
                        for (ListElement listElement : exam.getExamInformResult().getList()) {
                            autoWorkAction(courseInform, videoInform, String.valueOf(videoInform.getId()), String.valueOf(listElement.getId()));
                        }
                        return;
                    }
                }
                if (user.getCoursesCustom().getVideoModel() == 0) return; //如果模式不播放视频那么直接跳过

                //视屏总时长
                long videoDuration = videMessage.getResult().getData().getVideoDuration();
                //当前学习进度
                VideoInformStudyTotal studyTotal = videMessage.getResult().getData().getStudyTotal();
                //如果学习总时长超过了视屏总时长那么就跳过
                log.info("正在学习视屏：{}", videoInform.getName());
                //开始看视屏---------------
                long studyTime = Long.parseLong(studyTotal.getDuration());

                //直接先提交一次，这里是为了防止傻逼英华TM自己传的参数都搞错整成了视屏时长为0的问题（傻逼英华，连个参数都传错了）
                CourseAction.submitStudyTime(user, videoInform, studyTime + studyInterval, studyId);
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
                        else
                            update();
                    }
                }
            }
        }).start();
        addAcco();
    }
}

public void addAcco() {
    synchronized (accoVideo) {
        ++accoVideo;
    }
}

public long getAcco() {
    synchronized (accoVideo) {
        return this.accoVideo;
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

/**
 * 自动考试
 */
public void autoExamAction(CourseInform courseInform, NodeList videoInform, String nodeId, String examId) {

    //先检测AI是否可用
    boolean aiState = ExamAction.checkChatGLM(setting.getAiSetting().getAiType(), setting.getAiSetting().getAPI_KEY());
    if (!aiState) {
        if (setting.getAiSetting().getAPI_KEY() == null) {
            log.info("APIK_KEY参数为空，请检查API_KEY配置是否正常填写！！！");
            return;
        }
        log.info("AI无法正常使用，请检查API_KEY是否过期等情况！！！");
        return;
    }


    log.info("{}:正在考试课程>>>{}", user.getAccount(), courseInform.getName());
    ExamInformRequest exam = com.cbq.yatori.core.action.yinghua.ExamAction.getExam(user, nodeId);
//        String examId = String.valueOf(exam.getExamInformResult().getList().get(0).getId());
    String courseId = String.valueOf(exam.getExamInformResult().getList().get(0).getCourseId());
    StartExamRequest startExamRequest = com.cbq.yatori.core.action.yinghua.ExamAction.startExam(user, courseId, nodeId, examId);//开始考试
    ExamTopics examTopics = com.cbq.yatori.core.action.yinghua.ExamAction.getExamTopic(user, nodeId, examId);//获取题目

    List<String> list = examTopics.getExamTopics().keySet().stream().toList();
    for (int i = 0; i < list.size(); ++i) {
        ExamTopic examTopic = examTopics.getExamTopics().get(list.get(i));

        String answer = "";
        //如果存有相关的题那么直接获取答案回答
        if (topicMd5.containsKey(turnMd5(examTopic))) {
            answer = topics.get(topicMd5.get(turnMd5(examTopic))).getAnswer();
        } else {
            // 没缓存那么就直接AI
            answer = com.cbq.yatori.core.action.yinghua.ExamAction.aiAnswerFormChatGLM(setting.getAiSetting().getAiType(), setting.getAiSetting().getAPI_KEY(), examTopics.getExamTopics().get(list.get(i)), courseInform.getName());
            answer = answer.replace("\n", "");
            answer = answer.replace(" ", "");
            String topicAllContent = examTopic.getContent();
            topics.add(new Topic(turnMd5(examTopic), turnTopicType(examTopic.getType()), examTopic.getContent(), answer));
        }
        if (examTopic.getType().equals("单选") || examTopic.getType().equals("多选") || examTopic.getType().equals("判断")) {
            ExamAction.submitExam(user, examId, examTopics.getExamTopics().get(list.get(i)).getAnswerId(), answer, (i + 1) < list.size() ? "0" : "1");
        } else if (examTopic.getType().equals("填空")) {
            ExamAction.submitExam(user, examId, examTopics.getExamTopics().get(list.get(i)).getAnswerId(), examTopic.getSelects(), (i + 1) < list.size() ? "0" : "1");
        }

    }
    log.info("{}:课程:{}考试成功！对应考试试卷{}，服务器信息：{}", user.getAccount(), courseInform.getName(), videoInform.getName());
}


/**
 * 自动写作业习题
 */
public void autoWorkAction(CourseInform courseInform, NodeList videoInform, String nodeId, String workId) {

    //先检测AI是否可用
    boolean aiState = ExamAction.checkChatGLM(setting.getAiSetting().getAiType(), setting.getAiSetting().getAPI_KEY());
    if (!aiState) {
        if (setting.getAiSetting().getAPI_KEY() == null) {
            log.info("APIK_KEY参数为空，请检查API_KEY配置是否正常填写！！！");
            return;
        }
        log.info("AI无法正常使用，请检查API_KEY是否过期等情况！！！");
        return;
    }

    log.info("{}:正在写课程习题>>>{}", user.getAccount(), courseInform.getName());
    ExamInformRequest exam = com.cbq.yatori.core.action.yinghua.ExamAction.getWork(user, nodeId);
//        String workId = String.valueOf(exam.getExamInformResult().getList().get(0).getId());
    String courseId = String.valueOf(exam.getExamInformResult().getList().get(0).getCourseId());
    StartExamRequest startExamRequest = com.cbq.yatori.core.action.yinghua.ExamAction.startWork(user, courseId, nodeId, workId);//开始考试
    if (startExamRequest.getCode() == 9) {
        try {
            log.info("{}:课程:{}课后作业考试失败！对应作业试卷{}，服务器信息：{}", user.getAccount(), courseInform.getName(), videoInform.getName(), ConverterStartExam.toJsonString(startExamRequest));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return;
    }
    ExamTopics examTopics = com.cbq.yatori.core.action.yinghua.ExamAction.getWorkTopic(user, nodeId, workId);//获取题目

    List<String> list = examTopics.getExamTopics().keySet().stream().toList();
    for (int i = 0; i < list.size(); ++i) {
        ExamTopic examTopic = examTopics.getExamTopics().get(list.get(i));

        String answer = "";
        //如果存有相关的题那么直接获取答案回答
        if (topicMd5.containsKey(turnMd5(examTopic))) {
            answer = topics.get(topicMd5.get(turnMd5(examTopic))).getAnswer();
        } else {
            // 没缓存那么就直接AI
            answer = com.cbq.yatori.core.action.yinghua.ExamAction.aiAnswerFormChatGLM(setting.getAiSetting().getAiType(), setting.getAiSetting().getAPI_KEY(), examTopics.getExamTopics().get(list.get(i)), courseInform.getName());
            answer = answer.replace("\n", "");
            answer = answer.replace(" ", "");
            String topicAllContent = examTopic.getContent();
            topics.add(new Topic(turnMd5(examTopic), turnTopicType(examTopic.getType()), examTopic.getContent(), answer));
        }
        if (examTopic.getType().equals("单选") || examTopic.getType().equals("多选") || examTopic.getType().equals("判断")) {
            ExamAction.submitWork(user, workId, examTopics.getExamTopics().get(list.get(i)).getAnswerId(), answer, (i + 1) < list.size() ? "0" : "1");
        } else if (examTopic.getType().equals("填空")) {
            ExamAction.submitWork(user, workId, examTopics.getExamTopics().get(list.get(i)).getAnswerId(), examTopic.getSelects(), (i + 1) < list.size() ? "0" : "1");
        }
    }
    log.info("{}:课程:{}课后作业考试成功！对应作业试卷{}，服务器信息：{}", user.getAccount(), courseInform.getName(), videoInform.getName());
}

private String turnMd5(ExamTopic examTopic) {

    return "";
}

private String turnTopicType(String type) {
    if (type.contains("单选")) {
        return "ONECHOICE";
    } else if (type.contains("多选")) {
        return "MULTIPLECHOICE";
    } else if (type.contains("填空")) {
        return "COMPLETION";
    } else if (type.contains("简答")) {
        return "SHORTANSWER";
    }
    return "";
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

    public Builder setting(Setting setting) {
        courseStudyAction.setting = setting;
        return this;
    }

    public CourseStudyAction build() {
        //初始化视屏列表
        courseStudyAction.courseVideosList = null;
        while ((courseStudyAction.courseVideosList = CourseAction.getCourseVideosList(courseStudyAction.user, courseStudyAction.courseInform)) == null)
            ;

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