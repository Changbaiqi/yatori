package com.cbq.yatori.core.aggregate.canghui;


import com.cbq.yatori.core.action.canghui.entity.coursedetail.Chapter;

import com.cbq.yatori.core.entity.User;

import java.util.ArrayList;
import java.util.List;

public class CangHuiAllChapterOperation {
    private User user;
    private ArrayList<CangHuiChapterOperation> cangHuiChapterOperations;


    public CangHuiAllChapterOperation(User user, ArrayList<CangHuiChapterOperation> cangHuiChapterOperations) {
        this.user = user;
        this.cangHuiChapterOperations = cangHuiChapterOperations;
    }

    public static CangHuiAllChapterOperationBuilder builder(){
        return new CangHuiAllChapterOperationBuilder();
    }


    public static class CangHuiAllChapterOperationBuilder{

        private User user;
        private long semesterId;
        private List<Chapter> chapterList;
        private ArrayList<CangHuiChapterOperation> cangHuiChapterOperations;

        public CangHuiAllChapterOperationBuilder user(User user){
            this.user = user;
            return this;
        }
        public CangHuiAllChapterOperationBuilder semesterId(long semesterId){
            this.semesterId = semesterId;
            return this;
        }
        public CangHuiAllChapterOperationBuilder chapterList(List<Chapter> chapterList){
            this.chapterList = chapterList;
            return this;
        }


        private void buildChapterOperation(){
            ArrayList<CangHuiChapterOperation> list  = new ArrayList<>();
            for (Chapter chapter : chapterList) {
                list.add(CangHuiChapterOperation.builder()
                        .user(user)
                        .semesterId(semesterId)
                        .chapter(chapter)
                        .build());
            }
        }
        public CangHuiAllChapterOperation build(){

            //构建章节
            buildChapterOperation();


            return new CangHuiAllChapterOperation(user,cangHuiChapterOperations);
        }
    }
}
