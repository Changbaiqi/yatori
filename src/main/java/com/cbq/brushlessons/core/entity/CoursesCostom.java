package com.cbq.brushlessons.core.entity;

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
public class CoursesCostom {
    private Set<String> excludeCourses; //排除课程
    private Set<String> includeCourses; //包含课程
    private ArrayList<CoursesSetting> coursesSettings; //课程对应配置
}
