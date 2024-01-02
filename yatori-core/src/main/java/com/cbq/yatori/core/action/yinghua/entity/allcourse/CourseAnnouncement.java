package com.cbq.yatori.core.action.yinghua.entity.allcourse;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class CourseAnnouncement {
    @JsonProperty("count")
    private long count;
    @JsonProperty("id")
    private long id;
    @JsonProperty("title")
    private String title;
}
