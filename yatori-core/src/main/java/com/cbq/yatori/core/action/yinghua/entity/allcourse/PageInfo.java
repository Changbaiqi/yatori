package com.cbq.yatori.core.action.yinghua.entity.allcourse;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class PageInfo {
    @lombok.Getter(onMethod_ = {@JsonProperty("keyName")})
    @lombok.Setter(onMethod_ = {@JsonProperty("keyName")})
    private String keyName;
    @lombok.Getter(onMethod_ = {@JsonProperty("onlyCount")})
    @lombok.Setter(onMethod_ = {@JsonProperty("onlyCount")})
    private long onlyCount;
    @lombok.Getter(onMethod_ = {@JsonProperty("page")})
    @lombok.Setter(onMethod_ = {@JsonProperty("page")})
    private long page;
    @lombok.Getter(onMethod_ = {@JsonProperty("pageCount")})
    @lombok.Setter(onMethod_ = {@JsonProperty("pageCount")})
    private long pageCount;
    @lombok.Getter(onMethod_ = {@JsonProperty("pageSize")})
    @lombok.Setter(onMethod_ = {@JsonProperty("pageSize")})
    private long pageSize;
    @lombok.Getter(onMethod_ = {@JsonProperty("recordsCount")})
    @lombok.Setter(onMethod_ = {@JsonProperty("recordsCount")})
    private long recordsCount;
}
