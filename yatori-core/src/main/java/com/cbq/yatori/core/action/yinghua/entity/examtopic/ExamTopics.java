package com.cbq.yatori.core.action.yinghua.entity.examtopic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExamTopics {
    private LinkedHashMap<String,ExamTopic> examTopics;
}
