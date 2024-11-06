package com.cbq.yatori.core.action.enaea.entity.requirecourselist;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request
 */
@lombok.Data
public class RequiredCourseListRequest {
    @JsonProperty("id")
    private long id;
    @JsonProperty("result")
    private Result result;
    @JsonProperty("totalCount")
    private long totalCount;
}
