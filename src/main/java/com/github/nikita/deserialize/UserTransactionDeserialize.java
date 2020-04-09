package com.github.nikita.deserialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.nikita.configuration.DatabaseConfiguration;
import com.github.nikita.model.Product;
import com.github.nikita.model.Status;
import com.github.nikita.model.UserTransaction;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;


public class UserTransactionDeserialize extends StdDeserializer<UserTransaction> {

    public UserTransactionDeserialize() {
        super(UserTransaction.class);
    }

    @Override
    public UserTransaction deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        UserTransaction userTran = new UserTransaction();
        JsonNode node = parser.getCodec().readTree(parser);
        if(Status.valueOf(node.get("status").asText())==Status.BUY) {
            userTran.setId(0);
            try {
                Product product = DatabaseConfiguration.prodDao.queryForId(node.get("product").asInt());
                userTran.setProduct(product);
                userTran.setSeller(product.getSeller());
                userTran.setMoney(product.getPrice());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            userTran.setStatus(Status.BUY);
            userTran.setBeginTime(LocalDateTime.now().toString());
            userTran.setEndTime(null);
        } else {
            userTran.setId(0);
            try {
                Product product = DatabaseConfiguration.prodDao.queryForId(node.get("product").asInt());
                userTran.setProduct(product);
                userTran.setSeller(product.getSeller());
                userTran.setMoney(product.getPrice());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            userTran.setStatus(Status.CONFIRM);
            userTran.setBeginTime(null);
            userTran.setEndTime(LocalDateTime.now().toString());
        }

        return userTran;
    }
}
