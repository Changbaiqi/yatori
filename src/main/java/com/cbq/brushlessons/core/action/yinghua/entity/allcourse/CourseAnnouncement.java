package com.cbq.brushlessons.core.action.yinghua.entity.allcourse;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class CourseAnnouncement {
    @lombok.Getter(onMethod_ = {@JsonProperty("count")})
    @lombok.Setter(onMethod_ = {@JsonProperty("count")})
    private long count;
    @lombok.Getter(onMethod_ = {@JsonProperty("id")})
    @lombok.Setter(onMethod_ = {@JsonProperty("id")})
    private long id;
    @lombok.Getter(onMethod_ = {@JsonProperty("title")})
    @lombok.Setter(onMethod_ = {@JsonProperty("title")})
    private String title;
}
