package com.cbq.yatori.core.aggregate.canghui;

import com.cbq.yatori.core.action.canghui.CourseAction;
import com.cbq.yatori.core.action.canghui.entity.coursedetail.Chapter;
import com.cbq.yatori.core.action.canghui.entity.coursedetail.CourseDetailData;
import com.cbq.yatori.core.action.canghui.entity.coursedetail.Process;
import com.cbq.yatori.core.action.canghui.entity.coursedetail.Section;
import com.cbq.yatori.core.action.canghui.entity.mycourselistresponse.Course;
import com.cbq.yatori.core.action.canghui.entity.mycourselistresponse.RouterDatum;
import com.cbq.yatori.core.entity.User;

public class CangHuiCourseOperation {
    private User user;

    private CangHuiAllChapterOperation cangHuiAllChapterOperation;

    public CangHuiCourseOperation(User user, CangHuiAllChapterOperation cangHuiAllChapterOperation) {
        this.user = user;
        this.cangHuiAllChapterOperation = cangHuiAllChapterOperation;
    }

    public static CangHuiCourseOperationBuilder builder(){
        return null;
    }

    public static class CangHuiCourseOperationBuilder{

        private User user;

        private CangHuiAllChapterOperation cangHuiAllChapterOperation;

        private Course course;


        public CangHuiCourseOperationBuilder user(User user){
            this.user = user;
            return this;
        }


        public CangHuiCourseOperationBuilder course(Course course){
            this.course = course;
            return this;
        }

        /**
         * 构建章节
         */
        public void buildAllChapterOperation(){
            long id1 = course.getId(); //课程id

            //详细页面获取进度并赋值--------------------------
            CourseDetailData courseDetail = null;
            while ((courseDetail = CourseAction.getCourseDetail(user, id1)) == null) ;
            //章节
            if (courseDetail.getChapters() != null)
                cangHuiAllChapterOperation = CangHuiAllChapterOperation.builder()
                        .user(user)
                        .semesterId(courseDetail.getCurrentSemesterId())
                        .chapterList(courseDetail.getChapters()).build();
        }

        public CangHuiCourseOperation build(){
            return new CangHuiCourseOperation(user,cangHuiAllChapterOperation);
        }
    }
}
