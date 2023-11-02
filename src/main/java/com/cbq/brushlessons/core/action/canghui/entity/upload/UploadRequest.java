package com.cbq.brushlessons.core.action.canghui.entity.upload;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request
 */
@lombok.Data
public class UploadRequest {
    @lombok.Getter(onMethod_ = {@JsonProperty("code")})
    @lombok.Setter(onMethod_ = {@JsonProperty("code")})
    private long code;
    @lombok.Getter(onMethod_ = {@JsonProperty("data")})
    @lombok.Setter(onMethod_ = {@JsonProperty("data")})
    private Object data;
    @lombok.Getter(onMethod_ = {@JsonProperty("msg")})
    @lombok.Setter(onMethod_ = {@JsonProperty("msg")})
    private String msg;
    @lombok.Getter(onMethod_ = {@JsonProperty("sub_code")})
    @lombok.Setter(onMethod_ = {@JsonProperty("sub_code")})
    private long subCode;
    @lombok.Getter(onMethod_ = {@JsonProperty("sub_msg")})
    @lombok.Setter(onMethod_ = {@JsonProperty("sub_msg")})
    private Object subMsg;
}
