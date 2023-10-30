package com.cbq.brushlessons.core.action.yinghua.entity.allcourse;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@lombok.Data
public class Enlist {
    @lombok.Getter(onMethod_ = {@JsonProperty("list")})
    @lombok.Setter(onMethod_ = {@JsonProperty("list")})
    private List<EnlistList> list;
    @lombok.Getter(onMethod_ = {@JsonProperty("pageInfo")})
    @lombok.Setter(onMethod_ = {@JsonProperty("pageInfo")})
    private PageInfo pageInfo;
}
