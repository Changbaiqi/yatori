package com.cbq.yatori.core.entity;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AiSetting {
    private AiType aiType;
    @JsonProperty("API_KEY")
    private String API_KEY;
    @JsonProperty("API_SECRET")
    private String API_SECRET;
}
