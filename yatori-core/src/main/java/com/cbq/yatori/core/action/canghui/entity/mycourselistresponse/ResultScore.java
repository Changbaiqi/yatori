package com.cbq.yatori.core.action.canghui.entity.mycourselistresponse;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class ResultScore {
    @lombok.Getter(onMethod_ = {@JsonProperty("discussScore")})
    @lombok.Setter(onMethod_ = {@JsonProperty("discussScore")})
    private long discussScore;
    @lombok.Getter(onMethod_ = {@JsonProperty("examScore")})
    @lombok.Setter(onMethod_ = {@JsonProperty("examScore")})
    private long examScore;
    @lombok.Getter(onMethod_ = {@JsonProperty("extraScore")})
    @lombok.Setter(onMethod_ = {@JsonProperty("extraScore")})
    private long extraScore;
    @lombok.Getter(onMethod_ = {@JsonProperty("homeworkScore")})
    @lombok.Setter(onMethod_ = {@JsonProperty("homeworkScore")})
    private long homeworkScore;
    @lombok.Getter(onMethod_ = {@JsonProperty("quizScore")})
    @lombok.Setter(onMethod_ = {@JsonProperty("quizScore")})
    private long quizScore;
    @lombok.Getter(onMethod_ = {@JsonProperty("totalScore")})
    @lombok.Setter(onMethod_ = {@JsonProperty("totalScore")})
    private long totalScore;
    @lombok.Getter(onMethod_ = {@JsonProperty("videoScore")})
    @lombok.Setter(onMethod_ = {@JsonProperty("videoScore")})
    private long videoScore;
}
