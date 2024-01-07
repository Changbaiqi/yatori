package com.cbq.yatori.core.aggregate.yinghua;

import com.cbq.yatori.core.action.yinghua.CourseAction;
import com.cbq.yatori.core.action.yinghua.CourseStudyAction;
import com.cbq.yatori.core.action.yinghua.entity.allcourse.CourseInform;
import com.cbq.yatori.core.action.yinghua.entity.allcourse.CourseRequest;
import com.cbq.yatori.core.aggregate.YatoriOperation;
import com.cbq.yatori.core.entity.User;

import java.util.ArrayList;

/**
 * @description: TODO 全部课程的操作对象
 * @author 长白崎
 * @date 2024/1/4 19:40
 * @version 1.0
 */
public class YingHuaAllCoursesOperation extends YatoriOperation {
    private User user;
    private ArrayList<YingHuaCourseOperation> yingHuaCourseOperations; //课程列表



    public YingHuaAllCoursesOperation(User user, ArrayList<YingHuaCourseOperation> yingHuaCourseOperations) {
        this.user = user;
        this.yingHuaCourseOperations = yingHuaCourseOperations;
    }


    public User getUser() {
        return user;
    }

    public ArrayList<YingHuaCourseOperation> getYingHuaCourseOperations() {
        return yingHuaCourseOperations;
    }




    public static YingHuaCoursesOperationBuilder builder(){
        return new YingHuaCoursesOperationBuilder();
    }



    public static class YingHuaCoursesOperationBuilder{
        private User user; //用户

        private ArrayList<YingHuaCourseOperation> yingHuaCourseOperations=new ArrayList<>(); //课程列表

        public YingHuaCoursesOperationBuilder user(User user){
            this.user = user;
            return this;
        }

        /**
         * 构建课程列表
         */
        private void buildCourseOperations(){
            //获取全部课程
            CourseRequest allCourseList = null;
            while((allCourseList= CourseAction.getAllCourseRequest(user))==null);

            for (CourseInform courseInform : allCourseList.getResult().getList()) {
                
                //构建课程操作对象并且加入到对应列表
                YingHuaCourseOperation courseOperation = YingHuaCourseOperation.builder()
                        .user(user)
                        .courseInform(courseInform)
                        .build();
                yingHuaCourseOperations.add(courseOperation);
            }
        }
        public YingHuaAllCoursesOperation build(){

            //构建课程信息
            buildCourseOperations();

            return new YingHuaAllCoursesOperation(user,yingHuaCourseOperations);
        }
    }

}
