package com.github.nikita.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.nikita.model.UserTransaction;

import java.io.IOException;

public class UserTransactionSerialize extends StdSerializer<UserTransaction> {

    public UserTransactionSerialize() {
        super(UserTransaction.class);
    }

    @Override
    public void serialize(UserTransaction userTransaction, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", userTransaction.getId());
        jsonGenerator.writeObjectField("product", userTransaction.getProduct());
        jsonGenerator.writeObjectField("buyer", userTransaction.getBuyer());
        jsonGenerator.writeObjectField("money", userTransaction.getMoney());
        jsonGenerator.writeStringField("status", userTransaction.getStatus().name());
        jsonGenerator.writeStringField("beginTime", userTransaction.getBeginTime());
        jsonGenerator.writeStringField("endTime", userTransaction.getEndTime());
        jsonGenerator.writeEndObject();
    }
}
