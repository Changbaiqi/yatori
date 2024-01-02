package com.cbq.yatori.core.action.enaea.entity.requirecourselist;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class ListElement {
    @JsonProperty("canRemove")
    private boolean canRemove;
    @JsonProperty("canView")
    private boolean canView;
    @JsonProperty("circleId")
    private Object circleId;
    @JsonProperty("commentCount")
    private long commentCount;
    @JsonProperty("courseId")
    private Object courseId;
    @JsonProperty("endTime")
    private Object endTime;
    @JsonProperty("homeworkStatus")
    private Object homeworkStatus;
    @JsonProperty("id")
    private Object id;
    @JsonProperty("introduce")
    private Object introduce;
    @JsonProperty("isEvaluated")
    private Object isEvaluated;
    @JsonProperty("isReAnswer")
    private boolean isReAnswer;
    @JsonProperty("isRecommend")
    private Object isRecommend;
    @JsonProperty("isRequired")
    private boolean isRequired;
    @JsonProperty("liveCourseShowDTO")
    private Object liveCourseShowDTO;
    @JsonProperty("passScore")
    private Object passScore;
    @JsonProperty("reAnswerReason")
    private Object reAnswerReason;
    @JsonProperty("remark")
    private String remark;
    @JsonProperty("resourceId")
    private Object resourceId;
    @JsonProperty("resourceRandomPaperId")
    private Object resourceRandomPaperId;
    @JsonProperty("score")
    private Object score;
    @JsonProperty("scoreTime")
    private Object scoreTime;
    @JsonProperty("screenName")
    private Object screenName;
    @JsonProperty("startTime")
    private Object startTime;
    @JsonProperty("studyCenterDTO")
    private StudyCenterDTO studyCenterDTO;
    @JsonProperty("submitTime")
    private Object submitTime;
    @JsonProperty("supportCount")
    private long supportCount;
    @JsonProperty("syllabusResourceId")
    private long syllabusResourceId;
    @JsonProperty("teacherName")
    private String teacherName;
    @JsonProperty("thesisStatus")
    private Object thesisStatus;
    @JsonProperty("title")
    private Object title;
    @JsonProperty("totalScore")
    private Object totalScore;
}
