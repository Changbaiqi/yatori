package com.cbq.yatori.core.action.yinghua.entity.allcourse;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.IOException;

@JsonDeserialize(using = CourseProgress.Deserializer.class)
@JsonSerialize(using = CourseProgress.Serializer.class)
public class CourseProgress {
    public Double doubleValue;
    public Long integerValue;

    static class Deserializer extends JsonDeserializer<CourseProgress> {
        @Override
        public CourseProgress deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            CourseProgress value = new CourseProgress();
            switch (jsonParser.currentToken()) {
                case VALUE_NUMBER_INT:
                    value.integerValue = jsonParser.readValueAs(Long.class);
                    break;
                case VALUE_NUMBER_FLOAT:
                    value.doubleValue = jsonParser.readValueAs(Double.class);
                    break;
                default:
                    throw new IOException("Cannot deserialize Progress");
            }
            return value;
        }
    }

    static class Serializer extends JsonSerializer<CourseProgress> {
        @Override
        public void serialize(CourseProgress obj, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            if (obj.doubleValue != null) {
                jsonGenerator.writeObject(obj.doubleValue);
                return;
            }
            if (obj.integerValue != null) {
                jsonGenerator.writeObject(obj.integerValue);
                return;
            }
            throw new IOException("Progress must not be null");
        }
    }
}
