package com.cbq.yatori.core.aggregate.yinghua.entity;

public enum LoginStatus {
    LOGIN_MISTAKE_CODE, //登录失败，验证码错误
    LOGIN_MISTAKE_AP, //登录失败，账号或者密码错误
    LOGIN_SUCCESS, //登录成功
}
