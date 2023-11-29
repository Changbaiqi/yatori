package com.cbq.brushlessons.core.action.canghui;

import com.cbq.brushlessons.core.action.canghui.entity.coursedetail.Chapter;
import com.cbq.brushlessons.core.action.canghui.entity.coursedetail.CourseDetailData;
import com.cbq.brushlessons.core.action.canghui.entity.coursedetail.Process;
import com.cbq.brushlessons.core.action.canghui.entity.coursedetail.Section;
import com.cbq.brushlessons.core.action.canghui.entity.exam.ExamCourse;
import com.cbq.brushlessons.core.action.canghui.entity.exam.ExamItem;
import com.cbq.brushlessons.core.action.canghui.entity.exam.ExamJson;
import com.cbq.brushlessons.core.action.canghui.entity.exam.ExamTopic;
import com.cbq.brushlessons.core.action.canghui.entity.examsubmit.TopicAnswer;
import com.cbq.brushlessons.core.action.canghui.entity.examsubmit.TopicRequest;
import com.cbq.brushlessons.core.action.canghui.entity.examsubmitrespose.ExamSubmitResponse;
import com.cbq.brushlessons.core.action.canghui.entity.mycourselistresponse.*;
import com.cbq.brushlessons.core.action.canghui.entity.startexam.StartExam;
import com.cbq.brushlessons.core.action.canghui.entity.submitstudy.ConverterSubmitStudyTime;
import com.cbq.brushlessons.core.action.canghui.entity.submitstudy.SubmitStudyTimeRequest;
import com.cbq.brushlessons.core.entity.AccountCacheCangHui;
import com.cbq.brushlessons.core.entity.CoursesSetting;
import com.cbq.brushlessons.core.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class CourseStudyAction implements Runnable {
    private User user;

    private MyCourse myCourse;

    //视屏
//    private VideoRouter videoRouter;
    private Map<Long, RouterDatum> map = new HashMap<>();


    private boolean newThread;

    private long studyInterval = 5; //单次提交学习时长

    private long accoVideo = 0;

    public void toStudy() {
        user.setModel(user.getModel() == null ? 0 : user.getModel());
        switch (user.getModel()) {
            //普通模式
            case 0 -> {
                if (newThread) {
                    new Thread(this).start();
                } else {
                    log.info("{}:正在学习课程>>>{}", user.getAccount(), myCourse.getCourse().getTitle());
                    study1();
                    log.info("{}:{}学习完毕！", user.getAccount(), myCourse.getCourse().getTitle());
                    if (user.getAutoExam() == 1) {
                        log.info("{}:正在考试课程>>>{}", user.getAccount(), myCourse.getCourse().getTitle());
                        autoExamAction();
                        log.info("{}:{}考试完毕！", user.getAccount(), myCourse.getCourse().getTitle());
                    }
                }
            }
            //暴力模式
            case 1 -> {
                log.info("{}:正在学习课程>>>{}", user.getAccount(), myCourse.getCourse().getTitle());
                study2();
            }
        }
    }

    /**
     * 普通一个一个刷版本
     */
    public void study1() {
        AccountCacheCangHui cache = (AccountCacheCangHui) user.getCache();
        Iterator<Long> iterator = map.keySet().iterator();
        Long arr[] = map.keySet().toArray(new Long[0]);
        Arrays.sort(arr);
        for (int i = 0; i < arr.length; ++i) {
            Long videoId = arr[i];
            RouterDatum routerDatum = map.get(videoId);

            long studyTime = routerDatum.getProgress();//当前学习时间
            long videoDuration = routerDatum.getVideoDuration();//视屏总时长
            String title = routerDatum.getName();//视屏名称
            //循环开始学习
            while (studyTime < videoDuration) {
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

                SubmitStudyTimeRequest submitStudyTimeRequest = CourseAction.submitLearnTime(user, myCourse, videoId, studyTime);
                try {
                    if (submitStudyTimeRequest != null) {
                        if (submitStudyTimeRequest.getMsg().contains("登录超时")) {
                            cache.setStatus(2);
                            studyTime -= studyInterval;
                            continue;
                        }
                        //成功提交

                        log.info("\n服务器端信息：>>>{}\n学习账号>>>{}\n学习平台>>>{}\n视屏名称>>>{}\n视屏总长度>>>{}\n当前学时>>>{}",
                                ConverterSubmitStudyTime.toJsonString(submitStudyTimeRequest),
                                user.getAccount(),
                                user.getAccountType().name(),
                                title,
                                videoDuration,
                                studyTime);


                    }

                    if (studyTime < videoDuration) {
                        Thread.sleep(1000 * studyInterval);
                    }
                } catch (JsonProcessingException e) {
                    log.error("");
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }


                //添加学时
                studyTime += studyInterval;

                //更新数据
                if (studyTime >= videoDuration) {
                    if (submitStudyTimeRequest == null)
                        studyTime -= studyInterval;
                    else
                        update();
                }
            }
        }

        //自动考试
        if (user.getAutoExam() == 1) {
            log.info("{}:正在考试课程>>>{}", user.getAccount(), myCourse.getCourse().getTitle());
            autoExamAction();
            log.info("{}:{}考试完毕！", user.getAccount(), myCourse.getCourse().getTitle());
        }
    }

    /**
     * 暴力模式
     */
    public void study2() {

        AccountCacheCangHui cache = (AccountCacheCangHui) user.getCache();
        Long arr[] = map.keySet().toArray(new Long[0]);

        for (int i = 0; i < arr.length; ++i) {
            Long videoId = arr[i];
            new Thread(() -> {
                RouterDatum routerDatum = map.get(videoId);
                long studyTime = routerDatum.getProgress() == 0 ? studyInterval : routerDatum.getProgress();//当前学习时间
                long videoDuration = routerDatum.getVideoDuration();//视屏总时长
                String title = routerDatum.getName();//视屏名称

                //循环开始学习
                while (studyTime < videoDuration) {
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

                    SubmitStudyTimeRequest submitStudyTimeRequest = CourseAction.submitLearnTime(user, myCourse, videoId, studyTime);
                    try {
                        if (submitStudyTimeRequest != null) {
                            if (submitStudyTimeRequest.getMsg().contains("登录超时")) {
                                cache.setStatus(2);
                                studyTime -= studyInterval;
                                continue;
                            }
                            //成功提交

                            log.info("\n服务器端信息：>>>{}\n学习账号>>>{}\n学习平台>>>{}\n视屏名称>>>{}\n视屏总长度>>>{}\n当前学时>>>{}",
                                    ConverterSubmitStudyTime.toJsonString(submitStudyTimeRequest),
                                    user.getAccount(),
                                    user.getAccountType().name(),
                                    title,
                                    videoDuration,
                                    studyTime);


                        }

                        if (studyTime < videoDuration) {
                            Thread.sleep(1000 * studyInterval);
                        }
                    } catch (JsonProcessingException e) {
                        log.error("");
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }


                    //添加学时
                    studyTime += studyInterval;

                    //更新数据
                    if (studyTime >= videoDuration) {
                        if (submitStudyTimeRequest == null)
                            studyTime -= studyInterval;
                        else
                            addAcco();
                        //判断是否刷课完成
                        if (getAcco() == arr.length)
                            log.info("{}:{}学习完毕！", user.getAccount(), myCourse.getCourse().getTitle());
                    }
                }
            }).start();
        }
    }

    /**
     * 自动考试
     */
    public void autoExamAction() {
        AccountCacheCangHui cacheCangHui = (AccountCacheCangHui) user.getCache();

        //获取考试
        ExamJson examList = null;
        while ((examList = ExamAction.getExamList(user, String.valueOf(myCourse.getCourseId()))) == null) ;

        cacheCangHui.setExamJson(examList);

        for (ExamCourse examCours : examList.getExamCourses()) {

            boolean interceptFlag = false; //是否拦截，true为拦截，反之不拦截
            if (user.getCoursesCostom() != null) {//是否配置了课程定制
                if (user.getCoursesCostom().getCoursesSettings() != null) { //是否配置了指定课程配置文件
                    ArrayList<CoursesSetting> coursesSettings = user.getCoursesCostom().getCoursesSettings();
                    for (CoursesSetting coursesSetting : coursesSettings) {
                        if (!coursesSetting.getName().equals(myCourse.getCourse().getTitle())) //是否有匹配的定制配置
                            continue;
                        //是否包含指定考试
                        Set<String> includeExams = coursesSetting.getIncludeExams();
                        if (includeExams != null) {
                            if (includeExams.size() != 0) {
                                boolean resState = false;//标记
                                for (String includeExam : includeExams) {
                                    if (includeExam.equals(examCours.getTitle())) { //判断是否与当前考试标签相等
                                        resState = true;
                                        break;
                                    }
                                }
                                interceptFlag = resState ? false : true;
                            }
                        }
                        //是否包含排除指定考试
                        Set<String> excludeExams = coursesSetting.getExcludeExams();
                        if (excludeExams != null) {
                            if (excludeExams.size() != 0) {
                                boolean resState = false;//标记
                                for (String excludeExam : excludeExams) {
                                    if (excludeExam.equals(examCours.getTitle())) { //判断是否与当前考试标签相等
                                        resState = true;
                                        break;
                                    }
                                }
                                interceptFlag = resState ? true : false;
                            }
                        }
                    }
                }
            }
            if(interceptFlag)//是否触发拦截
                continue;

            Integer id = examCours.getId();
            StartExam startExam = null;
            while ((startExam = ExamAction.startExam(user, String.valueOf(id))) == null) ;
            if (startExam.getCode() == -1) {//代表考试考过了
                log.info("{}:课程:{}考试失败！对应考试试卷{}，失败原因：{}", user.getAccount(), myCourse.getCourse().getTitle(),examCours.getTitle(), startExam.getMsg());
                continue;
            }
            TopicRequest topicRequest = new TopicRequest();
            topicRequest.setId(String.valueOf(id));
            topicRequest.setExamId(String.valueOf(id));
            List<TopicAnswer> list = new ArrayList<>();
            topicRequest.setAnswers(list);

            //获取对应考试题目
            LinkedHashMap<String, ExamTopic> examTopics = examCours.getExamTopics();
            //答案装载
            examTopics.forEach((k, v) -> {
                boolean flag = true;
                for (ExamItem examItem : v.getItem()) {
                    if (v.getType() == 5) {
                        flag = false;
                        list.add(new TopicAnswer(List.of(examItem.getKey()), Long.parseLong(k)));
                        break;
                    }
                    if (examItem.getIsCorrect() == true) {
                        flag = false;
                        list.add(new TopicAnswer(List.of(examItem.getValue()), Long.parseLong(k)));
                        break;
                    }
                }
                if (flag) {
                    int choose = (int) (Math.random() * v.getItem().size()) + 0;
                    list.add(new TopicAnswer(List.of(v.getItem().get(choose).getValue()), Long.parseLong(k)));
                }

            });

            ExamSubmitResponse examSubmitResponse = null;
            while ((examSubmitResponse = ExamAction.submitExam(user, topicRequest)) == null) ;
            if (examSubmitResponse.getCode() != 0) {
                log.info("{}:课程:{}考试失败！对应考试试卷{}，失败原因：{}", user.getAccount(), myCourse.getCourse().getTitle(),examCours.getTitle(), startExam.getMsg());
                continue;
            }
            log.info("{}:课程:{}考试成功！对应考试试卷{}，服务器信息：{}", user.getAccount(), myCourse.getCourse().getTitle(),examCours.getTitle(), examSubmitResponse.getMsg());
        }
    }


    private void update() {
//详细页面获取进度并赋值--------------------------
        CourseDetailData courseDetail = null;
        while ((courseDetail = CourseAction.getCourseDetail(user, myCourse.getCourse().getId())) == null) ;

        //章节
        if (courseDetail.getChapters() != null)
            for (Chapter chapter : courseDetail.getChapters()) {
                //视屏
                if (chapter == null)
                    continue;
                for (Section section : chapter.getSections()) {
                    Process process = section.getProcess();
                    if (process == null)
                        continue;
                    long id = process.getId();//视屏id
                    long progress = process.getProgress();//获取已经学习了的时长
                    RouterDatum orDefault = map.getOrDefault(id, null);
                    if (orDefault == null) continue;
                    orDefault.setProgress(progress > orDefault.getProgress() ? progress : orDefault.getProgress());//设置时长
                }
            }
    }


    public synchronized void addAcco() {
        ++accoVideo;
    }

    public synchronized long getAcco() {
        return this.accoVideo;
    }

    @Override
    public void run() {
        log.info("{}:正在学习课程>>>{}", user.getAccount(), myCourse.getCourse().getTitle());
        study1();
        log.info("{}:{}学习完毕！", user.getAccount(), myCourse.getCourse().getTitle());
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

        public Builder courseInform(MyCourse myCourse) {
            courseStudyAction.myCourse = myCourse;
            return this;
        }

        public Builder newThread(Boolean newThread) {
            courseStudyAction.newThread = newThread;
            return this;
        }

        public CourseStudyAction build() {
            Course course = courseStudyAction.myCourse.getCourse();//获取课程
            long id1 = course.getId(); //课程id
            VideoRouter router = course.getRouter();//视屏列表

            //将需要学习的视屏加进去
            for (RouterDatum datum : router.getData()) {
                long id = datum.getId();//视屏id
                long videoDuration = datum.getVideoDuration();//获取视屏总时长
                courseStudyAction.map.put(id, datum);
            }

            //将进度缓存设置进去
            ProgressDetail progressDetail = courseStudyAction.myCourse.getProgressDetail();
            if (progressDetail.getData() != null)
                for (ProgressDetailDatum datum : progressDetail.getData()) {
                    RouterDatum routerDatum = courseStudyAction.map.getOrDefault(datum.getId(), null);
                    if (routerDatum == null)
                        continue;
                    courseStudyAction.map.get(datum.getId()).setProgress(datum.getProgress());//设置观看进度
                }


            //详细页面获取进度并赋值--------------------------
            CourseDetailData courseDetail = null;
            while ((courseDetail = CourseAction.getCourseDetail(courseStudyAction.user, id1)) == null) ;

            //章节
            if (courseDetail.getChapters() != null)
                for (Chapter chapter : courseDetail.getChapters()) {
                    //视屏
                    if (chapter == null)
                        continue;
                    for (Section section : chapter.getSections()) {
                        Process process = section.getProcess();
                        if (process == null)
                            continue;
                        long id = process.getId();//视屏id
                        long progress = process.getProgress();//获取已经学习了的时长
                        RouterDatum orDefault = courseStudyAction.map.getOrDefault(id, null);
                        if (orDefault == null) continue;
                        orDefault.setProgress(progress > orDefault.getProgress() ? progress : orDefault.getProgress());//设置时长
                    }
                }

            return courseStudyAction;
        }
    }
}
