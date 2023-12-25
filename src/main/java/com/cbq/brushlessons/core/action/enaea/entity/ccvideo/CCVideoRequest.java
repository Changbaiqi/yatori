package com.cbq.brushlessons.core.action.enaea.entity.ccvideo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request
 */
@lombok.Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CCVideoRequest {
    @lombok.Getter(onMethod_ = {@JsonProperty("message")})
    @lombok.Setter(onMethod_ = {@JsonProperty("message")})
    private String message;
    @lombok.Getter(onMethod_ = {@JsonProperty("success")})
    @lombok.Setter(onMethod_ = {@JsonProperty("success")})
    private boolean success;
    private String SCFUCKPKey; //用于提交学时的Cookie的Key值
    private String SCFUCKPValue;//用于提交学时的Cookie的Value值
}
