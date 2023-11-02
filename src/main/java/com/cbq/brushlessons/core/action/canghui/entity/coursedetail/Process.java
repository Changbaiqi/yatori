package com.cbq.brushlessons.core.action.canghui.entity.coursedetail;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class Process {
    @lombok.Getter(onMethod_ = {@JsonProperty("endTime")})
    @lombok.Setter(onMethod_ = {@JsonProperty("endTime")})
    private String endTime;
    @lombok.Getter(onMethod_ = {@JsonProperty("id")})
    @lombok.Setter(onMethod_ = {@JsonProperty("id")})
    private long id;
    @lombok.Getter(onMethod_ = {@JsonProperty("progress")})
    @lombok.Setter(onMethod_ = {@JsonProperty("progress")})
    private long progress;
    @lombok.Getter(onMethod_ = {@JsonProperty("startTime")})
    @lombok.Setter(onMethod_ = {@JsonProperty("startTime")})
    private String startTime;
    @lombok.Getter(onMethod_ = {@JsonProperty("status")})
    @lombok.Setter(onMethod_ = {@JsonProperty("status")})
    private long status;
    @lombok.Getter(onMethod_ = {@JsonProperty("studyCount")})
    @lombok.Setter(onMethod_ = {@JsonProperty("studyCount")})
    private long studyCount;
    @lombok.Getter(onMethod_ = {@JsonProperty("totalProgress")})
    @lombok.Setter(onMethod_ = {@JsonProperty("totalProgress")})
    private long totalProgress;
    @lombok.Getter(onMethod_ = {@JsonProperty("type")})
    @lombok.Setter(onMethod_ = {@JsonProperty("type")})
    private long type;
}
