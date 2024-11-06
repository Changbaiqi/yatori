
// UnderwayProjectRquest.java

package com.cbq.yatori.core.action.enaea.entity.underwayproject;

import com.fasterxml.jackson.annotation.*;

/**
 * Request
 */
@lombok.Data
public class UnderwayProjectRquest {
    @JsonProperty("id")
    private long id;
    @JsonProperty("result")
    private Result result;
    @JsonProperty("totalCount")
    private long totalCount;
}