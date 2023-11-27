package com.cbq.brushlessons.core.entity;

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
    @ExcelProperty("title")
    private String title;
    @ExcelProperty("type")
    private String type;
    @ExcelProperty("answer")
    private String answer;
}
