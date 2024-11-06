package com.cbq.yatori.core.action.enaea.entity.underwayproject;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class ResultList {
    @JsonProperty("circleAlias")
    private Object circleAlias;
    @JsonProperty("circleCardNumber")
    private String circleCardNumber;
    @JsonProperty("circleId")
    private Long circleId;
    @JsonProperty("circleName")
    private String circleName;
    @JsonProperty("circleNameShort")
    private String circleNameShort;
    @JsonProperty("clusterId")
    private Long clusterId;
    @JsonProperty("clusterName")
    private String clusterName;
    @JsonProperty("clusterNameShort")
    private String clusterNameShort;
    @JsonProperty("hasSpecialAssessment")
    private Object hasSpecialAssessment;
    @JsonProperty("isCircleLeader")
    private Boolean isCircleLeader;
    @JsonProperty("myCircleStatisticDTOList")
    private MyCircleStatisticDTOList myCircleStatisticDTOList;
    @JsonProperty("planEnable")
    private Boolean planEnable;
    @JsonProperty("planId")
    private Object planId;
    @JsonProperty("planState")
    private Long planState;
    @JsonProperty("startEndTime")
    private String startEndTime;
    @JsonProperty("studentList")
    private StudentList studentList;
    @JsonProperty("teacherList")
    private TeacherList teacherList;
}
