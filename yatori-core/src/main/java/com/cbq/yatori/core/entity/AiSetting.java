package com.cbq.yatori.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiSetting {
    private AiType aiType;
    private String API_KEY;
    private String API_SECRET;
}
