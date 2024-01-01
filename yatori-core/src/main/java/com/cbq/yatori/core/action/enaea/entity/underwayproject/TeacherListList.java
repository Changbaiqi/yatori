package com.cbq.yatori.core.action.enaea.entity.underwayproject;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class TeacherListList {
    @lombok.Getter(onMethod_ = {@JsonProperty("accountPhotoUrl")})
    @lombok.Setter(onMethod_ = {@JsonProperty("accountPhotoUrl")})
    private String accountPhotoUrl;
    @lombok.Getter(onMethod_ = {@JsonProperty("attentioned")})
    @lombok.Setter(onMethod_ = {@JsonProperty("attentioned")})
    private Boolean attentioned;
    @lombok.Getter(onMethod_ = {@JsonProperty("comName")})
    @lombok.Setter(onMethod_ = {@JsonProperty("comName")})
    private Object comName;
    @lombok.Getter(onMethod_ = {@JsonProperty("desc")})
    @lombok.Setter(onMethod_ = {@JsonProperty("desc")})
    private String desc;
    @lombok.Getter(onMethod_ = {@JsonProperty("friend")})
    @lombok.Setter(onMethod_ = {@JsonProperty("friend")})
    private Boolean friend;
    @lombok.Getter(onMethod_ = {@JsonProperty("id")})
    @lombok.Setter(onMethod_ = {@JsonProperty("id")})
    private Long id;
    @lombok.Getter(onMethod_ = {@JsonProperty("isClientOnline")})
    @lombok.Setter(onMethod_ = {@JsonProperty("isClientOnline")})
    private Object isClientOnline;
    @lombok.Getter(onMethod_ = {@JsonProperty("isOnline")})
    @lombok.Setter(onMethod_ = {@JsonProperty("isOnline")})
    private Boolean isOnline;
    @lombok.Getter(onMethod_ = {@JsonProperty("lastLoginTime")})
    @lombok.Setter(onMethod_ = {@JsonProperty("lastLoginTime")})
    private Object lastLoginTime;
    @lombok.Getter(onMethod_ = {@JsonProperty("registerTime")})
    @lombok.Setter(onMethod_ = {@JsonProperty("registerTime")})
    private Object registerTime;
    @lombok.Getter(onMethod_ = {@JsonProperty("screenName")})
    @lombok.Setter(onMethod_ = {@JsonProperty("screenName")})
    private String screenName;
    @lombok.Getter(onMethod_ = {@JsonProperty("shortName")})
    @lombok.Setter(onMethod_ = {@JsonProperty("shortName")})
    private String shortName;
    @lombok.Getter(onMethod_ = {@JsonProperty("tag")})
    @lombok.Setter(onMethod_ = {@JsonProperty("tag")})
    private String tag;
    @lombok.Getter(onMethod_ = {@JsonProperty("username")})
    @lombok.Setter(onMethod_ = {@JsonProperty("username")})
    private String username;
}
