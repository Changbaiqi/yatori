package com.cbq.yatori.core.action.yinghua.entity.examstart;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class StartEaxmData {
    @JsonProperty("studyId")
    private long studyId;
}
