package com.cbq.yatori.core.aggregate.canghui;

import com.cbq.yatori.core.action.canghui.entity.coursedetail.Section;
import com.cbq.yatori.core.entity.User;

import java.util.ArrayList;
import java.util.List;

public class CangHuiAllVideosOperation {

    private User user;
    private ArrayList<CangHuiVideoOperation> cangHuiVideoOperations;


    public CangHuiAllVideosOperation(User user, ArrayList<CangHuiVideoOperation> cangHuiVideoOperations) {
        this.user = user;
        this.cangHuiVideoOperations = cangHuiVideoOperations;
    }

    public static CangHuiAllVideosOperationBuilder builder(){
        return new CangHuiAllVideosOperationBuilder();
    }

    public static class CangHuiAllVideosOperationBuilder{
        private User user;
        private long semesterId;
        private List<Section> sections; //对应章节视屏内容列表
        private ArrayList<CangHuiVideoOperation> cangHuiVideoOperations;


        public CangHuiAllVideosOperationBuilder user(User user){
            this.user =user;
            return this;
        }
        public CangHuiAllVideosOperationBuilder semesterId(long semesterId){
            this.semesterId = semesterId;
            return this;
        }
        public CangHuiAllVideosOperationBuilder sections(List<Section> sections){
            this.sections = sections;
            return this;
        }

        public void buildVideOperations(){
            ArrayList<CangHuiVideoOperation> list = new ArrayList<>();
            for (Section section : sections) {
                list.add(CangHuiVideoOperation.builder()
                        .user(user)
                        .section(section)
                        .build());
            }
        }

        public CangHuiAllVideosOperation build(){
            //构建视屏操作对象
            buildVideOperations();
            return new CangHuiAllVideosOperation(user,cangHuiVideoOperations);
        }
    }
}
