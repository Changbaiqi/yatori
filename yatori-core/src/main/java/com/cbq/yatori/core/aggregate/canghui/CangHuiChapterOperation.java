package com.cbq.yatori.core.aggregate.canghui;

import com.cbq.yatori.core.entity.User;

public class CangHuiChapterOperation {
    private User user;
    private CangHuiAllVideosOperation cangHuiAllVideosOperation;



    public static CangHuiChapterOperationBuilder builder(){
        return new CangHuiChapterOperationBuilder();
    }


    public static class CangHuiChapterOperationBuilder{
        private User user;

        private CangHuiAllVideosOperation cangHuiAllVideosOperation;




        public CangHuiChapterOperationBuilder user(User user){
            this.user = user;
            return this;
        }

        private void buildAllVideosOperation(){

        }


        public CangHuiChapterOperation build(){
            return new CangHuiChapterOperation();
        }
    }
}
