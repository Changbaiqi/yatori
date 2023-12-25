package com.cbq.brushlessons.core.action.enaea.entity.coursevidelist;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class VideoListList {
    @lombok.Getter(onMethod_ = {@JsonProperty("quality")})
    @lombok.Setter(onMethod_ = {@JsonProperty("quality")})
    private String quality;
    @lombok.Getter(onMethod_ = {@JsonProperty("videoSize")})
    @lombok.Setter(onMethod_ = {@JsonProperty("videoSize")})
    private Object videoSize;
    @lombok.Getter(onMethod_ = {@JsonProperty("videoUrl")})
    @lombok.Setter(onMethod_ = {@JsonProperty("videoUrl")})
    private String videoUrl;
}
