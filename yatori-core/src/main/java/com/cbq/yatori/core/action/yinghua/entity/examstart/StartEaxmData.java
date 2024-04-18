package com.cbq.yatori.core.action.yinghua.entity.examstart;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class StartEaxmData {
    @lombok.Getter(onMethod_ = {@JsonProperty("studyId")})
    @lombok.Setter(onMethod_ = {@JsonProperty("studyId")})
    private long studyId;
}
