package com.cbq.brushlessons.core.action.yinghua.entity.videomessage;

import com.cbq.brushlessons.core.action.yinghua.entity.allcourse.CourseProgress;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.IOException;
import java.util.Map;

/**
 * 这个是用于统计学习相关的，比如学习时长
 */
@lombok.Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = VideoInformStudyTotal.Deserializer.class)
//@JsonSerialize(using = VideoInformStudyTotal.Serializer.class)
public class VideoInformStudyTotal {
    @lombok.Getter(onMethod_ = {@JsonProperty("duration")})
    @lombok.Setter(onMethod_ = {@JsonProperty("duration")})
    /**
     * 学习时长
     */
    private String duration;
    @lombok.Getter(onMethod_ = {@JsonProperty("progress")})
    @lombok.Setter(onMethod_ = {@JsonProperty("progress")})
    /**
     * 视屏学习进度百分百比
     */
    private String progress;
    @lombok.Getter(onMethod_ = {@JsonProperty("state")})
    @lombok.Setter(onMethod_ = {@JsonProperty("state")})
    /**
     * 视屏学习状态值
     */
    private String state;

    static class Deserializer extends JsonDeserializer<VideoInformStudyTotal> {
        @Override
        public VideoInformStudyTotal deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
//            Map map = jsonParser.getCodec().readValue(jsonParser, Map.class);
            JsonToken currentToken = jsonParser.getCurrentToken();
            switch (currentToken) {
                case START_OBJECT -> {
                    Map<String,Object> map = jsonParser.readValueAs(Map.class);
                    VideoInformStudyTotal videoInformStudyTotal = new VideoInformStudyTotal();
                    videoInformStudyTotal.duration = (String) map.get("duration");
                    videoInformStudyTotal.progress = (String) map.get("progress");
                    videoInformStudyTotal.state = (String) map.get("state");
                    return videoInformStudyTotal;
                }
            }
            return null;
        }
    }

    static class Serializer extends JsonSerializer<VideoInformStudyTotal> {
        @Override
        public void serialize(VideoInformStudyTotal obj, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            if (obj.duration != null) {
                jsonGenerator.writeObject(obj.duration);
                return;
            }
            if (obj.progress != null) {
                jsonGenerator.writeObject(obj.progress);
                return;
            }
            throw new IOException("Progress must not be null");
        }
    }

}
