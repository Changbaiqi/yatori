package com.cbq.brushlessons.core.action.canghui.entity.mycourselistresponse;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@lombok.Data
public class Router {
    @lombok.Getter(onMethod_ = {@JsonProperty("data")})
    @lombok.Setter(onMethod_ = {@JsonProperty("data")})
    private List<RouterDatum> data;
}
