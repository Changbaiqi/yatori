
// Result.java

package com.cbq.brushlessons.core.action.enaea.entity.underwayproject;

import com.fasterxml.jackson.annotation.*;
import java.util.List;

@lombok.Data
public class Result {
    @lombok.Getter(onMethod_ = {@JsonProperty("list")})
    @lombok.Setter(onMethod_ = {@JsonProperty("list")})
    private List<ResultList> list;
}