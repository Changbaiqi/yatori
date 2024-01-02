package com.cbq.yatori.core.action.enaea.entity.coursevidelist;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class VideoListList {
    @JsonProperty("quality")
    private String quality;
    @JsonProperty("videoSize")
    private Object videoSize;
    @JsonProperty("videoUrl")
    private String videoUrl;
}
