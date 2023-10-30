package com.cbq.brushlessons.core.action.yinghua.entity.videomessage;

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
    @lombok.Getter(onMethod_ = {@JsonProperty("_code")})
    @lombok.Setter(onMethod_ = {@JsonProperty("_code")})
    /**
     * 状态码，0代表获取成功
     */
    private long code;
    @lombok.Getter(onMethod_ = {@JsonProperty("msg")})
    @lombok.Setter(onMethod_ = {@JsonProperty("msg")})
    private String msg;
    @lombok.Getter(onMethod_ = {@JsonProperty("result")})
    @lombok.Setter(onMethod_ = {@JsonProperty("result")})
    private VideoInformResult result;
    @lombok.Getter(onMethod_ = {@JsonProperty("status")})
    @lombok.Setter(onMethod_ = {@JsonProperty("status")})
    private boolean status;
}
