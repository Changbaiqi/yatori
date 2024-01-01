package com.cbq.yatori.core.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.event.SyncReadListener;
import com.cbq.yatori.core.entity.Topic;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: TODO Excel表格相关操作的类
 * @author 长白崎
 * @date 2023/11/27 20:59
 * @version 1.0
 */
public class ExcelUtils {

    /**
     * 用于获取Excel题库
     * @param fileName 文件路径名称
     * @return 返回题库对象列表
     */
    public static List<Topic> getTopics(String fileName){
        final List<Topic> list = new ArrayList<>();
        EasyExcel.read(fileName, Topic.class,new SyncReadListener(){
                //EasyExcel在读取excel表格时，每读取到一行，就会调用一次这个方法，
                //并且将读取到的行数据，封装到指定类型（User）的对象中，传递给我们（Object object）
            /*
            此问题可能出现在低版本的easyExcel中，出现时可以按照下列方式解决
                如果表格数据不是顶行写的，需要通过headRowNumber指定表头行的数量
                如果表格数据不是顶列写的，需要在封装的实体属性上通过@ExcelProperty将实体属性和表格列名进行对应*/

            @Override
            public void invoke(Object object, AnalysisContext context) {
                list.add((Topic) object);
            }
        }).doReadAll();
        return list;
    }
}
