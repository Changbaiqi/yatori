package com.cbq.yatori.core.action.canghui.entity.coursedetail;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class MissionStandard {
    @JsonProperty("homeworkCount")
    private long homeworkCount;
    @JsonProperty("questionCount")
    private long questionCount;
    @JsonProperty("quizCount")
    private long quizCount;
    @JsonProperty("replyCount")
    private long replyCount;
    @JsonProperty("video")
    private long video;
}
