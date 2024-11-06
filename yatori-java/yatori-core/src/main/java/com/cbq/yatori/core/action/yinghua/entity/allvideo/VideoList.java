package com.cbq.yatori.core.action.yinghua.entity.allvideo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@lombok.Data
public class VideoList {
    @JsonProperty("id")
    private long id;
    @JsonProperty("idx")
    private long idx;
    @JsonProperty("name")
    private String name;
    @JsonProperty("nodeList")
    private List<NodeList> nodeList;
}
