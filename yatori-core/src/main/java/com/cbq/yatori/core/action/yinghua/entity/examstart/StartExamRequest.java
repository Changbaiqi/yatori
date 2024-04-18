package com.cbq.yatori.core.action.yinghua.entity.examstart;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request
 */
@lombok.Data
public class StartExamRequest {
    @JsonProperty("_code")
    private long code;
    @JsonProperty("msg")
    private String msg;
    @JsonProperty("result")
    private StartExamResult startExamResult;
    @JsonProperty("status")
    private boolean status;
}
