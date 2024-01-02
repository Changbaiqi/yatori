package com.cbq.yatori.core.action.yinghua.entity.allcourse;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class Banner {
    @JsonProperty("id")
    private String id;
    @JsonProperty("image")
    private String image;
    @JsonProperty("name")
    private String name;
}
