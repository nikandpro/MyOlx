package com.github.nikita.deserialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.nikita.model.Categories;

import java.io.IOException;

public class CategoriesDeserialize extends StdDeserializer<Categories> {

    public CategoriesDeserialize() {
        super(Categories.class);
    }

    @Override
    public Categories deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        Categories categories = new Categories();
        JsonNode node = parser.getCodec().readTree(parser);
        categories.setId(0);
        categories.setCategoriesName(node.get("categoriesName").asText());
        return categories;
    }
}
