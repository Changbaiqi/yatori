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
    @lombok.Getter(onMethod_ = {@JsonProperty("answers")})
    @lombok.Setter(onMethod_ = {@JsonProperty("answers")})
    private List<String> answers;
    @lombok.Getter(onMethod_ = {@JsonProperty("id")})
    @lombok.Setter(onMethod_ = {@JsonProperty("id")})
    private long id;
}