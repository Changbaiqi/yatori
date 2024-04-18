package com.cbq.yatori.core.aggregate.yinghua;

import com.cbq.yatori.core.action.yinghua.CourseAction;
import com.cbq.yatori.core.action.yinghua.entity.allcourse.CourseInform;
import com.cbq.yatori.core.action.yinghua.entity.allvideo.VideoList;
import com.cbq.yatori.core.action.yinghua.entity.allvideo.VideoRequest;
import com.cbq.yatori.core.entity.User;

import java.util.List;


/**
 * @description: TODO 单课程操作对象
 * @author 长白崎
 * @date 2024/1/4 19:40
 * @version 1.0
 */
public class YingHuaCourseOperation  {

    private YingHuaAllChapterOperation yingHuaAllChapterOperation;

    public YingHuaCourseOperation(YingHuaAllChapterOperation yingHuaAllChapterOperation) {
        this.yingHuaAllChapterOperation = yingHuaAllChapterOperation;
    }

    public YingHuaAllChapterOperation getAllChapterOperation(){
        return YingHuaAllChapterOperation.builder().build();
    }




    public static YingHuaCourseOperationBuilder builder(){
        return new YingHuaCourseOperationBuilder();
    }

    public static class YingHuaCourseOperationBuilder{

        private User user;
        private CourseInform courseInform;

        private YingHuaAllChapterOperation yingHuaAllChapterOperation; //所有章节的操作对象



        public YingHuaCourseOperationBuilder user(User user){
            this.user = user;
            return this;
        }

        public YingHuaCourseOperationBuilder courseInform(CourseInform courseInform){
            this.courseInform = courseInform;
            return this;
        }


        /**
         * 构建全部章节的操作对象
         */
        public void buildAllChapterOperation(){

            //初始化视屏列表
            VideoRequest courseVideosList = null;
            while ((courseVideosList = CourseAction.getCourseVideosList(user, courseInform)) == null) ;
            List<VideoList> list = courseVideosList.getResult().getList();
            //构建全部章节的操作对象
            yingHuaAllChapterOperation = YingHuaAllChapterOperation.builder()
                    .chapterList(list)
                    .build();
        }

        public YingHuaCourseOperation build(){

            //构建全部章节的操作对象
            buildAllChapterOperation();

            return new YingHuaCourseOperation(yingHuaAllChapterOperation);
        }
    }
}
