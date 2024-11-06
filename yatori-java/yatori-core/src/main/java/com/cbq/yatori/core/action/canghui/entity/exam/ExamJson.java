package com.cbq.yatori.core.action.canghui.entity.exam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamJson {
    ArrayList<ExamCourse> examCourses;

}
