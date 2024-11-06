
// Result.java

package com.cbq.yatori.core.action.enaea.entity.underwayproject;

import com.fasterxml.jackson.annotation.*;
import java.util.List;

@lombok.Data
public class Result {
    @JsonProperty("list")
    private List<ResultList> list;
}