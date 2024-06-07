package com.cbq.yatori.core.action.enaea;
import com.cbq.yatori.core.entity.CoursesCustom;
import com.cbq.yatori.core.entity.Setting;
import com.cbq.yatori.core.utils.EmailUtil;
import com.cbq.yatori.core.action.enaea.entity.ccvideo.CCVideoRequest;
import com.cbq.yatori.core.action.enaea.entity.coursevidelist.CourseVideoListRequest;
import com.cbq.yatori.core.action.enaea.entity.requirecourselist.ListElement;
import com.cbq.yatori.core.action.enaea.entity.requirecourselist.RequiredCourseListRequest;
import com.cbq.yatori.core.action.enaea.entity.submitlearntime.SubmitLearnTimeConverter;
import com.cbq.yatori.core.action.enaea.entity.submitlearntime.SubmitLearnTimeRequest;
import com.cbq.yatori.core.action.enaea.entity.underwayproject.ResultList;
import com.cbq.yatori.core.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;

import javax.mail.MessagingException;

@Slf4j
public class CourseStudyAction implements Runnable {
    private User user;
    private Setting setting;
//    private static final Boolean IsOpenmail = ConfigUtils.loadingConfig().getSetting().getEmailInform().getEmail()!="";

    //正在进行的其中一个项目
    ResultList resultList;
    private boolean newThread;//是否线程堵塞


    public void toStudy(){
        CoursesCustom coursesCustom = user.getCoursesCustom();

        //视屏刷课模式
        switch (coursesCustom.getVideoModel()){
            case 0->{

            }
            //普通模式
            case 1->{
                if(newThread){
                    new Thread(this).start();
                }else {
                    log.info("{}:正在学习项目>>>{}",user.getAccount(),resultList.getCircleName());
                    study1();
                    log.info("{}:{}学习完毕！",user.getAccount(),resultList.getCircleName());
                }
            }
        }
    }


    public void study1(){
        log.info(resultList.getCircleId().toString()); //打印所有需要学的项目ID
        //遍历获取必学课程
        RequiredCourseListRequest requiredCourseList = CourseAction.getRequiredCourseList(user, resultList.getCircleId().toString());
        for (ListElement listElement : requiredCourseList.getResult().getList()) {
//            log.info(listElement.getStudyCenterDTO().getCourseTitle());
            log.info("{}:正在学习课程>>>{}",user.getAccount(),listElement.getStudyCenterDTO().getCourseTitle());
            //获取对应课程的视屏
            CourseVideoListRequest courseVideList = CourseAction.getCourseVideList(user, resultList.getCircleId().toString(), listElement.getStudyCenterDTO().getCourseId().toString());
            for (com.cbq.yatori.core.action.enaea.entity.coursevidelist.ResultList list : courseVideList.getResult().getList()) {
                float progress = Float.valueOf(list.getStudyProgress()); //获取当前视屏进度
                if (progress >= 100)
                    continue;
                log.info("{}:正在学习视屏：{}", user.getAccount(),list.getFilename());
                CCVideoRequest ccVideoRequest = CourseAction.statisticForCCVideo(user, listElement.getStudyCenterDTO().getCourseId(), list.getId(), resultList.getCircleId());
                if (!ccVideoRequest.isSuccess()) //不能点进去观看则直接跳出
                    continue;
                do {
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    SubmitLearnTimeRequest submitLearnTimeRequest = CourseAction.submitLearnTime(user, ccVideoRequest.getSCFUCKPKey(), ccVideoRequest.getSCFUCKPValue(), resultList.getCircleId().toString(), list.getId().toString(), System.currentTimeMillis());
                    //如果提交学时失败则启用自纠正（再次拉取Cookie重新计时提交)
                    while (!submitLearnTimeRequest.isSuccess()) {
                        log.info("状态：{}", submitLearnTimeRequest.getMessage());//这边如果提示double说明存在视屏双开的情况
                        //失败提交
                        try {
                            log.info("\n服务器端信息：>>>{}\n学习账号>>>{}\n学习平台>>>{}\n视屏名称>>>{}\n当前学习进度>>>{}",
                                    SubmitLearnTimeConverter.toJsonString(submitLearnTimeRequest),
                                    user.getAccount(),
                                    user.getAccountType().name(),
                                    list.getFilename(),
                                    progress+"%");
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                        ccVideoRequest = CourseAction.statisticForCCVideo(user, listElement.getStudyCenterDTO().getCourseId(), list.getId(), resultList.getCircleId());
                        submitLearnTimeRequest = CourseAction.submitLearnTime(user, ccVideoRequest.getSCFUCKPKey(), ccVideoRequest.getSCFUCKPValue(), resultList.getCircleId().toString(), list.getId().toString(), System.currentTimeMillis());
                    }
                    progress = submitLearnTimeRequest.getProgress();
                    //成功提交
                    try {
                        log.info("\n服务器端信息：>>>{}\n学习账号>>>{}\n学习平台>>>{}\n视屏名称>>>{}\n当前学习进度>>>{}",
                                SubmitLearnTimeConverter.toJsonString(submitLearnTimeRequest),
                                user.getAccount(),
                                user.getAccountType().name(),
                                list.getFilename(),
                                progress+"%");
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                } while (progress < 100);
                log.info("正在学习视屏：{}", list.getFilename());
                log.info("{}:{}视屏学习完毕！", user.getAccount(),list.getFilename());
            }
            log.info("{}:{}课程学习完毕！",user.getAccount(),listElement.getStudyCenterDTO().getCourseTitle());
        }
    }

    @Override
    public void run() {
        log.info("{}:正在学习项目>>>{}", user.getAccount(),resultList.getClusterName());
        study1();
        if(setting.getEmailInform().getSw()==1){
            try {
                EmailUtil.sendEmail(user.getAccount(), resultList.getClusterName());
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }}
        log.info("{}:{}学习完毕！",  user.getAccount(),resultList.getClusterName());
    }

    public static Builder builder() {
        return new Builder();
    }


    public static class Builder{
        private CourseStudyAction courseStudyAction = new CourseStudyAction();

        public Builder user(User user){
            courseStudyAction.user = user;
            return this;
        }

        public Builder objectInform(ResultList resultList){
            courseStudyAction.resultList = resultList;
            return this;
        }

        public Builder newThread(Boolean newThread){
            courseStudyAction.newThread = newThread;
            return this;
        }

        public Builder setting(Setting setting){
            courseStudyAction.setting = setting;
            return this;
        }
        public CourseStudyAction build() {
            return courseStudyAction;
        }
    }

}
