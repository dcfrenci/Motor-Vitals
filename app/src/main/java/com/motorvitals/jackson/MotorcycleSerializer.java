package com.motorvitals.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.motorvitals.classes.ElementList;
import com.motorvitals.classes.Motorcycle;

import java.io.IOException;

public class MotorcycleSerializer extends StdSerializer<Motorcycle> {
    public MotorcycleSerializer() {
        this(null);
    }
    protected MotorcycleSerializer(Class<Motorcycle> t) {
        super(t);
    }

    @Override
    public void serialize(Motorcycle motorcycle, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("name", motorcycle.getName());
        jsonGenerator.writeStringField("description", motorcycle.getDescription());
        jsonGenerator.writeNumberField("km", motorcycle.getKm());
        jsonGenerator.writeStringField("photo", motorcycle.getPhoto().toString());
        jsonGenerator.writeArrayFieldStart("elementLists");
        for (ElementList elementList : motorcycle.getElementList()) {
            jsonGenerator.writeObject(elementList);
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
    }
}
