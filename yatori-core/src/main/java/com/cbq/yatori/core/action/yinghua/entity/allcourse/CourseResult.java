package com.cbq.yatori.core.action.yinghua.entity.allcourse;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@lombok.Data
public class CourseResult {
    @JsonProperty("announcement")
    private CourseAnnouncement announcement;
    @JsonProperty("banner")
    private List<Banner> banner;
    @JsonProperty("enlist")
    private Enlist enlist;
    @JsonProperty("finish")
    private List<CourseFinishInform> finish;
    @JsonProperty("list")
    private List<CourseInform> list;
}
