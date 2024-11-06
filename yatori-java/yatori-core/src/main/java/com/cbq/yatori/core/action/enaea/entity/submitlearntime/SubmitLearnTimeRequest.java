package com.cbq.yatori.core.action.enaea.entity.submitlearntime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request
 */
@lombok.Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubmitLearnTimeRequest {
    @JsonProperty("message")
    private String message;
    @JsonProperty("progress")
    private long progress;
    @JsonProperty("success")
    private boolean success; //true代表提交学时成功，false代表提交失败
}
