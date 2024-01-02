package com.cbq.yatori.core.action.yinghua.entity.videomessage;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VideoInformResult {
    @JsonProperty("data")
    private VideoInformData data;
}
