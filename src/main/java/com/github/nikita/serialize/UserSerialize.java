package com.github.nikita.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.nikita.model.User;

import java.io.IOException;

public class UserSerialize extends StdSerializer<User> {

    public UserSerialize() {
        super(User.class);
    }

    public void serialize(User user, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", user.getId());
        jsonGenerator.writeStringField("login", user.getLogin());
        jsonGenerator.writeStringField("fname", user.getFname());
        jsonGenerator.writeStringField("lname", user.getLname());
        jsonGenerator.writeNumberField("balance", user.getBalance());
        jsonGenerator.writeStringField("phone", user.getPhone());
        jsonGenerator.writeStringField("email", user.getEmail());
        jsonGenerator.writeStringField("role", user.getRole().name());
        jsonGenerator.writeEndObject();
    }
}
