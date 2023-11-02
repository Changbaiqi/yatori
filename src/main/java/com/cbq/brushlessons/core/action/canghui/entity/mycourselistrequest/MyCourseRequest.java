package com.cbq.brushlessons.core.action.canghui.entity.mycourselistrequest;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request
 */
@lombok.Data
public class MyCourseRequest {
    @lombok.Getter(onMethod_ = {@JsonProperty("page")})
    @lombok.Setter(onMethod_ = {@JsonProperty("page")})
    private long page;
    @lombok.Getter(onMethod_ = {@JsonProperty("pageSize")})
    @lombok.Setter(onMethod_ = {@JsonProperty("pageSize")})
    /**
     * 页面长度
     */
    private long pageSize;
    @lombok.Getter(onMethod_ = {@JsonProperty("status")})
    @lombok.Setter(onMethod_ = {@JsonProperty("status")})
    private long status;
}
