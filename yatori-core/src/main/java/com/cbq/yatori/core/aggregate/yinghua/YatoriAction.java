package com.cbq.yatori.core.aggregate.yinghua;

import com.cbq.yatori.core.action.canghui.entity.mycourselistresponse.Course;
import com.cbq.yatori.core.action.canghui.entity.mycourselistresponse.MyCourse;
import com.cbq.yatori.core.action.canghui.entity.mycourselistresponse.MyCourseData;
import com.cbq.yatori.core.action.enaea.entity.underwayproject.ResultList;
import com.cbq.yatori.core.action.enaea.entity.underwayproject.UnderwayProjectRquest;
import com.cbq.yatori.core.action.yinghua.CourseAction;
import com.cbq.yatori.core.action.yinghua.CourseStudyAction;
import com.cbq.yatori.core.action.yinghua.entity.allcourse.CourseInform;
import com.cbq.yatori.core.action.yinghua.entity.allcourse.CourseRequest;
import com.cbq.yatori.core.entity.User;

public class YatoriAction {

    public static void brushLessonsAction(YatoriLoginYingHua yatoriLogin){
        User user = yatoriLogin.getUser();
        switch (user.getAccountType()) {
            case YINGHUA -> {
                new Thread(()->{
                    //获取全部课程
                    CourseRequest allCourseList = null;
                    while((allCourseList= CourseAction.getAllCourseRequest(user))==null);

                    for (CourseInform courseInform : allCourseList.getResult().getList()) {
                        //课程排除配置
                        if(user.getCoursesCostom()!=null) {
                            if (user.getCoursesCostom().getExcludeCourses() != null) {
                                if (user.getCoursesCostom().getExcludeCourses().size() != 0)
                                    if (user.getCoursesCostom().getExcludeCourses().contains(courseInform.getName()))
                                        continue;
                            }
                            //如果有指定课程包含设定，那么就执行
                            if (user.getCoursesCostom().getIncludeCourses() != null) {
                                if (user.getCoursesCostom().getIncludeCourses().size() != 0)
                                    if (!user.getCoursesCostom().getIncludeCourses().contains(courseInform.getName()))
                                        continue;
                            }
                        }
                        CourseStudyAction bulild = CourseStudyAction.builder()
                                .user(user)
                                .courseInform(courseInform)
                                .newThread(true)
                                .build();
                        bulild.toStudy();
                    }
                }).start();
            }
            case CANGHUI -> {
                new Thread(()->{
                    //获取全部课程
//                        CourseRequest allCourseList = null;
                    MyCourseData myCourseData = null;

                    while((myCourseData= com.cbq.yatori.core.action.canghui.CourseAction.myCourseList(user))==null);

                    for (MyCourse list : myCourseData.getLists()) {
                        Course courseInform = list.getCourse();
                        //课程排除配置
                        if(user.getCoursesCostom()!=null) {
                            if (user.getCoursesCostom().getExcludeCourses() != null) {
                                if (user.getCoursesCostom().getExcludeCourses().size() != 0)
                                    if (user.getCoursesCostom().getExcludeCourses().contains(courseInform.getTitle()))
                                        continue;
                            }
                            //如果有指定课程包含设定，那么就执行
                            if (user.getCoursesCostom().getIncludeCourses() != null) {
                                if (user.getCoursesCostom().getIncludeCourses().size() != 0)
                                    if (!user.getCoursesCostom().getIncludeCourses().contains(courseInform.getTitle()))
                                        continue;
                            }
                        }
                        com.cbq.yatori.core.action.canghui.CourseStudyAction bulild = com.cbq.yatori.core.action.canghui.CourseStudyAction.builder()
                                .user(user)
                                .courseInform(list)
                                .newThread(true)
                                .build();
                        bulild.toStudy();
                    }
                }).start();
            }

            case ENAEA -> {
                new Thread(()->{
                    //获取正在进行的项目
                    UnderwayProjectRquest underwayProject = com.cbq.yatori.core.action.enaea.CourseAction.getUnderwayProject(user);
                    //遍历获取所有正在学的课程项目
                    for (ResultList resultList : underwayProject.getResult().getList()) {
                        com.cbq.yatori.core.action.enaea.CourseStudyAction build = com.cbq.yatori.core.action.enaea.CourseStudyAction.builder()
                                .user(user)
                                .objectInform(resultList)
                                .newThread(true)
                                .build();
                        build.toStudy();
                    }
                }).start();
            }
        }
    }
}
