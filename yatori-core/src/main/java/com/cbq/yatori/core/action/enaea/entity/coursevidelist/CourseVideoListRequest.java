package com.cbq.yatori.core.action.enaea.entity.coursevidelist;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request
 */
@lombok.Data
public class CourseVideoListRequest {
    @JsonProperty("courseContentsTotalCount")
    private long courseContentsTotalCount;
    @JsonProperty("id")
    private long id;
    @JsonProperty("result")
    private Result result;
}
