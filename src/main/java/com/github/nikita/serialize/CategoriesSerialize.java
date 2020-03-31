package com.github.nikita.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.nikita.model.Categories;

import java.io.IOException;

public class CategoriesSerialize extends StdSerializer<Categories> {

    public CategoriesSerialize() {
        super(Categories.class);
    }

    @Override
    public void serialize(Categories categories, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", categories.getId());
        jsonGenerator.writeStringField("categoriesName", categories.getCategoriesName());
        jsonGenerator.writeEndObject();
    }
}
