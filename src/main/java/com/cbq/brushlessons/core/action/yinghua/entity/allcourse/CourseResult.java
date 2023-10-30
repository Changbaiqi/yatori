package com.cbq.brushlessons.core.action.yinghua.entity.allcourse;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@lombok.Data
public class CourseResult {
    @lombok.Getter(onMethod_ = {@JsonProperty("announcement")})
    @lombok.Setter(onMethod_ = {@JsonProperty("announcement")})
    private CourseAnnouncement announcement;
    @lombok.Getter(onMethod_ = {@JsonProperty("banner")})
    @lombok.Setter(onMethod_ = {@JsonProperty("banner")})
    private List<Banner> banner;
    @lombok.Getter(onMethod_ = {@JsonProperty("enlist")})
    @lombok.Setter(onMethod_ = {@JsonProperty("enlist")})
    private Enlist enlist;
    @lombok.Getter(onMethod_ = {@JsonProperty("finish")})
    @lombok.Setter(onMethod_ = {@JsonProperty("finish")})
    private List<CourseFinishInform> finish;
    @lombok.Getter(onMethod_ = {@JsonProperty("list")})
    @lombok.Setter(onMethod_ = {@JsonProperty("list")})
    private List<CourseInform> list;
}
