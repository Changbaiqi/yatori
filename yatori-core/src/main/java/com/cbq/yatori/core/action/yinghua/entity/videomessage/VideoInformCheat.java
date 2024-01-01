package com.cbq.yatori.core.action.yinghua.entity.videomessage;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VideoInformCheat {
    @lombok.Getter(onMethod_ = {@JsonProperty("duration")})
    @lombok.Setter(onMethod_ = {@JsonProperty("duration")})
    private long duration;
    @lombok.Getter(onMethod_ = {@JsonProperty("state")})
    @lombok.Setter(onMethod_ = {@JsonProperty("state")})
    private long state;
}
