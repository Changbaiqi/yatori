package com.cbq.yatori.core.action.canghui.entity.mycourselistresponse;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class LastSection {
    @JsonProperty("attachments")
    private Object attachments;
    @JsonProperty("chapter")
    private Object chapter;
    @JsonProperty("chapterId")
    private long chapterId;
    @JsonProperty("createdAt")
    private String createdAt;
    @JsonProperty("deletedAt")
    private Object deletedAt;
    @JsonProperty("id")
    private long id;
    @JsonProperty("key")
    private String key;
    @JsonProperty("link")
    private String link;
    @JsonProperty("name")
    private String name;
    @JsonProperty("process")
    private Object process;
    @JsonProperty("sort")
    private long sort;
    @JsonProperty("updatedAt")
    private String updatedAt;
    @JsonProperty("video")
    private String video;
    @JsonProperty("videoDuration")
    private long videoDuration;
    @JsonProperty("videoName")
    private String videoName;
    @JsonProperty("viewCount")
    private long viewCount;
}
