package com.cbq.yatori.core.action.yinghua.entity.examstart;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class StartExamResult {
    @lombok.Getter(onMethod_ = {@JsonProperty("data")})
    @lombok.Setter(onMethod_ = {@JsonProperty("data")})
    private StartEaxmData startEaxmData;
}
