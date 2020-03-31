package com.github.nikita.deserialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.nikita.SecurityService;
import com.github.nikita.configuration.DatabaseConfiguration;
import com.github.nikita.model.Product;
import com.github.nikita.model.User;
import io.javalin.http.Context;

import java.io.IOException;
import java.sql.SQLException;

public class ProductDeserialize extends StdDeserializer<Product> {

    public ProductDeserialize() {
        super(Product.class);
    }

    @Override
    public Product deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        Product product = new Product();
        JsonNode node = parser.getCodec().readTree(parser);
        product.setId(0);
        product.setNameProd(node.get("nameProd").asText());
        product.setAdress(node.get("adress").asText());
        product.setSeller(null);
        product.setPrice(node.get("price").asInt());
        return product;
    }
}
