package com.cbq.yatori.core.action.canghui.entity.mycourselistresponse;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class MyCourse {

    @JsonProperty("course")
    private Course course;

    @JsonProperty("courseId")
    private Long courseId;
    @JsonProperty("createdAt")
    private String createdAt;
    @JsonProperty("deletedAt")
    private Object deletedAt;
    @JsonProperty("endTime")
    private String endTime;
    @JsonProperty("homeworkCount")
    private HomeworkCount homeworkCount;
    @JsonProperty("id")
    private Long id;
    @JsonProperty("lastPosition")
    private Long lastPosition;
    @JsonProperty("lastSection")
    private LastSection lastSection;
    @JsonProperty("lastSectionId")
    private Long lastSectionId;
    @JsonProperty("member")
    private Member member;
    @JsonProperty("memberId")
    private Long memberId;
    @JsonProperty("progress")
    private Long progress;
    @JsonProperty("progressDetail")
    private ProgressDetail progressDetail;
    @JsonProperty("quizCount")
    private QuizCount quizCount;
    @JsonProperty("resultScore")
    private ResultScore resultScore;
    @JsonProperty("sectionCount")
    private SectionCount sectionCount;
    @JsonProperty("semester")
    private Semester semester;
    @JsonProperty("semesterId")
    private Long semesterId;
    @JsonProperty("startTime")
    private String startTime;
    @JsonProperty("status")
    private Long status;
    @JsonProperty("updatedAt")
    private String updatedAt;
}
