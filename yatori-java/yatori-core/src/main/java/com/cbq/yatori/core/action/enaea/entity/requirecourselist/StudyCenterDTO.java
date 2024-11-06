package com.cbq.yatori.core.action.enaea.entity.requirecourselist;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class StudyCenterDTO {
    @JsonProperty("agented")
    private boolean agented;
    @JsonProperty("authorityType")
    private Object authorityType;
    @JsonProperty("canView")
    private Object canView;
    @JsonProperty("contentLength")
    private String contentLength;
    @JsonProperty("courseAttachmentCount")
    private Object courseAttachmentCount;
    @JsonProperty("courseContentCount")
    private Object courseContentCount;
    @JsonProperty("courseContentLink")
    private Object courseContentLink;
    @JsonProperty("coursecontentType")
    private String coursecontentType;
    @JsonProperty("courseExternalType")
    private Object courseExternalType;
    @JsonProperty("courseId")
    private Long courseId;
    @JsonProperty("coursePrice")
    private long coursePrice;
    @JsonProperty("courseSnapshotInfoId")
    private Object courseSnapshotInfoId;
    @JsonProperty("courseTitle")
    private String courseTitle;
    @JsonProperty("courseType")
    private Object courseType;
    @JsonProperty("dateAuthority")
    private String dateAuthority;
    @JsonProperty("dateLastStudy")
    private String dateLastStudy;
    @JsonProperty("hasAuthorization")
    private Object hasAuthorization;
    @JsonProperty("hasFlv")
    private boolean hasFlv;
    @JsonProperty("id")
    private long id;
    @JsonProperty("isCompleted")
    private Object isCompleted;
    @JsonProperty("isNew")
    private boolean isNew;
    @JsonProperty("isPaid")
    private Object isPaid;
    @JsonProperty("redCourseTitle")
    private String redCourseTitle;
    @JsonProperty("sellerName")
    private String sellerName;
    @JsonProperty("studyProgress")
    private String studyProgress;
    @JsonProperty("tradeId")
    private Object tradeId;
    @JsonProperty("watchCourseContentCount")
    private Object watchCourseContentCount;
}
