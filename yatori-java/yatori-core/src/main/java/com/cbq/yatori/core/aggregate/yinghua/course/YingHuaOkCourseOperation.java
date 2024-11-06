package com.cbq.yatori.core.aggregate.yinghua.course;

import com.cbq.yatori.core.action.yinghua.entity.allcourse.CourseRequest;
import com.cbq.yatori.core.entity.User;

public class YingHuaOkCourseOperation extends YingHuaCourseAbstractChain {

    @Override
    public boolean action(YingHuaCourse course, YingHuaCourseAbstractChain chain, User user, Object o) {
        return false;
    }
}
