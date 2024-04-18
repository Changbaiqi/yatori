package com.cbq.yatori.core.action.yinghua.entity.examtopic;

import lombok.Data;

import java.util.List;

@Data
public class ExamTopic {
    private String index; //题目号码
    private String type; //题目类型
    private String source; //题目分数
    private String content; //题目内容
    private List<TopicSelect> selects;
}
