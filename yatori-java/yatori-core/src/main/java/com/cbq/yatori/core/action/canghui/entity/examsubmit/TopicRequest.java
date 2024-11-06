package com.cbq.yatori.core.action.canghui.entity.examsubmit;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Request
 */
@lombok.Data
public class TopicRequest {
    @JsonProperty("answers")
    private List<TopicAnswer> answers;
    @JsonProperty("examId")
    private String examId;
    @JsonProperty("id")
    private String id;
}



