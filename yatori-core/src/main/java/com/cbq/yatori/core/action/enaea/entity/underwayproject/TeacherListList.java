package com.cbq.yatori.core.action.enaea.entity.underwayproject;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class TeacherListList {
    @JsonProperty("accountPhotoUrl")
    private String accountPhotoUrl;
    @JsonProperty("attentioned")
    private Boolean attentioned;
    @JsonProperty("comName")
    private Object comName;
    @JsonProperty("desc")
    private String desc;
    @JsonProperty("friend")
    private Boolean friend;
    @JsonProperty("id")
    private Long id;
    @JsonProperty("isClientOnline")
    private Object isClientOnline;
    @JsonProperty("isOnline")
    private Boolean isOnline;
    @JsonProperty("lastLoginTime")
    private Object lastLoginTime;
    @JsonProperty("registerTime")
    private Object registerTime;
    @JsonProperty("screenName")
    private String screenName;
    @JsonProperty("shortName")
    private String shortName;
    @JsonProperty("tag")
    private String tag;
    @JsonProperty("username")
    private String username;
}
