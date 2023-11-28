package com.cbq.brushlessons.core.action.canghui.entity.examsubmit;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Request
 */
@lombok.Data
public class TopicRequest {
    @lombok.Getter(onMethod_ = {@JsonProperty("answers")})
    @lombok.Setter(onMethod_ = {@JsonProperty("answers")})
    private List<TopicAnswer> answers;
    @lombok.Getter(onMethod_ = {@JsonProperty("examId")})
    @lombok.Setter(onMethod_ = {@JsonProperty("examId")})
    private String examId;
    @lombok.Getter(onMethod_ = {@JsonProperty("id")})
    @lombok.Setter(onMethod_ = {@JsonProperty("id")})
    private String id;
}



