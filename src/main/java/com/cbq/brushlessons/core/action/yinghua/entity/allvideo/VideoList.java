package com.cbq.brushlessons.core.action.yinghua.entity.allvideo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@lombok.Data
public class VideoList {
    @lombok.Getter(onMethod_ = {@JsonProperty("id")})
    @lombok.Setter(onMethod_ = {@JsonProperty("id")})
    private long id;
    @lombok.Getter(onMethod_ = {@JsonProperty("idx")})
    @lombok.Setter(onMethod_ = {@JsonProperty("idx")})
    private long idx;
    @lombok.Getter(onMethod_ = {@JsonProperty("name")})
    @lombok.Setter(onMethod_ = {@JsonProperty("name")})
    private String name;
    @lombok.Getter(onMethod_ = {@JsonProperty("nodeList")})
    @lombok.Setter(onMethod_ = {@JsonProperty("nodeList")})
    private List<NodeList> nodeList;
}
