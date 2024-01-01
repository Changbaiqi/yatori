package com.cbq.yatori.core.action.canghui.entity.loginresponse;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class LoginResponseData {
    @lombok.Getter(onMethod_ = {@JsonProperty("account")})
    @lombok.Setter(onMethod_ = {@JsonProperty("account")})
    private String account;
    @lombok.Getter(onMethod_ = {@JsonProperty("age")})
    @lombok.Setter(onMethod_ = {@JsonProperty("age")})
    private long age;
    @lombok.Getter(onMethod_ = {@JsonProperty("avatar")})
    @lombok.Setter(onMethod_ = {@JsonProperty("avatar")})
    private String avatar;
    @lombok.Getter(onMethod_ = {@JsonProperty("birthday")})
    @lombok.Setter(onMethod_ = {@JsonProperty("birthday")})
    private Object birthday;
    @lombok.Getter(onMethod_ = {@JsonProperty("collegeId")})
    @lombok.Setter(onMethod_ = {@JsonProperty("collegeId")})
    private long collegeId;
    @lombok.Getter(onMethod_ = {@JsonProperty("email")})
    @lombok.Setter(onMethod_ = {@JsonProperty("email")})
    private String email;
    @lombok.Getter(onMethod_ = {@JsonProperty("gender")})
    @lombok.Setter(onMethod_ = {@JsonProperty("gender")})
    private long gender;
    @lombok.Getter(onMethod_ = {@JsonProperty("id")})
    @lombok.Setter(onMethod_ = {@JsonProperty("id")})
    private long id;
    @lombok.Getter(onMethod_ = {@JsonProperty("iDCard")})
    @lombok.Setter(onMethod_ = {@JsonProperty("iDCard")})
    private String iDCard;
    @lombok.Getter(onMethod_ = {@JsonProperty("identity")})
    @lombok.Setter(onMethod_ = {@JsonProperty("identity")})
    private long identity;
    @lombok.Getter(onMethod_ = {@JsonProperty("isAdmin")})
    @lombok.Setter(onMethod_ = {@JsonProperty("isAdmin")})
    private boolean isAdmin;
    @lombok.Getter(onMethod_ = {@JsonProperty("memberRolesId")})
    @lombok.Setter(onMethod_ = {@JsonProperty("memberRolesId")})
    private Object memberRolesId;
    @lombok.Getter(onMethod_ = {@JsonProperty("permission")})
    @lombok.Setter(onMethod_ = {@JsonProperty("permission")})
    private Object permission;
    @lombok.Getter(onMethod_ = {@JsonProperty("phone")})
    @lombok.Setter(onMethod_ = {@JsonProperty("phone")})
    private String phone;
    @lombok.Getter(onMethod_ = {@JsonProperty("realName")})
    @lombok.Setter(onMethod_ = {@JsonProperty("realName")})
    private String realName;
    @lombok.Getter(onMethod_ = {@JsonProperty("schoolId")})
    @lombok.Setter(onMethod_ = {@JsonProperty("schoolId")})
    private long schoolId;
    @lombok.Getter(onMethod_ = {@JsonProperty("schoolType")})
    @lombok.Setter(onMethod_ = {@JsonProperty("schoolType")})
    private long schoolType;
    @lombok.Getter(onMethod_ = {@JsonProperty("status")})
    @lombok.Setter(onMethod_ = {@JsonProperty("status")})
    private long status;
    @lombok.Getter(onMethod_ = {@JsonProperty("studentNo")})
    @lombok.Setter(onMethod_ = {@JsonProperty("studentNo")})
    private String studentNo;
    @lombok.Getter(onMethod_ = {@JsonProperty("token")})
    @lombok.Setter(onMethod_ = {@JsonProperty("token")})
    private String token;
    @lombok.Getter(onMethod_ = {@JsonProperty("weChat")})
    @lombok.Setter(onMethod_ = {@JsonProperty("weChat")})
    private String weChat;
}
