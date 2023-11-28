package com.cbq.brushlessons.core.action.canghui.entity.exam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamCourse {
    private Integer id; //考试试卷id
    private String title; //考试题目
    private LinkedHashMap<String,ExamTopic> examTopics; //考试试题
}
