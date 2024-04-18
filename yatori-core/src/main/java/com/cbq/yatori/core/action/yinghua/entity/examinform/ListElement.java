package com.cbq.yatori.core.action.yinghua.entity.examinform;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class ListElement {
    @JsonProperty("addDate")
    private String addDate;
    @JsonProperty("addTime")
    private String addTime;
    @JsonProperty("allow")
    private String allow;
    @JsonProperty("classList")
    private String classList;
    @JsonProperty("courseId")
    private Long courseId;
    @JsonProperty("createUserId")
    private String createUserId;
    @JsonProperty("endTime")
    private String endTime;
    @JsonProperty("finish")
    private Long finish;
    @JsonProperty("flag")
    private Long flag;
    @JsonProperty("frequency")
    private String frequency;
    @JsonProperty("hasCollect")
    private String hasCollect;
    @JsonProperty("id")
    private Long id;
    @JsonProperty("isPrivate")
    private String isPrivate;
    @JsonProperty("limitedTime")
    private Long limitedTime;
    @JsonProperty("nodeId")
    private Long nodeId;
    @JsonProperty("parsing")
    private String parsing;
    @JsonProperty("randData")
    private Object randData;
    @JsonProperty("randNumber")
    private String randNumber;
    @JsonProperty("random")
    private String random;
    @JsonProperty("remarks")
    private String remarks;
    @JsonProperty("schoolId")
    private String schoolId;
    @JsonProperty("score")
    private Long score;
    @JsonProperty("start")
    private Long start;
    @JsonProperty("startTime")
    private String startTime;
    @JsonProperty("teacherType")
    private String teacherType;
    @JsonProperty("title")
    private String title;
    @JsonProperty("topicNumber")
    private Long topicNumber;
    @JsonProperty("type")
    private Long type;
    @JsonProperty("url")
    private String url;
}
