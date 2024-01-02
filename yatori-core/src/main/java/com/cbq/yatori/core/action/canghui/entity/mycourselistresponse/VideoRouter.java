package com.cbq.yatori.core.action.canghui.entity.mycourselistresponse;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@lombok.Data
public class VideoRouter {
    /**
     * 这个存的是真正的视屏课程
     */
    @JsonProperty("data")
    private List<RouterDatum> data;
}
