package com.cbq.yatori.core.action.yinghua.entity.allcourse;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@lombok.Data
public class Enlist {
    @JsonProperty("list")
    private List<EnlistList> list;
    @JsonProperty("pageInfo")
    private PageInfo pageInfo;
}
