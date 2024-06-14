package com.cbq.yatori.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CoursesCustom {
    private Integer videoModel = 1; //视屏刷课模式，0为不刷视屏,1为普通模式，2为暴力模式，默认为1
    private Integer autoExam = 0;//自动考试模式，0为不考试，1为自动考试，默认为0
    private Set<String> excludeCourses; //排除课程
    private Set<String> includeCourses; //包含课程
    private ArrayList<CoursesSetting> coursesSettings; //课程对应配置
}
