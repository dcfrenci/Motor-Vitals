package com.motorvitals.jackson;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.motorvitals.classes.Element;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;

public class ElementDeserializer extends StdDeserializer<Element> {
    public ElementDeserializer() {
        this(null);
    }
    protected ElementDeserializer(Class<Element> t) {
        super(t);
    }

    @Override
    public Element deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        Element element = new Element();
        element.setName(node.get("name").asText());
        element.setDescription(node.get("description").asText());
        element.setPhoto(node.get("photo").asText());
        element.setPrice(node.get("price").asDouble());
        element.setState(node.get("state").asBoolean());
        HashMap<String, Integer> map = new HashMap<>();
        //Day interval
        map.put("min", node.get("minDay").asInt());
        map.put("med", node.get("medDay").asInt());
        map.put("max", node.get("maxDay").asInt());
        element.setDayInterval(map);
        element.setLastServiceDate(LocalDate.parse(node.get("lastDayInterval").asText()));
        //Km interval
        map.put("min", node.get("minKm").asInt());
        map.put("med", node.get("medKm").asInt());
        map.put("max", node.get("maxKm").asInt());
        element.setKmInterval(map);
        element.setLastServiceKm(node.get("lastServiceKm").asInt());
        element.setCurrentKm(node.get("currentKm").asInt());
        return element;
    }
}
