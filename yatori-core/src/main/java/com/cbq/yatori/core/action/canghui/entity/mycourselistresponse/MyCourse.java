package com.cbq.yatori.core.action.canghui.entity.mycourselistresponse;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class MyCourse {
    @lombok.Getter(onMethod_ = {@JsonProperty("course")})
    @lombok.Setter(onMethod_ = {@JsonProperty("course")})
    private Course course;
    @lombok.Getter(onMethod_ = {@JsonProperty("courseId")})
    @lombok.Setter(onMethod_ = {@JsonProperty("courseId")})
    private Long courseId;
    @lombok.Getter(onMethod_ = {@JsonProperty("createdAt")})
    @lombok.Setter(onMethod_ = {@JsonProperty("createdAt")})
    private String createdAt;
    @lombok.Getter(onMethod_ = {@JsonProperty("deletedAt")})
    @lombok.Setter(onMethod_ = {@JsonProperty("deletedAt")})
    private Object deletedAt;
    @lombok.Getter(onMethod_ = {@JsonProperty("endTime")})
    @lombok.Setter(onMethod_ = {@JsonProperty("endTime")})
    private String endTime;
    @lombok.Getter(onMethod_ = {@JsonProperty("homeworkCount")})
    @lombok.Setter(onMethod_ = {@JsonProperty("homeworkCount")})
    private HomeworkCount homeworkCount;
    @lombok.Getter(onMethod_ = {@JsonProperty("id")})
    @lombok.Setter(onMethod_ = {@JsonProperty("id")})
    private Long id;
    @lombok.Getter(onMethod_ = {@JsonProperty("lastPosition")})
    @lombok.Setter(onMethod_ = {@JsonProperty("lastPosition")})
    private Long lastPosition;
    @lombok.Getter(onMethod_ = {@JsonProperty("lastSection")})
    @lombok.Setter(onMethod_ = {@JsonProperty("lastSection")})
    private LastSection lastSection;
    @lombok.Getter(onMethod_ = {@JsonProperty("lastSectionId")})
    @lombok.Setter(onMethod_ = {@JsonProperty("lastSectionId")})
    private Long lastSectionId;
    @lombok.Getter(onMethod_ = {@JsonProperty("member")})
    @lombok.Setter(onMethod_ = {@JsonProperty("member")})
    private Member member;
    @lombok.Getter(onMethod_ = {@JsonProperty("memberId")})
    @lombok.Setter(onMethod_ = {@JsonProperty("memberId")})
    private Long memberId;
    @lombok.Getter(onMethod_ = {@JsonProperty("progress")})
    @lombok.Setter(onMethod_ = {@JsonProperty("progress")})
    private Long progress;
    @lombok.Getter(onMethod_ = {@JsonProperty("progressDetail")})
    @lombok.Setter(onMethod_ = {@JsonProperty("progressDetail")})
    private ProgressDetail progressDetail;
    @lombok.Getter(onMethod_ = {@JsonProperty("quizCount")})
    @lombok.Setter(onMethod_ = {@JsonProperty("quizCount")})
    private QuizCount quizCount;
    @lombok.Getter(onMethod_ = {@JsonProperty("resultScore")})
    @lombok.Setter(onMethod_ = {@JsonProperty("resultScore")})
    private ResultScore resultScore;
    @lombok.Getter(onMethod_ = {@JsonProperty("sectionCount")})
    @lombok.Setter(onMethod_ = {@JsonProperty("sectionCount")})
    private SectionCount sectionCount;
    @lombok.Getter(onMethod_ = {@JsonProperty("semester")})
    @lombok.Setter(onMethod_ = {@JsonProperty("semester")})
    private Semester semester;
    @lombok.Getter(onMethod_ = {@JsonProperty("semesterId")})
    @lombok.Setter(onMethod_ = {@JsonProperty("semesterId")})
    private Long semesterId;
    @lombok.Getter(onMethod_ = {@JsonProperty("startTime")})
    @lombok.Setter(onMethod_ = {@JsonProperty("startTime")})
    private String startTime;
    @lombok.Getter(onMethod_ = {@JsonProperty("status")})
    @lombok.Setter(onMethod_ = {@JsonProperty("status")})
    private Long status;
    @lombok.Getter(onMethod_ = {@JsonProperty("updatedAt")})
    @lombok.Setter(onMethod_ = {@JsonProperty("updatedAt")})
    private String updatedAt;
}
