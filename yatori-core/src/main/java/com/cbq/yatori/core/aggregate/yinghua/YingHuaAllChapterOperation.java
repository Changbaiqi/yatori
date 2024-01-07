package com.cbq.yatori.core.aggregate.yinghua;

import com.cbq.yatori.core.action.yinghua.entity.allcourse.CourseInform;
import com.cbq.yatori.core.action.yinghua.entity.allvideo.VideoList;

import java.util.ArrayList;
import java.util.List;

public class YingHuaAllChapterOperation {

    private ArrayList<YingHuaChapterOperation> yingHuaChapterOperations; //所有单个章节列表

    public YingHuaAllChapterOperation(ArrayList<YingHuaChapterOperation> yingHuaChapterOperations) {
        this.yingHuaChapterOperations = yingHuaChapterOperations;
    }

    public ArrayList<YingHuaChapterOperation>  getYingHuaChapterOperations(){
        return yingHuaChapterOperations;
    }


    public static YingHuaAllChapterOperationBuilder builder(){
        return new YingHuaAllChapterOperationBuilder();
    }

    public static class YingHuaAllChapterOperationBuilder{

        private List<VideoList> chapterList; //章节原始数据

        private ArrayList<YingHuaChapterOperation> yingHuaChapterOperations;



        public YingHuaAllChapterOperationBuilder chapterList(List<VideoList> chapterList){
            this.chapterList = chapterList;
            return this;
        }

        /**
         * 构建章节对象
         */
        private void buildChapterOperations(){
            for (VideoList videoList : chapterList) {
                yingHuaChapterOperations.add(YingHuaChapterOperation.builder().build());
            }
        }


        public YingHuaAllChapterOperation build(){
            return new YingHuaAllChapterOperation(yingHuaChapterOperations);
        }
    }
}
