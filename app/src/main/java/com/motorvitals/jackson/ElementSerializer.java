package com.motorvitals.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.motorvitals.classes.Element;

import java.io.IOException;

public class ElementSerializer extends StdSerializer<Element> {
    public ElementSerializer() {
        this(null);
    }
    protected ElementSerializer(Class<Element> t) {
        super(t);
    }

    @Override
    public void serialize(Element element, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("name", element.getName());
        jsonGenerator.writeStringField("description", element.getDescription());
        jsonGenerator.writeStringField("photo", element.getPhoto());
        jsonGenerator.writeNumberField("price", element.getPrice());
        jsonGenerator.writeBooleanField("state", element.getState());
        //Day interval
        jsonGenerator.writeNumberField("minDay", element.getDayInterval().get("min"));
        jsonGenerator.writeNumberField("medDay", element.getDayInterval().get("med"));
        jsonGenerator.writeNumberField("maxDay", element.getDayInterval().get("max"));
        jsonGenerator.writeStringField("lastDayInterval", element.getLastServiceDate().toString());
        //Km interval
        jsonGenerator.writeNumberField("minKm", element.getKmInterval().get("min"));
        jsonGenerator.writeNumberField("medKm", element.getKmInterval().get("med"));
        jsonGenerator.writeNumberField("maxKm", element.getKmInterval().get("max"));
        jsonGenerator.writeNumberField("lastServiceKm", element.getLastServiceKm());
        jsonGenerator.writeNumberField("currentKm", element.getCurrentKm());
        jsonGenerator.writeEndObject();
    }
}
