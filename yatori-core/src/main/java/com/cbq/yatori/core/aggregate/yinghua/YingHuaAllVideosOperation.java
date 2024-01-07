package com.cbq.yatori.core.aggregate.yinghua;

import com.cbq.yatori.core.action.yinghua.entity.allvideo.NodeList;
import com.cbq.yatori.core.action.yinghua.entity.allvideo.VideoList;

import java.util.ArrayList;
import java.util.List;

/**
 * 全部视屏的操作对象
 */
public class YingHuaAllVideosOperation {

    private ArrayList<YingHuaVideoOperation> yingHuaVideoOperations;


    public ArrayList<YingHuaVideoOperation> getYingHuaVideoOperations(){
        return yingHuaVideoOperations;
    }



    public static YingHuaAllVideosOperationBuilder builder(){
        return new YingHuaAllVideosOperationBuilder();
    }

    public static class YingHuaAllVideosOperationBuilder{


        private List<NodeList> videosList; //对应章节视屏集合

        private ArrayList<YingHuaVideoOperation> yingHuaVideoOperations; //视屏操作列表



        public YingHuaAllVideosOperationBuilder videosList(List<NodeList> nodeLists){
            this.videosList = videosList;
            return this;
        }

        /**
         * 构建视屏列表
         */
        private void buildVideos(){

            for (NodeList nodeList : videosList) {
                yingHuaVideoOperations.add(YingHuaVideoOperation.builder().node(nodeList).build());
            }
        }

        public YingHuaAllVideosOperation build(){

            //构建视屏列表
            buildVideos();
            return new YingHuaAllVideosOperation();
        }
    }
}
