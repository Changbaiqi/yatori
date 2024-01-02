package com.cbq.yatori.core.action.canghui.entity.submitstudy;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Request
 */
@Data
public class SubmitStudyTimeRequest {


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
