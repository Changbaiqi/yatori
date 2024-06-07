package com.cbq.yatori.core.aggregate.yinghua.course;

import com.cbq.yatori.core.entity.User;

public abstract class YingHuaCourseAbstractChain {
    public abstract boolean action(YingHuaCourse course, YingHuaCourseAbstractChain chain, User user, Object o);

}
