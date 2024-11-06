package com.cbq.yatori.core.aggregate.yinghua.course;

import com.cbq.yatori.core.action.yinghua.CourseAction;
import com.cbq.yatori.core.action.yinghua.entity.allcourse.CourseRequest;
import com.cbq.yatori.core.entity.User;

import java.util.ArrayList;

public class YingHuaCourse{

    private ArrayList<YingHuaCourseAbstractChain> chains=new ArrayList<>();

    private User user;

    public static void getCourseListAction(){
        new YingHuaCourse().getCourseListAction();
    }

    public boolean action(User user, Object o){
        CourseRequest courseRequest = courseAction();
        //责任链
//        chains.forEach(yingHuaAbstractCourse ->{if(!yingHuaAbstractCourse.action(user, courseRequest)) return;});
        return false;
    }

    private CourseRequest courseAction(){
        //获取全部课程
        CourseRequest allCourseList = null;
        while((allCourseList= CourseAction.getAllCourseRequest(user))==null);
        return allCourseList;
    }

    public void addOperation(YingHuaCourseAbstractChain yingHuaAbstractCourse){
        chains.add(yingHuaAbstractCourse);
    }
}
