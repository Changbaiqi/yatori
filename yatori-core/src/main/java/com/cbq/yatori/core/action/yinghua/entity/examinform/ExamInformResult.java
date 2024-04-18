package com.cbq.yatori.core.action.yinghua.entity.examinform;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@lombok.Data
public class ExamInformResult {
    @JsonProperty("list")
    private List<ListElement> list;
}
