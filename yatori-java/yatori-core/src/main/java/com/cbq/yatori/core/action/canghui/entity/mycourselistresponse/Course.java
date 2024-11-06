package com.cbq.yatori.core.action.canghui.entity.mycourselistresponse;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class Course {
    @JsonProperty("access")
    private long access;
    @JsonProperty("banner")
    private String banner;
    @JsonProperty("categories")
    private Object categories;
    @JsonProperty("chapters")
    private Object chapters;
    @JsonProperty("classHour")
    private long classHour;
    @JsonProperty("code")
    private String code;
    @JsonProperty("collegeId")
    private long collegeId;
    @JsonProperty("cover")
    private String cover;
    @JsonProperty("createdAt")
    private String createdAt;
    @JsonProperty("createUserId")
    private long createUserId;
    @JsonProperty("createUserName")
    private String createUserName;
    @JsonProperty("createUserType")
    private long createUserType;
    @JsonProperty("credit")
    private String credit;
    @JsonProperty("currentSemesterId")
    private long currentSemesterId;
    @JsonProperty("deletedAt")
    private Object deletedAt;
    @JsonProperty("details")
    private String details;
    @JsonProperty("duration")
    private long duration;
    @JsonProperty("id")
    private long id;
    @JsonProperty("isOpen")
    private long isOpen;
    @JsonProperty("joinCount")
    private long joinCount;
    @JsonProperty("keywords")
    private String keywords;
    @JsonProperty("learnedCount")
    private long learnedCount;
    @JsonProperty("lecturerInfo")
    private String lecturerInfo;
    @JsonProperty("mainTeacher")
    private String mainTeacher;
    @JsonProperty("missionDesc")
    private String missionDesc;
    @JsonProperty("missionStandard")
    private MissionStandard missionStandard;
    @JsonProperty("openMode")
    private long openMode;
    @JsonProperty("price")
    private long price;
    @JsonProperty("quoteCourseId")
    private long quoteCourseId;
    @JsonProperty("recommendTag")
    private String recommendTag;
    @JsonProperty("router")
    private VideoRouter router;
    @JsonProperty("rules")
    private String rules;
    @JsonProperty("schoolId")
    private long schoolId;
    @JsonProperty("semesterEndTime")
    private String semesterEndTime;
    @JsonProperty("semesters")
    private Object semesters;
    @JsonProperty("semesterStartTime")
    private String semesterStartTime;
    @JsonProperty("sort")
    private long sort;
    @JsonProperty("source")
    private String source;
    @JsonProperty("status")
    private long status;
    @JsonProperty("subjectId")
    private long subjectId;
    @JsonProperty("summary")
    private String summary;
    @JsonProperty("title")
    private String title;
    @JsonProperty("type")
    private long type;
    @JsonProperty("updatedAt")
    private String updatedAt;
}
