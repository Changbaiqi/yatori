package com.cbq.yatori.core.action.enaea.entity.coursevidelist;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request
 */
@lombok.Data
public class CourseVideoListRequest {
    @lombok.Getter(onMethod_ = {@JsonProperty("courseContentsTotalCount")})
    @lombok.Setter(onMethod_ = {@JsonProperty("courseContentsTotalCount")})
    private long courseContentsTotalCount;
    @lombok.Getter(onMethod_ = {@JsonProperty("id")})
    @lombok.Setter(onMethod_ = {@JsonProperty("id")})
    private long id;
    @lombok.Getter(onMethod_ = {@JsonProperty("result")})
    @lombok.Setter(onMethod_ = {@JsonProperty("result")})
    private Result result;
}
