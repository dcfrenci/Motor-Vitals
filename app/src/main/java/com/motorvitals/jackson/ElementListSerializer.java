package com.motorvitals.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.motorvitals.classes.Element;
import com.motorvitals.classes.ElementList;

import java.io.IOException;

public class ElementListSerializer extends StdSerializer<ElementList> {
    public ElementListSerializer() {
        this(null);
    }
    protected ElementListSerializer(Class<ElementList> t) {
        super(t);
    }

    @Override
    public void serialize(ElementList elementList, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("title", elementList.getTitle());
        jsonGenerator.writeArrayFieldStart("elements");
        for (Element element : elementList.getElements()) {
            jsonGenerator.writeObject(element);
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
    }
}
