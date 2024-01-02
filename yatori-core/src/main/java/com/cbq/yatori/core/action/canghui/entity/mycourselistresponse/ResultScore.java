package com.cbq.yatori.core.action.canghui.entity.mycourselistresponse;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class ResultScore {
    @JsonProperty("discussScore")
    private long discussScore;
    @JsonProperty("examScore")
    private long examScore;
    @JsonProperty("extraScore")
    private long extraScore;
    @JsonProperty("homeworkScore")
    private long homeworkScore;
    @JsonProperty("quizScore")
    private long quizScore;
    @JsonProperty("totalScore")
    private long totalScore;
    @JsonProperty("videoScore")
    private long videoScore;
}
