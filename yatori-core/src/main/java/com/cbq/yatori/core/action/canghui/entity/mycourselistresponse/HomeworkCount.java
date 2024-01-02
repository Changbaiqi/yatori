package com.cbq.yatori.core.action.canghui.entity.mycourselistresponse;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class HomeworkCount {
    @JsonProperty("completeCount")
    private long completeCount;
    @JsonProperty("totalCount")
    private long totalCount;
}
