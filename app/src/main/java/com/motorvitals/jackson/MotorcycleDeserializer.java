package com.motorvitals.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.motorvitals.classes.ElementList;
import com.motorvitals.classes.Motorcycle;

import java.io.IOException;
import java.util.ArrayList;

public class MotorcycleDeserializer extends StdDeserializer<Motorcycle> {
    public MotorcycleDeserializer() {
        this(null);
    }
    protected MotorcycleDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Motorcycle deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String name = node.get("name").asText();
        String description = node.get("description").asText();
        int km = node.get("km").asInt();
        JsonNode elementListsNode = node.get("elementLists");
        ArrayList<ElementList> elementLists = new ArrayList<>();
        for (JsonNode elementListNode : elementListsNode) {
            ElementList elementList = deserializeElementList(elementListNode, jsonParser);
            elementLists.add(elementList);
        }
        return new Motorcycle(elementLists, name, description, km);
    }

    private ElementList deserializeElementList(JsonNode elementListNode, JsonParser jsonParser) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        return mapper.treeToValue(elementListNode, ElementList.class);
    }
}
