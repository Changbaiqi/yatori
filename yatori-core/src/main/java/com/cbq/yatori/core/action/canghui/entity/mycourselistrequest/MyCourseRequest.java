package com.cbq.yatori.core.action.canghui.entity.mycourselistrequest;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request
 */
@lombok.Data
public class MyCourseRequest {
    @JsonProperty("page")
    private long page;
    @JsonProperty("pageSize")
    /**
     * 页面长度
     */
    private long pageSize;
    @JsonProperty("status")
    private long status;
}
