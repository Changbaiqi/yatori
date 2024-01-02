package com.cbq.yatori.core.action.yinghua.entity.allcourse;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class PageInfo {
    @JsonProperty("keyName")
    private String keyName;
    @JsonProperty("onlyCount")
    private long onlyCount;
    @JsonProperty("page")
    private long page;
    @JsonProperty("pageCount")
    private long pageCount;
    @JsonProperty("pageSize")
    private long pageSize;
    @JsonProperty("recordsCount")
    private long recordsCount;
}
