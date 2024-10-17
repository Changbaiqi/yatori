package com.cbq.yatori.core.action.canghui;

import com.cbq.yatori.core.action.canghui.entity.mycourselistresponse.Course;
import com.cbq.yatori.core.entity.*;
import com.cbq.yatori.core.utils.EmailUtil;
import com.cbq.yatori.core.action.canghui.entity.coursedetail.Chapter;
import com.cbq.yatori.core.action.canghui.entity.coursedetail.CourseDetailData;
import com.cbq.yatori.core.action.canghui.entity.coursedetail.Process;
import com.cbq.yatori.core.action.canghui.entity.coursedetail.Section;
import com.cbq.yatori.core.action.canghui.entity.exam.ExamCourse;
import com.cbq.yatori.core.action.canghui.entity.exam.ExamItem;
import com.cbq.yatori.core.action.canghui.entity.exam.ExamJson;
import com.cbq.yatori.core.action.canghui.entity.exam.ExamTopic;
import com.cbq.yatori.core.action.canghui.entity.examsubmit.TopicAnswer;
import com.cbq.yatori.core.action.canghui.entity.examsubmit.TopicRequest;
import com.cbq.yatori.core.action.canghui.entity.examsubmitrespose.ExamSubmitResponse;
import com.cbq.yatori.core.action.canghui.entity.mycourselistresponse.*;
import com.cbq.yatori.core.action.canghui.entity.startexam.StartExam;
import com.cbq.yatori.core.action.canghui.entity.submitstudy.ConverterSubmitStudyTime;
import com.cbq.yatori.core.action.canghui.entity.submitstudy.SubmitStudyTimeRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;

import javax.mail.MessagingException;
import java.util.*;

@Slf4j
public class CourseStudyAction implements Runnable {
    private User user;
    private Setting setting;
    //    private static final Boolean IsOpenmail = ConfigUtils.loadingConfig().getSetting().getEmailInform().getEmail()!="";
    private MyCourse myCourse; // 当前课程的对象

    // 视屏
    // private VideoRouter videoRouter;
    private final Map<Long, RouterDatum> map = new HashMap<>();


    private boolean newThread;// 是否线程堵塞

    private final long studyInterval = 5; // 单次提交学习时长

    private Long acoVideo = 0L;// 统计观看视屏数

    public void toStudy() {
        CoursesCustom coursesCustom = user.getCoursesCustom();

        // 视屏刷课模式
        switch (coursesCustom.getVideoModel()) {
            case 0 -> {
                acoVideo = (long) map.keySet().size();
            }
            // 普通模式
            case 1 -> {
                if (newThread) {
                    new Thread(this).start();
                } else {
                    log.info("{}:正在学习课程>>>{}", user.getAccount(), myCourse.getCourse().getTitle());
                    study1();
                    log.info("{}:{}学习完毕！", user.getAccount(), myCourse.getCourse().getTitle());
                }
            }
            // 暴力模式
            case 2 -> {
                log.info("{}:正在学习课程>>>{}", user.getAccount(), myCourse.getCourse().getTitle());
                study2();
            }
        }

        // 自动考试模式
        if (coursesCustom.getAutoExam() == 1) {
            autoExamAction();
        }
    }

