package com.cbq.brushlessons.core.action.canghui.entity.coursedetail;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@lombok.Data
public class Section {
    @lombok.Getter(onMethod_ = {@JsonProperty("attachments")})
    @lombok.Setter(onMethod_ = {@JsonProperty("attachments")})
    private List<String> attachments;
    @lombok.Getter(onMethod_ = {@JsonProperty("chapter")})
    @lombok.Setter(onMethod_ = {@JsonProperty("chapter")})
    private Object chapter;
    @lombok.Getter(onMethod_ = {@JsonProperty("chapterId")})
    @lombok.Setter(onMethod_ = {@JsonProperty("chapterId")})
    private long chapterId;
    @lombok.Getter(onMethod_ = {@JsonProperty("createdAt")})
    @lombok.Setter(onMethod_ = {@JsonProperty("createdAt")})
    private String createdAt;
    @lombok.Getter(onMethod_ = {@JsonProperty("deletedAt")})
    @lombok.Setter(onMethod_ = {@JsonProperty("deletedAt")})
    private Object deletedAt;
    @lombok.Getter(onMethod_ = {@JsonProperty("id")})
    @lombok.Setter(onMethod_ = {@JsonProperty("id")})
    private long id;
    @lombok.Getter(onMethod_ = {@JsonProperty("key")})
    @lombok.Setter(onMethod_ = {@JsonProperty("key")})
    private String key;
    @lombok.Getter(onMethod_ = {@JsonProperty("link")})
    @lombok.Setter(onMethod_ = {@JsonProperty("link")})
    private String link;
    @lombok.Getter(onMethod_ = {@JsonProperty("name")})
    @lombok.Setter(onMethod_ = {@JsonProperty("name")})
    private String name;
    @lombok.Getter(onMethod_ = {@JsonProperty("process")})
    @lombok.Setter(onMethod_ = {@JsonProperty("process")})
    private Process process;
    @lombok.Getter(onMethod_ = {@JsonProperty("sort")})
    @lombok.Setter(onMethod_ = {@JsonProperty("sort")})
    private long sort;
    @lombok.Getter(onMethod_ = {@JsonProperty("updatedAt")})
    @lombok.Setter(onMethod_ = {@JsonProperty("updatedAt")})
    private String updatedAt;
    @lombok.Getter(onMethod_ = {@JsonProperty("video")})
    @lombok.Setter(onMethod_ = {@JsonProperty("video")})
    private String video;
    @lombok.Getter(onMethod_ = {@JsonProperty("videoDuration")})
    @lombok.Setter(onMethod_ = {@JsonProperty("videoDuration")})
    private long videoDuration;
    @lombok.Getter(onMethod_ = {@JsonProperty("videoName")})
    @lombok.Setter(onMethod_ = {@JsonProperty("videoName")})
    private String videoName;
    @lombok.Getter(onMethod_ = {@JsonProperty("viewCount")})
    @lombok.Setter(onMethod_ = {@JsonProperty("viewCount")})
    private long viewCount;
}
