package com.cbq.yatori.core.action.yinghua.entity.videomessage;

import com.cbq.yatori.core.action.yinghua.entity.allcourse.CourseProgress;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

@lombok.Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VideoInformData {
    @JsonProperty("cheat")
    private VideoInformCheat cheat;
    @JsonProperty("study_total")
    /**
     * 这个是用于统计学习相关的，比如学习时长
     */
    private VideoInformStudyTotal studyTotal;
    @JsonProperty("videoDuration")
    /**
     * 视屏总时长
     */
    private long videoDuration;
    @JsonProperty("videoId")
    /**
     * 视屏id链接
     */
    private String videoId;
    @JsonProperty("videoMime")
    private String videoMime;
    @JsonProperty("videoToken")
    private String videoToken;


}
