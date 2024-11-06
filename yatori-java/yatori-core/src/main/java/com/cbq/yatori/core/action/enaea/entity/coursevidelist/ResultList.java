package com.cbq.yatori.core.action.enaea.entity.coursevidelist;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class ResultList {
    @JsonProperty("bitRate")
    private String bitRate;
    @JsonProperty("canEditeTestDuringCourse")
    private Object canEditeTestDuringCourse;
    @JsonProperty("canSkipTest")
    private boolean canSkipTest;
    @JsonProperty("ccsiteId")
    private Object ccsiteId;
    @JsonProperty("ccvideoId")
    private Object ccvideoId;
    @JsonProperty("contentType")
    private Object contentType;
    @JsonProperty("convertStatus")
    private String convertStatus;
    @JsonProperty("convertType")
    private long convertType;
    @JsonProperty("courseAuthorityFlag")
    private long courseAuthorityFlag;
    @JsonProperty("courseId")
    private Object courseId;
    @JsonProperty("deadLineEnd")
    private Object deadLineEnd;
    @JsonProperty("deadLineStart")
    private Object deadLineStart;
    @JsonProperty("documentId")
    private long documentId;
    @JsonProperty("expired")
    private boolean expired;
    @JsonProperty("filename")
    private String filename; //视屏名称
    @JsonProperty("filepath")
    private Object filepath;
    @JsonProperty("filesize")
    private long filesize;
    @JsonProperty("hasTestAfterCourse")
    private boolean hasTestAfterCourse;
    @JsonProperty("id")
    private Long id;
    @JsonProperty("isCanModify")
    private boolean isCanModify;
    @JsonProperty("isDeadLine")
    private Object isDeadLine;
    @JsonProperty("isFree")
    private boolean isFree;
    @JsonProperty("isGuest")
    private boolean isGuest;
    @JsonProperty("isHaveOffline")
    private boolean isHaveOffline;
    @JsonProperty("isNeedBuy")
    private boolean isNeedBuy;
    @JsonProperty("isOrgAdmin")
    private boolean isOrgAdmin;
    @JsonProperty("isOrgAdminCandRead")
    private boolean isOrgAdminCandRead;
    @JsonProperty("isOwner")
    private boolean isOwner;
    @JsonProperty("isStart")
    private Object isStart;
    @JsonProperty("isTrial")
    private Object isTrial;
    @JsonProperty("lastPlayPage")
    private Object lastPlayPage;
    @JsonProperty("lastPlayTime")
    private Object lastPlayTime;
    @JsonProperty("length")
    private String length;
    @JsonProperty("paperId")
    private Object paperId;
    @JsonProperty("paperName")
    private Object paperName;
    @JsonProperty("paperRedirectUrl")
    private Object paperRedirectUrl;
    @JsonProperty("photoPath")
    private String photoPath;
    @JsonProperty("rank")
    private long rank;
    @JsonProperty("redirectUrl")
    private Object redirectUrl;
    @JsonProperty("sign")
    private Object sign;
    @JsonProperty("snapshotInfoId")
    private Object snapshotInfoId;
    @JsonProperty("studyLength")
    private Object studyLength;
    @JsonProperty("studyProgress")
    private String studyProgress;
    @JsonProperty("testDuringCourseMessage")
    private Object testDuringCourseMessage;
    @JsonProperty("timesLeft")
    private long timesLeft;
    @JsonProperty("timesLimit")
    private long timesLimit;
    @JsonProperty("videoList")
    private VideoList videoList;
    @JsonProperty("videoType")
    private long videoType;
    @JsonProperty("volumeId")
    private Object volumeId;
}
