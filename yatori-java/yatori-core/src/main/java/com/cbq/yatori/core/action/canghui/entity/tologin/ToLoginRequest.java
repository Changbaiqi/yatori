package com.cbq.yatori.core.action.canghui.entity.tologin;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request
 */
@lombok.Data
public class ToLoginRequest {
    @JsonProperty("account")
    private String account;
    @JsonProperty("code")
    private String code;
    @JsonProperty("password")
    private String password;
}
