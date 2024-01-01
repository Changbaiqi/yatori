package com.cbq.yatori.core.action.canghui.entity.coursedetail;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@lombok.Data
public class Chapter {
    @lombok.Getter(onMethod_ = {@JsonProperty("course")})
    @lombok.Setter(onMethod_ = {@JsonProperty("course")})
    private Object course;
    @lombok.Getter(onMethod_ = {@JsonProperty("courseId")})
    @lombok.Setter(onMethod_ = {@JsonProperty("courseId")})
    private long courseId;
    @lombok.Getter(onMethod_ = {@JsonProperty("createdAt")})
    @lombok.Setter(onMethod_ = {@JsonProperty("createdAt")})
    private String createdAt;
    @lombok.Getter(onMethod_ = {@JsonProperty("deletedAt")})
    @lombok.Setter(onMethod_ = {@JsonProperty("deletedAt")})
    private Object deletedAt;
    @lombok.Getter(onMethod_ = {@JsonProperty("id")})
    @lombok.Setter(onMethod_ = {@JsonProperty("id")})
    private long id;
    @lombok.Getter(onMethod_ = {@JsonProperty("name")})
    @lombok.Setter(onMethod_ = {@JsonProperty("name")})
    private String name;
    @lombok.Getter(onMethod_ = {@JsonProperty("sections")})
    @lombok.Setter(onMethod_ = {@JsonProperty("sections")})
    private List<Section> sections;
    @lombok.Getter(onMethod_ = {@JsonProperty("sort")})
    @lombok.Setter(onMethod_ = {@JsonProperty("sort")})
    private long sort;
    @lombok.Getter(onMethod_ = {@JsonProperty("updatedAt")})
    @lombok.Setter(onMethod_ = {@JsonProperty("updatedAt")})
    private String updatedAt;
}
