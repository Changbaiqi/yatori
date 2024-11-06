package com.cbq.yatori.core.action.canghui.entity.coursedetail;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class College {
    @JsonProperty("createdAt")
    private String createdAt;
    @JsonProperty("deletedAt")
    private Object deletedAt;
    @JsonProperty("id")
    private long id;
    @JsonProperty("members")
    private Object members;
    @JsonProperty("name")
    private String name;
    @JsonProperty("schoolId")
    private long schoolId;
    @JsonProperty("status")
    private long status;
    @JsonProperty("summary")
    private String summary;
    @JsonProperty("updatedAt")
    private String updatedAt;
}
