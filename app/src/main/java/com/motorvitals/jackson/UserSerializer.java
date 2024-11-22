package com.motorvitals.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.motorvitals.classes.User;

import java.io.IOException;
import java.util.Map;

public class UserSerializer extends JsonSerializer<User> {
    @Override
    public void serialize(User user, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("name", user.getName());
        jsonGenerator.writeObjectFieldStart("stateColor");
        for (Map.Entry<String, Integer> entry : user.getStateColor().entrySet()) {
            jsonGenerator.writeStringField(entry.getKey(), String.format("#%06X", (0xFFFFFF & entry.getValue())));
        }
        jsonGenerator.writeEndObject();
        jsonGenerator.writeEndObject();
    }
}
