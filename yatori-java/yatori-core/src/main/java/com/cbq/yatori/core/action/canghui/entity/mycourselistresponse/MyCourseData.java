package com.cbq.yatori.core.action.canghui.entity.mycourselistresponse;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@lombok.Data
public class MyCourseData {
    @JsonProperty("lists")
    private List<MyCourse> lists;
    @JsonProperty("pageNo")
    private long pageNo;
    @JsonProperty("total")
    private long total;
}
