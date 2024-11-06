package com.cbq.yatori.core.action.yinghua.entity.submitstudy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubmitData {
    @JsonProperty("studyId")
    private long studyId;
}
