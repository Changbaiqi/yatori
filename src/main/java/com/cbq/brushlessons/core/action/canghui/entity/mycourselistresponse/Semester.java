package com.cbq.brushlessons.core.action.canghui.entity.mycourselistresponse;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class Semester {
    @lombok.Getter(onMethod_ = {@JsonProperty("courseId")})
    @lombok.Setter(onMethod_ = {@JsonProperty("courseId")})
    private long courseId;
    @lombok.Getter(onMethod_ = {@JsonProperty("createdAt")})
    @lombok.Setter(onMethod_ = {@JsonProperty("createdAt")})
    private String createdAt;
    @lombok.Getter(onMethod_ = {@JsonProperty("deletedAt")})
    @lombok.Setter(onMethod_ = {@JsonProperty("deletedAt")})
    private Object deletedAt;
    @lombok.Getter(onMethod_ = {@JsonProperty("endTime")})
    @lombok.Setter(onMethod_ = {@JsonProperty("endTime")})
    private String endTime;
    @lombok.Getter(onMethod_ = {@JsonProperty("EnrollClasses")})
    @lombok.Setter(onMethod_ = {@JsonProperty("EnrollClasses")})
    private String enrollClasses;
    @lombok.Getter(onMethod_ = {@JsonProperty("enrollScope")})
    @lombok.Setter(onMethod_ = {@JsonProperty("enrollScope")})
    private long enrollScope;
    @lombok.Getter(onMethod_ = {@JsonProperty("exams")})
    @lombok.Setter(onMethod_ = {@JsonProperty("exams")})
    private Object exams;
    @lombok.Getter(onMethod_ = {@JsonProperty("id")})
    @lombok.Setter(onMethod_ = {@JsonProperty("id")})
    private long id;
    @lombok.Getter(onMethod_ = {@JsonProperty("joinCount")})
    @lombok.Setter(onMethod_ = {@JsonProperty("joinCount")})
    private long joinCount;
    @lombok.Getter(onMethod_ = {@JsonProperty("joinEndTime")})
    @lombok.Setter(onMethod_ = {@JsonProperty("joinEndTime")})
    private long joinEndTime;
    @lombok.Getter(onMethod_ = {@JsonProperty("joinLimit")})
    @lombok.Setter(onMethod_ = {@JsonProperty("joinLimit")})
    private long joinLimit;
    @lombok.Getter(onMethod_ = {@JsonProperty("joinStartTime")})
    @lombok.Setter(onMethod_ = {@JsonProperty("joinStartTime")})
    private long joinStartTime;
    @lombok.Getter(onMethod_ = {@JsonProperty("memberId")})
    @lombok.Setter(onMethod_ = {@JsonProperty("memberId")})
    private long memberId;
    @lombok.Getter(onMethod_ = {@JsonProperty("rules")})
    @lombok.Setter(onMethod_ = {@JsonProperty("rules")})
    private String rules;
    @lombok.Getter(onMethod_ = {@JsonProperty("schoolId")})
    @lombok.Setter(onMethod_ = {@JsonProperty("schoolId")})
    private long schoolId;
    @lombok.Getter(onMethod_ = {@JsonProperty("startTime")})
    @lombok.Setter(onMethod_ = {@JsonProperty("startTime")})
    private String startTime;
    @lombok.Getter(onMethod_ = {@JsonProperty("status")})
    @lombok.Setter(onMethod_ = {@JsonProperty("status")})
    private long status;
    @lombok.Getter(onMethod_ = {@JsonProperty("students")})
    @lombok.Setter(onMethod_ = {@JsonProperty("students")})
    private Object students;
    @lombok.Getter(onMethod_ = {@JsonProperty("studentsCount")})
    @lombok.Setter(onMethod_ = {@JsonProperty("studentsCount")})
    private long studentsCount;
    @lombok.Getter(onMethod_ = {@JsonProperty("title")})
    @lombok.Setter(onMethod_ = {@JsonProperty("title")})
    private String title;
    @lombok.Getter(onMethod_ = {@JsonProperty("updatedAt")})
    @lombok.Setter(onMethod_ = {@JsonProperty("updatedAt")})
    private String updatedAt;
    @lombok.Getter(onMethod_ = {@JsonProperty("viewCount")})
    @lombok.Setter(onMethod_ = {@JsonProperty("viewCount")})
    private long viewCount;
}
