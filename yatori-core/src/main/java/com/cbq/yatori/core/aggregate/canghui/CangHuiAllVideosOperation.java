package com.cbq.yatori.core.aggregate.canghui;

import com.cbq.yatori.core.entity.User;

import java.util.ArrayList;

public class CangHuiAllVideosOperation {

    private User user;
    private ArrayList<CangHuiVideoOperation> cangHuiVideoOperations;




    public static CangHuiAllVideosOperationBuilder builder(){
        return new CangHuiAllVideosOperationBuilder();
    }

    public static class CangHuiAllVideosOperationBuilder{
        private User user;
        private ArrayList<CangHuiVideoOperation> cangHuiVideoOperations;


        public CangHuiAllVideosOperationBuilder user(User user){
            this.user =user;
            return this;
        }

        public void buildVideOperations(){

        }

        public CangHuiAllVideosOperationBuilder build(){
            return new CangHuiAllVideosOperationBuilder();
        }
    }
}
