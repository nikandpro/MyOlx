package com.github.nikita.deserialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.nikita.configuration.DatabaseConfiguration;
import com.github.nikita.model.Reviews;

import java.io.IOException;
import java.sql.SQLException;

public class ReviewsDeserialize extends StdDeserializer<Reviews> {
    
    public ReviewsDeserialize() {
        super(Reviews.class);
    }
    
    @Override
    public Reviews deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = parser.getCodec().readTree(parser);
        Reviews reviews = new Reviews();
        reviews.setId(0);
        try {
            reviews.setProduct(DatabaseConfiguration.prodDao.queryForId(node.get("product").asInt()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        reviews.setText(node.get("text").asText());
        return reviews;
    }
}
