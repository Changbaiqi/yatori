package com.cbq.yatori.core.action.yinghua.entity.allcourse;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class CourseFinishInform {
    @JsonProperty("addDate")
    private String addDate;
    @JsonProperty("addTime")
    private String addTime;
    @JsonProperty("cateBid")
    private String cateBid;
    @JsonProperty("categoryId")
    private long categoryId;
    @JsonProperty("categoryName")
    private String categoryName;
    @JsonProperty("cateMid")
    private String cateMid;
    @JsonProperty("classTeacher")
    private String classTeacher;
    @JsonProperty("clusterId")
    private String clusterId;
    @JsonProperty("code")
    private String code;
    @JsonProperty("collegeId")
    private long collegeId;
    @JsonProperty("collegeName")
    private String collegeName;
    @JsonProperty("cover")
    private String cover;
    @JsonProperty("createId")
    private String createId;
    @JsonProperty("credit")
    private String credit;
    @JsonProperty("endDate")
    private String endDate;
    @JsonProperty("id")
    private long id;
    @JsonProperty("intro")
    private String intro;
    @JsonProperty("lastNodeId")
    private long lastNodeId;
    @JsonProperty("learned")
    private long learned;
    @JsonProperty("learning")
    private long learning;
    @JsonProperty("lecturerName")
    private String lecturerName;
    @JsonProperty("lecturers")
    private String lecturers;
    @JsonProperty("lineLock")
    private String lineLock;
    @JsonProperty("mission")
    private String mission;
    @JsonProperty("mode")
    private long mode;
    @JsonProperty("name")
    private String name;
    @JsonProperty("offline")
    private String offline;
    @JsonProperty("periodName")
    private String periodName;
    @JsonProperty("proclamation")
    private Object proclamation;
    @JsonProperty("progress")
    private CourseProgress progress;
    @JsonProperty("progress1")
    private String progress1;
    @JsonProperty("resultRank")
    private long resultRank;
    @JsonProperty("resultScore")
    private CourseProgress resultScore;
    @JsonProperty("schoolId")
    private String schoolId;
    @JsonProperty("scoreRuleUrl")
    private String scoreRuleUrl;
    @JsonProperty("sign")
    private long sign;
    @JsonProperty("signClass")
    private String signClass;
    @JsonProperty("signedIn")
    private long signedIn;
    @JsonProperty("signEndTime")
    private String signEndTime;
    @JsonProperty("signInId")
    private long signInId;
    @JsonProperty("signLimit")
    private long signLimit;
    @JsonProperty("signScope")
    private String signScope;
    @JsonProperty("signStartTime")
    private String signStartTime;
    @JsonProperty("signState")
    private long signState;
    @JsonProperty("startDate")
    private String startDate;
    @JsonProperty("state")
    private long state;
    @JsonProperty("stuCount")
    private String stuCount;
    @JsonProperty("studentCount")
    private long studentCount;
    @JsonProperty("tabExam")
    private boolean tabExam;
    @JsonProperty("tabFile")
    private boolean tabFile;
    @JsonProperty("tabVideo")
    private boolean tabVideo;
    @JsonProperty("tabVote")
    private boolean tabVote;
    @JsonProperty("tabWork")
    private boolean tabWork;
    @JsonProperty("teachers")
    private String teachers;
    @JsonProperty("tplId")
    private String tplId;
    @JsonProperty("videoCount")
    private long videoCount;
    @JsonProperty("videoLearned")
    private long videoLearned;
}
