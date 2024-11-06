package com.cbq.yatori.core.action.yinghua.entity.examtopic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExamTopic {
    private String index; //题目号码
    private String answerId; //答题ID
    private String type; //题目类型
    private String source; //题目分数
    private String content; //题目内容
    private List<TopicSelect> selects;

}
