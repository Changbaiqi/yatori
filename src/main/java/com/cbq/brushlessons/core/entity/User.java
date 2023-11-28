package com.cbq.brushlessons.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @description: 用户类，每个操作单位将会以用户为基准进行操作
 * @author 长白崎
 * @date 2023/10/23 14:43
 * @version 1.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private AccountType accountType;
    private String url; //课程平台url
    private String account; //密码
    private String password; //账号
    private Integer model; //视屏刷课模式
    private Boolean autoExam; //是否自动考试
    private Set<String> excludeCourses; //排除课程
    private Set<String> includeCourses; //包含课程
    private AccountCache cache;
}