    /**
     * 普通一个一个刷版本
     */
    public void study1() {
        AccountCacheCangHui cache = (AccountCacheCangHui) user.getCache();
        // Iterator<Long> iterator = map.keySet().iterator();
        Long[] arr = map.keySet().toArray(new Long[0]);
        Arrays.sort(arr);
        for (Long videoId : arr) {
            RouterDatum routerDatum = map.get(videoId);

            long studyTime = routerDatum.getProgress();// 当前学习时间
            long videoDuration = routerDatum.getVideoDuration();// 视屏总时长
            String title = routerDatum.getName();// 视屏名称
            // 循环开始学习
            while (studyTime < videoDuration) {
                // 这里根据账号账号登录状态进行策划行为
                switch (cache.getStatus()) {// 未登录则跳出
                    case 0 -> {
                        log.info("账号未登录，禁止刷课！");
                        return;
                    }
                    case 2 -> {// 如果登录超时，则堵塞等待
                        studyTime -= studyInterval;
                        continue;
                    }
                }

                SubmitStudyTimeRequest submitStudyTimeRequest = CourseAction.submitLearnTime(user, myCourse.getSemesterId(), videoId, studyTime);

                try {
                    if (submitStudyTimeRequest != null) {
                        if (submitStudyTimeRequest.getMsg().contains("登录超时")) {
                            cache.setStatus(2);
                            studyTime -= studyInterval;
                            continue;
                        }

                        // 成功提交
                        log.info("\n服务器端信息：>>>{}\n学习账号>>>{}\n学习平台>>>{}\n视屏名称>>>{}\n视屏总长度>>>{}\n当前学时>>>{}",
                                ConverterSubmitStudyTime.toJsonString(submitStudyTimeRequest),
                                user.getAccount(),
                                user.getAccountType().name(),
                                title,
                                videoDuration,
                                studyTime);
                    }

                    Thread.sleep(1000 * studyInterval);
                } catch (JsonProcessingException e) {
                    log.error("");
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }


                // 添加学时
                studyTime += studyInterval;

                // 更新数据
                if (studyTime >= videoDuration) {
                    if (submitStudyTimeRequest == null)
                        studyTime -= studyInterval;
                    else {
//                        update();
                        Map<Long, RouterDatum> resRouterDatum = getNewRouterDatum();
                        studyTime = resRouterDatum.get(videoId).getProgress();
                    }
                }
            }
            addAcco();
        }

    }

