package com.cbq.brushlessons.core.action.canghui.entity.tologin;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request
 */
@lombok.Data
public class ToLoginRequest {
    @lombok.Getter(onMethod_ = {@JsonProperty("account")})
    @lombok.Setter(onMethod_ = {@JsonProperty("account")})
    private String account;
    @lombok.Getter(onMethod_ = {@JsonProperty("code")})
    @lombok.Setter(onMethod_ = {@JsonProperty("code")})
    private String code;
    @lombok.Getter(onMethod_ = {@JsonProperty("password")})
    @lombok.Setter(onMethod_ = {@JsonProperty("password")})
    private String password;
}
