package com.cbq.yatori.core.action.canghui.entity.loginresponse;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request
 */
@lombok.Data
public class LoginResponseRequest {
    @JsonProperty("code")
    private long code;
    @JsonProperty("data")
    private LoginResponseData data;
    @JsonProperty("msg")
    private String msg;
    @JsonProperty("sub_code")
    private long subCode;
    @JsonProperty("sub_msg")
    private Object subMsg;
}
