package com.cbq.yatori.core.aggregate;

import com.cbq.yatori.core.entity.User;
import lombok.Builder;


/**
 * @description: TODO 统一用户对象信息接口
 * @author 长白崎
 * @date 2024/5/15 14:13
 * @version 1.0
 */
public abstract class YatoriUser {

    public abstract YatoriLogin toLogin(); //核心登录方法

    public abstract void preOperation(); //前置操作
    public abstract void postOperation(); //后置操作
}
