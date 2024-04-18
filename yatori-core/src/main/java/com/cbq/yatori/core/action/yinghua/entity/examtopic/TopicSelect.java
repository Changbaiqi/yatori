package com.cbq.yatori.core.action.yinghua.entity.examtopic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TopicSelect {
    String value; //选项提交值
    String num; //选项文本值
    String txt; //选项具体内容文本
}
