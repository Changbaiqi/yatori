package com.cbq.yatori.core.action.canghui.entity.coursedetail;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class Process {
    @JsonProperty("endTime")
    private String endTime;
    @JsonProperty("id")
    private long id;
    @JsonProperty("progress")
    private long progress;
    @JsonProperty("startTime")
    private String startTime;
    @JsonProperty("status")
    private long status;
    @JsonProperty("studyCount")
    private long studyCount;
    @JsonProperty("totalProgress")
    private long totalProgress;
    @JsonProperty("type")
    private long type;
}
