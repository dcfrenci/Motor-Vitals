package com.motorvitals.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.motorvitals.classes.Element;
import com.motorvitals.classes.ElementList;

import java.io.IOException;
import java.util.ArrayList;

public class ElementListDeserializer extends StdDeserializer<ElementList> {
    public ElementListDeserializer() {
        this(null);
    }
    protected ElementListDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public ElementList deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String title = node.get("title").asText();
        JsonNode elementsNode = node.get("elements");
        ArrayList<Element> elements = new ArrayList<>();
        for (JsonNode elementNode : elementsNode) {
            Element element = deserializeElement(elementNode, jsonParser);
            elements.add(element);
        }
        return new ElementList(title, elements);
    }

    private Element deserializeElement(JsonNode elementNode, JsonParser jsonParser) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        return mapper.treeToValue(elementNode, Element.class);
    }

}
