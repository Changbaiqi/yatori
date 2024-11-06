
// Request.java

package com.cbq.yatori.core.action.canghui.entity.examsubmitrespose;

import com.fasterxml.jackson.annotation.*;

/**
 * Request
 */
@lombok.Data
public class ExamSubmitResponse {
    @JsonProperty("code")
    private long code;
    @JsonProperty("data")
    private Object data;
    @JsonProperty("msg")
    private String msg;
    @JsonProperty("sub_code")
    private long subCode;
    @JsonProperty("sub_msg")
    private Object subMsg;
}
