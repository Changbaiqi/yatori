package com.cbq.yatori.core.action.yinghua.entity.allvideo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request
 */
@lombok.Data
public class VideoRequest {
    @JsonProperty("_code")
    private long code;
    @JsonProperty("msg")
    private String msg;
    @JsonProperty("result")
    private VideoResult result;
    @JsonProperty("status")
    private boolean status;
}
