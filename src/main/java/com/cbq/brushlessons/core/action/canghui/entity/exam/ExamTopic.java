package com.cbq.brushlessons.core.action.canghui.entity.exam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

/**
 * @description: TODO 考试答案
 * @author 长白崎
 * @date 2023/11/27 23:27
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamTopic {
    private Integer id; //题目id
    private String title;//题目问题
    private Integer type;//题目类型,1为选择题，3为判断题，5为简答题
    private  Integer difficulty; //不知道是啥玩意
    private ArrayList<ExamItem> item; //答案选项
}
