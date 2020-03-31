package com.github.nikita.deserialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.nikita.configuration.DatabaseConfiguration;
import com.github.nikita.model.CategoriesProduct;
import java.io.IOException;
import java.sql.SQLException;

public class CategoriesProductDeserialize extends StdDeserializer<CategoriesProduct> {

    public CategoriesProductDeserialize() {
        super(CategoriesProductDeserialize.class);
    }

    @Override
    public CategoriesProduct deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        CategoriesProduct categoriesProduct = new CategoriesProduct();
        JsonNode node = parser.getCodec().readTree(parser);
        categoriesProduct.setId(0);
        try {
            categoriesProduct.setCategoeis(DatabaseConfiguration.categDao.queryForId(node.get("categoriesId").asInt()));
            categoriesProduct.setProduct(DatabaseConfiguration.prodDao.queryForId(node.get("productId").asInt()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categoriesProduct;
    }
}
