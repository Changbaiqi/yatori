package com.cbq.yatori.core.action.canghui.entity.examsubmit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class TopicAnswer {
    @JsonProperty("answers")
    private List<String> answers;
    @JsonProperty("id")
    private long id;
}