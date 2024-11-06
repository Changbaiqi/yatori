package com.cbq.yatori.core.action.enaea.entity.ccvideo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request
 */
@lombok.Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CCVideoRequest {
    @JsonProperty("message")
    private String message;
    @JsonProperty("success")
    private boolean success;
    private String SCFUCKPKey; //用于提交学时的Cookie的Key值
    private String SCFUCKPValue;//用于提交学时的Cookie的Value值
}
