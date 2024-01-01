package com.cbq.yatori.core.action.enaea.entity.underwayproject;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class ResultList {
    @lombok.Getter(onMethod_ = {@JsonProperty("circleAlias")})
    @lombok.Setter(onMethod_ = {@JsonProperty("circleAlias")})
    private Object circleAlias;
    @lombok.Getter(onMethod_ = {@JsonProperty("circleCardNumber")})
    @lombok.Setter(onMethod_ = {@JsonProperty("circleCardNumber")})
    private String circleCardNumber;
    @lombok.Getter(onMethod_ = {@JsonProperty("circleId")})
    @lombok.Setter(onMethod_ = {@JsonProperty("circleId")})
    private Long circleId;
    @lombok.Getter(onMethod_ = {@JsonProperty("circleName")})
    @lombok.Setter(onMethod_ = {@JsonProperty("circleName")})
    private String circleName;
    @lombok.Getter(onMethod_ = {@JsonProperty("circleNameShort")})
    @lombok.Setter(onMethod_ = {@JsonProperty("circleNameShort")})
    private String circleNameShort;
    @lombok.Getter(onMethod_ = {@JsonProperty("clusterId")})
    @lombok.Setter(onMethod_ = {@JsonProperty("clusterId")})
    private Long clusterId;
    @lombok.Getter(onMethod_ = {@JsonProperty("clusterName")})
    @lombok.Setter(onMethod_ = {@JsonProperty("clusterName")})
    private String clusterName;
    @lombok.Getter(onMethod_ = {@JsonProperty("clusterNameShort")})
    @lombok.Setter(onMethod_ = {@JsonProperty("clusterNameShort")})
    private String clusterNameShort;
    @lombok.Getter(onMethod_ = {@JsonProperty("hasSpecialAssessment")})
    @lombok.Setter(onMethod_ = {@JsonProperty("hasSpecialAssessment")})
    private Object hasSpecialAssessment;
    @lombok.Getter(onMethod_ = {@JsonProperty("isCircleLeader")})
    @lombok.Setter(onMethod_ = {@JsonProperty("isCircleLeader")})
    private Boolean isCircleLeader;
    @lombok.Getter(onMethod_ = {@JsonProperty("myCircleStatisticDTOList")})
    @lombok.Setter(onMethod_ = {@JsonProperty("myCircleStatisticDTOList")})
    private MyCircleStatisticDTOList myCircleStatisticDTOList;
    @lombok.Getter(onMethod_ = {@JsonProperty("planEnable")})
    @lombok.Setter(onMethod_ = {@JsonProperty("planEnable")})
    private Boolean planEnable;
    @lombok.Getter(onMethod_ = {@JsonProperty("planId")})
    @lombok.Setter(onMethod_ = {@JsonProperty("planId")})
    private Object planId;
    @lombok.Getter(onMethod_ = {@JsonProperty("planState")})
    @lombok.Setter(onMethod_ = {@JsonProperty("planState")})
    private Long planState;
    @lombok.Getter(onMethod_ = {@JsonProperty("startEndTime")})
    @lombok.Setter(onMethod_ = {@JsonProperty("startEndTime")})
    private String startEndTime;
    @lombok.Getter(onMethod_ = {@JsonProperty("studentList")})
    @lombok.Setter(onMethod_ = {@JsonProperty("studentList")})
    private StudentList studentList;
    @lombok.Getter(onMethod_ = {@JsonProperty("teacherList")})
    @lombok.Setter(onMethod_ = {@JsonProperty("teacherList")})
    private TeacherList teacherList;
}
