package com.motorvitals.jackson;

import android.graphics.Color;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.motorvitals.classes.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UserDeserializer extends JsonDeserializer<User> {
    @Override
    public User deserialize(JsonParser p, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        String name = node.get("name").asText();
        Map<String, Integer> stateColor = new HashMap<>();
        JsonNode stateColorNode = node.get("stateColor");
        Iterator<Map.Entry<String, JsonNode>> fields = stateColorNode.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            stateColor.put(field.getKey(), Color.parseColor(field.getValue().asText()));
        }
        return new User(stateColor, name);
    }
}
