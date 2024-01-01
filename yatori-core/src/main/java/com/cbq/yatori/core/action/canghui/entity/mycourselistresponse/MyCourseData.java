package com.cbq.yatori.core.action.canghui.entity.mycourselistresponse;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@lombok.Data
public class MyCourseData {
    @lombok.Getter(onMethod_ = {@JsonProperty("lists")})
    @lombok.Setter(onMethod_ = {@JsonProperty("lists")})
    private List<MyCourse> lists;
    @lombok.Getter(onMethod_ = {@JsonProperty("pageNo")})
    @lombok.Setter(onMethod_ = {@JsonProperty("pageNo")})
    private long pageNo;
    @lombok.Getter(onMethod_ = {@JsonProperty("total")})
    @lombok.Setter(onMethod_ = {@JsonProperty("total")})
    private long total;
}
