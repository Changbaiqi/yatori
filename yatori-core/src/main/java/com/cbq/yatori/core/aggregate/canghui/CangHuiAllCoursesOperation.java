package com.cbq.yatori.core.aggregate.canghui;

import com.cbq.yatori.core.action.canghui.CourseStudyAction;
import com.cbq.yatori.core.action.canghui.entity.mycourselistresponse.Course;
import com.cbq.yatori.core.action.canghui.entity.mycourselistresponse.MyCourse;
import com.cbq.yatori.core.action.canghui.entity.mycourselistresponse.MyCourseData;
import com.cbq.yatori.core.entity.User;

import java.util.ArrayList;

public class CangHuiAllCoursesOperation {

    private User user;

    private ArrayList<CangHuiCourseOperation> cangHuiCourseOperations; //课程控制对象列表

    public CangHuiAllCoursesOperation(User user, ArrayList<CangHuiCourseOperation> cangHuiCourseOperations) {
        this.user = user;
        this.cangHuiCourseOperations = cangHuiCourseOperations;
    }




    public static CangHuiAllCoursesOperationBuilder builder(){
        return new CangHuiAllCoursesOperationBuilder();
    }


    public static class CangHuiAllCoursesOperationBuilder{
        private User user;

        private ArrayList<CangHuiCourseOperation> cangHuiCourseOperations = new ArrayList<>();


        public CangHuiAllCoursesOperationBuilder user(User user){
            this.user = user;
            return this;
        }

        /**
         * 构建课程操作对象
         */
        private void buildCourseOperations(){
            //获取全部课程
//                        CourseRequest allCourseList = null;
            MyCourseData myCourseData = null;

            while((myCourseData= com.cbq.yatori.core.action.canghui.CourseAction.myCourseList(user))==null);

            for (MyCourse list : myCourseData.getLists()) {
                Course courseInform = list.getCourse();
                cangHuiCourseOperations.add(CangHuiCourseOperation.builder().user(user).course(courseInform).build());
            }
        }


        public CangHuiAllCoursesOperation build(){

            return new CangHuiAllCoursesOperation(user,cangHuiCourseOperations);
        }

    }
}
