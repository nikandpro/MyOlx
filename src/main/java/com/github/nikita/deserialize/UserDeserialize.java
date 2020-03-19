package com.github.nikita.deserialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.nikita.SecurityService;
import com.github.nikita.model.Role;
import com.github.nikita.model.User;

import java.io.IOException;

public class UserDeserialize extends StdDeserializer<User> {

    public UserDeserialize() {
        super(User.class);
    }

    public User deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        User user = new User();
        JsonNode node = parser.getCodec().readTree(parser);
        user.setId(0);
        user.setLogin(node.get("login").asText());
        user.setPassword(SecurityService.encryption(node.get("password").asText()));
        user.setFname(node.get("fname").asText());
        user.setLname(node.get("lname").asText());
        user.setBalance(node.get("balance").asInt());
        user.setPhone(node.get("phone").asText());
        user.setEmail(node.get("email").asText());
        user.setRole(Role.valueOf("ADMIN"));
        return user;
    }
}
