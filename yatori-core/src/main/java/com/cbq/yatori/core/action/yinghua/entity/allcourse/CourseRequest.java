package com.cbq.yatori.core.action.yinghua.entity.allcourse;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request
 */
@lombok.Data
public class CourseRequest {
    @lombok.Getter(onMethod_ = {@JsonProperty("_code")})
    @lombok.Setter(onMethod_ = {@JsonProperty("_code")})
    private long code;
    @lombok.Getter(onMethod_ = {@JsonProperty("msg")})
    @lombok.Setter(onMethod_ = {@JsonProperty("msg")})
    private String msg;
    @lombok.Getter(onMethod_ = {@JsonProperty("result")})
    @lombok.Setter(onMethod_ = {@JsonProperty("result")})
    private CourseResult result;
    @lombok.Getter(onMethod_ = {@JsonProperty("status")})
    @lombok.Setter(onMethod_ = {@JsonProperty("status")})
    private boolean status;
}
