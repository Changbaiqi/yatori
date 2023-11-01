package com.cbq.brushlessons.core.action.canghui.entity.mycourselistresponse;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class MissionStandard {
    @lombok.Getter(onMethod_ = {@JsonProperty("homeworkCount")})
    @lombok.Setter(onMethod_ = {@JsonProperty("homeworkCount")})
    private long homeworkCount;
    @lombok.Getter(onMethod_ = {@JsonProperty("questionCount")})
    @lombok.Setter(onMethod_ = {@JsonProperty("questionCount")})
    private long questionCount;
    @lombok.Getter(onMethod_ = {@JsonProperty("quizCount")})
    @lombok.Setter(onMethod_ = {@JsonProperty("quizCount")})
    private long quizCount;
    @lombok.Getter(onMethod_ = {@JsonProperty("replyCount")})
    @lombok.Setter(onMethod_ = {@JsonProperty("replyCount")})
    private long replyCount;
    @lombok.Getter(onMethod_ = {@JsonProperty("video")})
    @lombok.Setter(onMethod_ = {@JsonProperty("video")})
    private long video;
}
