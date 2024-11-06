package com.cbq.yatori.core.action.canghui.entity.coursedetail;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class Datum {
    @JsonProperty("id")
    private long id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("type")
    private long type;
    @JsonProperty("videoDuration")
    private long videoDuration;
}
