package com.cbq.yatori.core.action.canghui.entity.exam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: TODO 用于装选项的
 * @author 长白崎
 * @date 2023/11/27 23:26
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamItem {
    private String key; //选项的名称
    private String value; //选项对应的值
    private Integer score; //不知道啥玩意
    private Boolean isCorrect; //是否是真确答案，如果是true则代表这个选项是答案

}
