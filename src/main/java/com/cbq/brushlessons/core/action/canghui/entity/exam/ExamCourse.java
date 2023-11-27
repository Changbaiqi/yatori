package com.cbq.brushlessons.core.action.canghui.entity.exam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamCourse {
    Map<String,ExamTopic> examTopics;
}
