package com.cbq.yatori.core.action.canghui.entity.mycourselistresponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class SectionCount {

    @JsonProperty("completeCount")
    private long completeCount;

    @JsonProperty("totalCount")
    private long totalCount;
}
