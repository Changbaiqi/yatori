package com.cbq.yatori.core.action.canghui.entity.mycourselistresponse;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class Semester {
    @JsonProperty("courseId")
    private long courseId;
    @JsonProperty("createdAt")
    private String createdAt;
    @JsonProperty("deletedAt")
    private Object deletedAt;
    @JsonProperty("endTime")
    private String endTime;
    @JsonProperty("EnrollClasses")
    private String enrollClasses;
    @JsonProperty("enrollScope")
    private long enrollScope;
    @JsonProperty("exams")
    private Object exams;
    @JsonProperty("id")
    private long id;
    @JsonProperty("joinCount")
    private long joinCount;
    @JsonProperty("joinEndTime")
    private long joinEndTime;
    @JsonProperty("joinLimit")
    private long joinLimit;
    @JsonProperty("joinStartTime")
    private long joinStartTime;
    @JsonProperty("memberId")
    private long memberId;
    @JsonProperty("rules")
    private String rules;
    @JsonProperty("schoolId")
    private long schoolId;
    @JsonProperty("startTime")
    private String startTime;
    @JsonProperty("status")
    private long status;
    @JsonProperty("students")
    private Object students;
    @JsonProperty("studentsCount")
    private long studentsCount;
    @JsonProperty("title")
    private String title;
    @JsonProperty("updatedAt")
    private String updatedAt;
    @JsonProperty("viewCount")
    private long viewCount;
}
