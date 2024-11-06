package com.cbq.yatori.core.action.yinghua.entity.submitstudy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * @description: 提交学时之后返回的数据
 * @author 长白崎
 * @date 2023/10/30 2:31
 * @version 1.0
 */
@lombok.Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubmitStudyTimeRequest {
    @JsonProperty("_code")
    private long code;
    @JsonProperty("msg")
    private String msg;
    @JsonProperty("result")
    private SubmitResult result;
    @JsonProperty("status")
    private boolean status;
}
