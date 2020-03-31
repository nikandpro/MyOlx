package com.github.nikita.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.nikita.model.CategoriesProduct;

import java.io.IOException;

public class CategoriesProductSerialize extends StdSerializer<CategoriesProduct> {

    public CategoriesProductSerialize() {
        super(CategoriesProduct.class);
    }

    @Override
    public void serialize(CategoriesProduct categoriesProduct, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", categoriesProduct.getId());
        jsonGenerator.writeObjectField("categories", categoriesProduct.getCategoeis());
        jsonGenerator.writeObjectField("product", categoriesProduct.getProduct());
        jsonGenerator.writeEndObject();
    }
}