    /**
     * 暴力模式
     */
    public void study2() {

        AccountCacheCangHui cache = (AccountCacheCangHui) user.getCache();
        Long[] arr = map.keySet().toArray(new Long[0]);
        for (Long videoId : arr) {
            new Thread(() -> {
                long resVideoId = videoId;
                RouterDatum routerDatum = map.get(resVideoId);
                long studyTime = routerDatum.getProgress() == 0 ? studyInterval : routerDatum.getProgress();// 当前学习时间
                long videoDuration = routerDatum.getVideoDuration();// 视屏总时长
                String title = routerDatum.getName();// 视屏名称

                // 循环开始学习
                while (studyTime < videoDuration) {
                    // 这里根据账号账号登录状态进行策划行为
                    switch (cache.getStatus()) {// 未登录则跳出
                        case 0 -> {
                            log.info("账号未登录，禁止刷课！");
                            return;
                        }
                        case 2 -> {// 如果登录超时，则堵塞等待
                            studyTime -= studyInterval;
                            continue;
                        }
                    }

                    // 添加学时
                    studyTime += studyInterval;

                    SubmitStudyTimeRequest submitStudyTimeRequest = CourseAction.submitLearnTime(user, myCourse.getSemesterId(), resVideoId, studyTime);
                    try {
                        if (submitStudyTimeRequest != null) {
                            if (submitStudyTimeRequest.getMsg().contains("登录超时")) {
                                cache.setStatus(2);
                                studyTime -= studyInterval;
                                continue;
                            }

                            // 成功提交
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
                        log.error("{}", e.getMessage());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    // 更新数据
                    if (studyTime >= videoDuration) {
                        if (submitStudyTimeRequest == null) {
                            studyTime -= studyInterval;
                        } else {
                            // 这一步进行网页视屏课程时长获取并赋值同步赋值
                            Map<Long, RouterDatum> newRouterDatum = getNewRouterDatum();
                            RouterDatum resRouterDatum = newRouterDatum.get(resVideoId);
                            studyTime = resRouterDatum.getProgress();
                        }
                    }
                }
                addAcco();
                // 判断是否刷课完成
                if (getAcco() == arr.length) {
                    log.info("{}:{}学习完毕！", user.getAccount(), myCourse.getCourse().getTitle());
                }
            }).start();
        }
    }

    /**
     * 自动考试
     */
    public void autoExamAction() {
        new Thread(() -> {
            int length = map.keySet().toArray(new Long[0]).length;
            while (true) {
                // 判断是否刷课完成
                long nowLen = getAcco();
                if (nowLen == length) {
                    log.info("{}:正在考试课程>>>{}", user.getAccount(), myCourse.getCourse().getTitle());
                    AccountCacheCangHui cacheCangHui = (AccountCacheCangHui) user.getCache();

                    // 获取考试
                    ExamJson examList;
                    while ((examList = ExamAction.getExamList(user, String.valueOf(myCourse.getCourseId()))) == null) ;

                    cacheCangHui.setExamJson(examList);

                    for (ExamCourse examCourse : examList.getExamCourses()) {

                        boolean interceptFlag = false; // 是否拦截，true为拦截，反之不拦截
                        // 是否配置了课程定制
                        if (user.getCoursesCustom() != null && user.getCoursesCustom().getCoursesSettings() != null) { // 是否配置了指定课程配置文件
                            ArrayList<CoursesSetting> coursesSettings = user.getCoursesCustom().getCoursesSettings();
                            for (CoursesSetting coursesSetting : coursesSettings) {
                                if (!coursesSetting.getName().equals(myCourse.getCourse().getTitle())) // 是否有匹配的定制配置
                                    continue;
                                // 是否包含指定考试
                                Set<String> includeExams = coursesSetting.getIncludeExams();
                                if (!includeExams.isEmpty()) {
                                    boolean resState = false;// 标记
                                    for (String includeExam : includeExams) {
                                        if (includeExam.equals(examCourse.getTitle())) { // 判断是否与当前考试标签相等
                                            resState = true;
                                            break;
                                        }
                                    }
                                    interceptFlag = !resState;
                                }
                                // 是否包含排除指定考试
                                Set<String> excludeExams = coursesSetting.getExcludeExams();
                                if (!excludeExams.isEmpty()) {
                                    boolean resState = false;// 标记
                                    for (String excludeExam : excludeExams) {
                                        if (excludeExam.equals(examCourse.getTitle())) { // 判断是否与当前考试标签相等
                                            resState = true;
                                            break;
                                        }
                                    }
                                    interceptFlag = resState;
                                }
                            }
                        }
                        if (interceptFlag)// 是否触发拦截
                            continue;

                        Integer id = examCourse.getId();
                        StartExam startExam;
                        while ((startExam = ExamAction.startExam(user, String.valueOf(id))) == null) ;
                        if (startExam.getCode() == -1) {// 代表考试考过了
                            log.info("{}:课程:{}考试失败！对应考试试卷{}，失败原因：{}",
                                    user.getAccount(),
                                    myCourse.getCourse().getTitle(),
                                    examCourse.getTitle(),
                                    startExam.getMsg());
                            continue;
                        }
                        TopicRequest topicRequest = new TopicRequest();
                        topicRequest.setId(String.valueOf(id));
                        topicRequest.setExamId(String.valueOf(id));
                        List<TopicAnswer> list = new ArrayList<>();
                        topicRequest.setAnswers(list);

                        // 获取对应考试题目
                        LinkedHashMap<String, ExamTopic> examTopics = examCourse.getExamTopics();
                        // 答案装载
                        examTopics.forEach((k, v) -> {
                            boolean flag = true;
                            for (ExamItem examItem : v.getItem()) {
                                if (v.getType() == 5) {
                                    flag = false;
                                    list.add(new TopicAnswer(List.of(examItem.getKey()), Long.parseLong(k)));
                                    break;
                                }
                                if (examItem.getIsCorrect()) {
                                    flag = false;
                                    list.add(new TopicAnswer(List.of(examItem.getValue()), Long.parseLong(k)));
                                }
                            }
                            if (flag) {
                                int choose = (int) (Math.random() * v.getItem().size());
                                list.add(new TopicAnswer(List.of(v.getItem().get(choose).getValue()), Long.parseLong(k)));
                            }

                        });

                        ExamSubmitResponse examSubmitResponse;
                        while ((examSubmitResponse = ExamAction.submitExam(user, topicRequest)) == null) ;
                        if (examSubmitResponse.getCode() != 0) {
                            log.info("{}:课程:{}考试失败！对应考试试卷{}，失败原因：{}",
                                    user.getAccount(),
                                    myCourse.getCourse().getTitle(),
                                    examCourse.getTitle(),
                                    startExam.getMsg());
                            continue;
                        }
                        log.info("{}:课程:{}考试成功！对应考试试卷{}，服务器信息：{}", user.getAccount(), myCourse.getCourse().getTitle(), examCourse.getTitle(), examSubmitResponse.getMsg());
                    }
                    break;
                }
            }
        }).start();
    }


    private void update() {
// 详细页面获取进度并赋值--------------------------
        CourseDetailData courseDetail;
        while ((courseDetail = CourseAction.getCourseDetail(user, myCourse.getCourse().getId())) == null) ;

        // 章节
        if (courseDetail.getChapters() != null)
            for (Chapter chapter : courseDetail.getChapters()) {
                // 视屏
                if (chapter == null)
                    continue;
                for (Section section : chapter.getSections()) {
                    Process process = section.getProcess();
                    if (process == null)
                        continue;
                    long id = process.getId();// 视屏id
                    long progress = process.getProgress();// 获取已经学习了的时长
                    RouterDatum orDefault = map.getOrDefault(id, null);
                    if (orDefault == null) continue;
                    orDefault.setProgress(Math.max(progress, orDefault.getProgress()));// 设置时长
                }
            }
    }


    public void addAcco() {
        synchronized (acoVideo) {
            ++acoVideo;
        }
    }

    public long getAcco() {
        synchronized (acoVideo) {
            return this.acoVideo;
        }
    }

    public Map<Long, RouterDatum> getNewRouterDatum() {

        Course course = myCourse.getCourse();// 获取课程
        long id1 = course.getId(); // 课程id
        VideoRouter router = course.getRouter();// 视屏列表
        Map<Long, RouterDatum> newMap = new HashMap<>();
        // 将需要学习的视屏加进去
        for (RouterDatum datum : router.getData()) {
            long id = datum.getId();// 视屏id
            newMap.put(id, datum);
        }


        // 详细页面获取进度并赋值--------------------------
        CourseDetailData courseDetail;
        while ((courseDetail = CourseAction.getCourseDetail(user, id1)) == null) ;

        // 章节
        if (courseDetail.getChapters() != null)
            for (Chapter chapter : courseDetail.getChapters()) {
                // 视屏
                if (chapter == null)
                    continue;
                for (Section section : chapter.getSections()) {
                    Process process = section.getProcess();
                    if (process == null)
                        continue;
                    long id = process.getId();// 视屏id
                    long progress = process.getProgress();// 获取已经学习了的时长
                    RouterDatum orDefault = newMap.getOrDefault(id, null);
                    if (orDefault == null) continue;
                    orDefault.setProgress(Math.max(progress, orDefault.getProgress()));// 设置时长
                }
            }
        return newMap;
    }

    @Override
    public void run() {
        log.info("{}:正在学习课程>>>{}", user.getAccount(), myCourse.getCourse().getTitle());
        study1();
        if (setting.getEmailInform().getSw() == 1) {
            try {
                EmailUtil.sendEmail(user.getAccount(), myCourse.getCourse().getTitle());
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
        log.info("{}:{}学习完毕！", user.getAccount(), myCourse.getCourse().getTitle());
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final CourseStudyAction courseStudyAction = new CourseStudyAction();

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

        public Builder setting(Setting setting) {
            courseStudyAction.setting = setting;
            return this;
        }

        public CourseStudyAction build() {
            Course course = courseStudyAction.myCourse.getCourse();// 获取课程
            long id1 = course.getId(); // 课程id
            VideoRouter router = course.getRouter();// 视屏列表

            // 将需要学习的视屏加进去
            for (RouterDatum datum : router.getData()) {
                long id = datum.getId();// 视屏id
                long videoDuration = datum.getVideoDuration();// 获取视屏总时长
                courseStudyAction.map.put(id, datum);
            }

            // 将进度缓存设置进去
            ProgressDetail progressDetail = courseStudyAction.myCourse.getProgressDetail();
            if (progressDetail.getData() != null)
                for (ProgressDetailDatum datum : progressDetail.getData()) {
                    RouterDatum routerDatum = courseStudyAction.map.getOrDefault(datum.getId(), null);
                    if (routerDatum == null)
                        continue;
                    courseStudyAction.map.get(datum.getId()).setProgress(datum.getProgress());// 设置观看进度
                }


            // 详细页面获取进度并赋值--------------------------
            CourseDetailData courseDetail = null;
            while ((courseDetail = CourseAction.getCourseDetail(courseStudyAction.user, id1)) == null) ;

            // 章节
            if (courseDetail.getChapters() != null)
                for (Chapter chapter : courseDetail.getChapters()) {
                    // 视屏
                    if (chapter == null)
                        continue;
                    for (Section section : chapter.getSections()) {
                        Process process = section.getProcess();
                        if (process == null)
                            continue;
                        long id = process.getId();// 视屏id
                        long progress = process.getProgress();// 获取已经学习了的时长
                        RouterDatum orDefault = courseStudyAction.map.getOrDefault(id, null);
                        if (orDefault == null) continue;
                        orDefault.setProgress(Math.max(progress, orDefault.getProgress()));// 设置时长
                    }
                }

            return courseStudyAction;
        }
    }
}
