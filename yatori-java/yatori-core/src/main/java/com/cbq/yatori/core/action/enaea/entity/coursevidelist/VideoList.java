package com.cbq.yatori.core.action.enaea.entity.coursevidelist;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@lombok.Data
public class VideoList {
    @JsonProperty("list")
    private List<VideoListList> list;
}
