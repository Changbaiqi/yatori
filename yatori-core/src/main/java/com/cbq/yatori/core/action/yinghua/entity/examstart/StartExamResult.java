package com.cbq.yatori.core.action.yinghua.entity.examstart;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class StartExamResult {
    @JsonProperty("data")
    private StartEaxmData startEaxmData;
}
