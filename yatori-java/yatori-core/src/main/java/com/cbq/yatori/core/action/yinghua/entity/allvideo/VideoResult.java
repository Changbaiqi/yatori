package com.cbq.yatori.core.action.yinghua.entity.allvideo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@lombok.Data
public class VideoResult {
    @JsonProperty("list")
    private List<VideoList> list;
}
