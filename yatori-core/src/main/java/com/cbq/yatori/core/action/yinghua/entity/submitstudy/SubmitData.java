package com.cbq.yatori.core.action.yinghua.entity.submitstudy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubmitData {
    @lombok.Getter(onMethod_ = {@JsonProperty("studyId")})
    @lombok.Setter(onMethod_ = {@JsonProperty("studyId")})
    private long studyId;
}
