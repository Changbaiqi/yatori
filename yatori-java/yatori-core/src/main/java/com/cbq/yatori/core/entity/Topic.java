package com.cbq.yatori.core.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Topic {
    @ExcelProperty("md5")
    private String md5;

    @ExcelProperty("type")
    private String type; // 单选：ONECHOICE、多选：MULTIPLECHOICE、填空：COMPLETION、简答：SHORTANSWER
    @ExcelProperty("title")
    private String title;
    @ExcelProperty("answer")
    private String answer;

}
