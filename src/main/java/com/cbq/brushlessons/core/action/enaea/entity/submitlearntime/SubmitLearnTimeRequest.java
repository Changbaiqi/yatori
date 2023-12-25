package com.cbq.brushlessons.core.action.enaea.entity.submitlearntime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request
 */
@lombok.Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubmitLearnTimeRequest {
    @lombok.Getter(onMethod_ = {@JsonProperty("message")})
    @lombok.Setter(onMethod_ = {@JsonProperty("message")})
    private String message;
    @lombok.Getter(onMethod_ = {@JsonProperty("progress")})
    @lombok.Setter(onMethod_ = {@JsonProperty("progress")})
    private long progress;
    @lombok.Getter(onMethod_ = {@JsonProperty("success")})
    @lombok.Setter(onMethod_ = {@JsonProperty("success")})
    private boolean success; //true代表提交学时成功，false代表提交失败
}
