package com.cbq.yatori.core.action.canghui.entity.loginresponse;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class LoginResponseData {
    @JsonProperty("account")
    private String account;
    @JsonProperty("age")
    private long age;
    @JsonProperty("avatar")
    private String avatar;
    @JsonProperty("birthday")
    private Object birthday;
    @JsonProperty("collegeId")
    private long collegeId;
    @JsonProperty("email")
    private String email;
    @JsonProperty("gender")
    private long gender;
    @JsonProperty("id")
    private long id;
    @JsonProperty("iDCard")
    private String iDCard;
    @JsonProperty("identity")
    private long identity;
    @JsonProperty("isAdmin")
    private boolean isAdmin;
    @JsonProperty("memberRolesId")
    private Object memberRolesId;
    @JsonProperty("permission")
    private Object permission;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("realName")
    private String realName;
    @JsonProperty("schoolId")
    private long schoolId;
    @JsonProperty("schoolType")
    private long schoolType;
    @JsonProperty("status")
    private long status;
    @JsonProperty("studentNo")
    private String studentNo;
    @JsonProperty("token")
    private String token;
    @JsonProperty("weChat")
    private String weChat;
}
