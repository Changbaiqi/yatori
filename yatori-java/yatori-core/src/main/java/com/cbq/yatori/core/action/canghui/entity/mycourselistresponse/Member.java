package com.cbq.yatori.core.action.canghui.entity.mycourselistresponse;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class Member {
    @JsonProperty("account")
    private String account;
    @JsonProperty("age")
    private long age;
    @JsonProperty("avatar")
    private String avatar;
    @JsonProperty("birthday")
    private Object birthday;
    @JsonProperty("classId")
    private Object classId;
    @JsonProperty("collegeId")
    private Object collegeId;
    @JsonProperty("createdAt")
    private String createdAt;
    @JsonProperty("deletedAt")
    private Object deletedAt;
    @JsonProperty("email")
    private String email;
    @JsonProperty("gender")
    private long gender;
    @JsonProperty("gradeId")
    private Object gradeId;
    @JsonProperty("id")
    private long id;
    @JsonProperty("idCard")
    private String idCard;
    @JsonProperty("identity")
    private long identity;
    @JsonProperty("isAdmin")
    private boolean isAdmin;
    @JsonProperty("lastLoginDuration")
    private long lastLoginDuration;
    @JsonProperty("lastLoginIp")
    private String lastLoginIp;
    @JsonProperty("lastLoginTime")
    private long lastLoginTime;
    @JsonProperty("loginCount")
    private long loginCount;
    @JsonProperty("majorId")
    private Object majorId;
    @JsonProperty("memberRolesId")
    private Object memberRolesId;
    @JsonProperty("nickName")
    private String nickName;
    @JsonProperty("openId")
    private String openId;
    @JsonProperty("password")
    private String password;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("realName")
    private String realName;
    @JsonProperty("schoolId")
    private long schoolId;
    @JsonProperty("sign")
    private String sign;
    @JsonProperty("status")
    private long status;
    @JsonProperty("studentNo")
    private String studentNo;
    @JsonProperty("totalLoginDuration")
    private long totalLoginDuration;
    @JsonProperty("unionId")
    private String unionId;
    @JsonProperty("updatedAt")
    private String updatedAt;
    @JsonProperty("weChat")
    private String weChat;
    @JsonProperty("wxAppOpenId")
    private String wxAppOpenId;
}
