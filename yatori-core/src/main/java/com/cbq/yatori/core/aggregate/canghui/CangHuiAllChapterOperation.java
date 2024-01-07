package com.cbq.yatori.core.aggregate.canghui;


import com.cbq.yatori.core.action.canghui.entity.coursedetail.Chapter;

import com.cbq.yatori.core.entity.User;

import java.util.ArrayList;
import java.util.List;

public class CangHuiAllChapterOperation {
    private ArrayList<CangHuiChapterOperation> cangHuiChapterOperations;

    public static CangHuiAllChapterOperationBuilder builder(){
        return new CangHuiAllChapterOperationBuilder();
    }


    public static class CangHuiAllChapterOperationBuilder{

        private User user;
        private List<Chapter> chapterList;
        private ArrayList<CangHuiChapterOperation> cangHuiChapterOperations;

        public CangHuiAllChapterOperationBuilder user(User user){
            this.user = user;
            return this;
        }
        public CangHuiAllChapterOperationBuilder chapterList(List<Chapter> chapterList){
            this.chapterList = chapterList;
            return this;
        }


        private void buildChapterOperation(){

        }
        public CangHuiAllChapterOperation build(){

            //构建章节
            buildChapterOperation();


            return new CangHuiAllChapterOperation();
        }
    }
}
