package com.cbq.yatori.core.action.yinghua.entity.videomessage;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @description: 单个视屏的详细观看信息
 * @author 长白崎
 * @date 2023/10/30 2:32
 * @version 1.0
 */
@lombok.Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VideoInformRequest {
    @JsonProperty("_code")
    /**
     * 状态码，0代表获取成功
     */
    private long code;
    @JsonProperty("msg")
    private String msg;
    @JsonProperty("result")
    private VideoInformResult result;
    @JsonProperty("status")
    private boolean status;
}
