package com.cbq.yatori.core.aggregate.yinghua;

import com.cbq.yatori.core.action.yinghua.entity.allcourse.CourseInform;
import com.cbq.yatori.core.action.yinghua.entity.allvideo.NodeList;
import com.cbq.yatori.core.action.yinghua.entity.allvideo.VideoList;
import com.cbq.yatori.core.entity.User;

import java.util.ArrayList;
import java.util.List;

public class YingHuaChapterOperation {

    private User user;
    private CourseInform courseInform;
    private YingHuaAllVideosOperation yingHuaAllVideosOperation; //章节列表


    public YingHuaChapterOperation(User user, CourseInform courseInform, YingHuaAllVideosOperation yingHuaAllVideosOperation) {
        this.user = user;
        this.courseInform = courseInform;
        this.yingHuaAllVideosOperation = yingHuaAllVideosOperation;
    }




    public YingHuaAllVideosOperation getVideoOperation(){
        return YingHuaAllVideosOperation.builder().build();
    }


    public static YingHuaChapterOperationBuilder builder(){
        return new YingHuaChapterOperationBuilder();
    }
    public static class YingHuaChapterOperationBuilder{

        private User user;
        private CourseInform courseInform;
        private VideoList chapter; //章节

        private YingHuaAllVideosOperation yingHuaAllVideosOperation;
        private List<NodeList> nodeLists;//对应视屏节点列表


        public YingHuaChapterOperationBuilder chapter(VideoList chapter){
            this.chapter = chapter;
            return this;
        }

        /**
         * 构建视屏列表
         */
        private void buildAllVideosOperation(){

            nodeLists = chapter.getNodeList();

            yingHuaAllVideosOperation = YingHuaAllVideosOperation.builder()
                    .user(user)
                    .videosList(nodeLists)
                    .build();
        }


        public YingHuaChapterOperation build(){

            //构建章节
            buildAllVideosOperation();

            return new YingHuaChapterOperation(user,courseInform,yingHuaAllVideosOperation);
        }
    }
}
