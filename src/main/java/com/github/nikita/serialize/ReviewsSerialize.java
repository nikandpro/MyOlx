package com.github.nikita.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.nikita.model.Reviews;

import java.io.IOException;

public class ReviewsSerialize extends StdSerializer<Reviews> {
    
    public ReviewsSerialize() {
        super(Reviews.class);
    }
    
    @Override
    public void serialize(Reviews reviews, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", reviews.getId());
        jsonGenerator.writeObjectField("user", reviews.getUser());
        jsonGenerator.writeObjectField("product", reviews.getProduct());
        jsonGenerator.writeStringField("text", reviews.getText());
        jsonGenerator.writeEndObject();
        
        
        
    }
}
