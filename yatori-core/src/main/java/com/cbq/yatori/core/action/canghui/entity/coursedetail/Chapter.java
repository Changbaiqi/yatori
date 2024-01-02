package com.cbq.yatori.core.action.canghui.entity.coursedetail;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@lombok.Data
public class Chapter {
    @JsonProperty("course")
    private Object course;
    @JsonProperty("courseId")
    private long courseId;
    @JsonProperty("createdAt")
    private String createdAt;
    @JsonProperty("deletedAt")
    private Object deletedAt;
    @JsonProperty("id")
    private long id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("sections")
    private List<Section> sections;
    @JsonProperty("sort")
    private long sort;
    @JsonProperty("updatedAt")
    private String updatedAt;
}
