package com.cbq.yatori.core.action.yinghua.entity.examinform;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request
 */
@lombok.Data
public class ExamInformRequest {
    @JsonProperty("_code")
    private long code;
    @JsonProperty("msg")
    private String msg;
    @JsonProperty("result")
    private ExamInformResult examInformResult;
    @JsonProperty("status")
    private boolean status;
}
