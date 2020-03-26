package com.github.nikita.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.nikita.ObjectMapperFactory;
import com.github.nikita.model.Product;
import com.github.nikita.model.User;

import java.io.IOException;

public class ProductSerialize extends StdSerializer<Product> {

    public ProductSerialize() {
        super(Product.class);
    }

    @Override
    public void serialize(Product product, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", product.getId());
        //jsonGenerator.writeStringField("nameProd", product.getNameProd());
        //jsonGenerator.writeStringField("adress", product.getAdress());
        //jsonGenerator.writeObjectField("seller", product.getSeller());
        jsonGenerator.writeEndObject();
    }
}
