package com.cbq.yatori.core.action.yinghua.entity.allcourse;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request
 */
@lombok.Data
public class CourseRequest {
    @JsonProperty("_code")
    private long code;
    @JsonProperty("msg")
    private String msg;
    @JsonProperty("result")
    private CourseResult result;
    @JsonProperty("status")
    private boolean status;
}
