package com.cbq.yatori.core.aggregate.canghui;

import com.cbq.yatori.core.action.canghui.entity.coursedetail.Chapter;
import com.cbq.yatori.core.entity.User;

public class CangHuiChapterOperation {
    private User user;
    private CangHuiAllVideosOperation cangHuiAllVideosOperation;

    public CangHuiChapterOperation(User user, CangHuiAllVideosOperation cangHuiAllVideosOperation) {
        this.user = user;
        this.cangHuiAllVideosOperation = cangHuiAllVideosOperation;
    }

    public static CangHuiChapterOperationBuilder builder(){
        return new CangHuiChapterOperationBuilder();
    }


    public static class CangHuiChapterOperationBuilder{
        private User user;
        private Chapter chapter; //章节原始数据
        private long semesterId;

        private CangHuiAllVideosOperation cangHuiAllVideosOperation;




        public CangHuiChapterOperationBuilder user(User user){
            this.user = user;
            return this;
        }
        public CangHuiChapterOperationBuilder semesterId(long semesterId){
            this.semesterId = semesterId;
            return this;
        }

        public CangHuiChapterOperationBuilder chapter(Chapter chapter){
            this.chapter = chapter;
            return this;
        }

        private void buildAllVideosOperation(){

            cangHuiAllVideosOperation = CangHuiAllVideosOperation.builder()
                    .user(user)
                    .semesterId(semesterId)
                    .sections(chapter.getSections())
                    .build();
        }


        public CangHuiChapterOperation build(){
            //构建全部视屏
            buildAllVideosOperation();
            return new CangHuiChapterOperation(user,cangHuiAllVideosOperation);
        }
    }
}
