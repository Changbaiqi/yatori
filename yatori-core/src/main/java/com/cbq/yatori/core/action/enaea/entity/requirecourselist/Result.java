package com.cbq.yatori.core.action.enaea.entity.requirecourselist;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@lombok.Data
public class Result {
    @JsonProperty("list")
    private List<ListElement> list;
}
