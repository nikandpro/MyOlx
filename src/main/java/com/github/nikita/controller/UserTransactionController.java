package com.github.nikita.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.nikita.ObjectMapperFactory;
import com.github.nikita.SecurityService;
import com.github.nikita.configuration.DatabaseConfiguration;
import com.github.nikita.model.*;
import io.javalin.http.Context;

import java.io.IOException;
import java.sql.SQLException;

public class UserTransactionController {
    public static void createUserTran(Context ctx) throws IOException, SQLException {
        String json = ctx.body();
        UserTransaction userTran;
        ObjectMapper obMap = ObjectMapperFactory.createObjectMapper(UserTransaction.class);
        userTran = obMap.readValue(json, UserTransaction.class);
        if (SecurityService.searchUser(ctx).getBalance()>userTran.getProduct().getPrice()) {
            userTran.setBuyer(SecurityService.searchUser(ctx));
            if (userTran.getStatus() == Status.CONFIRM) {
                boolean seach = false;
                for (UserTransaction usTr : DatabaseConfiguration.usTranDao.queryForAll()) {
                    if (usTr.getStatus() == Status.BUY && usTr.getSeller().getId() == userTran.getSeller().getId() && usTr.getBuyer().getId() == userTran.getBuyer().getId() && usTr.getProduct().getId() == userTran.getProduct().getId()) {
                        if (!seach) {
                            userTran.setBeginTime(usTr.getBeginTime());
                            userTran.setId(usTr.getId());

                            User user = DatabaseConfiguration.userDao.queryForId(userTran.getSeller().getId());
                            user.setBalance(user.getBalance() + userTran.getMoney());
                            DatabaseConfiguration.userDao.update(user);

                            seach = true;
                            DatabaseConfiguration.usTranDao.update(userTran);
                            ctx.status(201);
                        }
                    }
                }
            } else {
                User user = DatabaseConfiguration.userDao.queryForId(userTran.getBuyer().getId());
                user.setBalance(user.getBalance() - userTran.getMoney());
                DatabaseConfiguration.userDao.update(user);
                DatabaseConfiguration.usTranDao.create(userTran);
                ctx.status(201);
            }
        } else
            ctx.status(400);
    }

    public static void getAllUserTran(Context ctx) throws SQLException, JsonProcessingException {
        if (SecurityService.authentication(ctx)) {
            if (SecurityService.authorization(ctx) == Role.ADMIN) {
                ObjectMapper obMap = ObjectMapperFactory.createObjectMapper(UserTransaction.class);
                ctx.result(obMap.writeValueAsString(DatabaseConfiguration.usTranDao.queryForAll()));
                ctx.status(200);
            } else if (SecurityService.authorization(ctx) == Role.USER) {
                ObjectMapper obMap = ObjectMapperFactory.createObjectMapper(UserTransaction.class);
                ctx.result(obMap.writeValueAsString(SecurityService.searchUserTran(ctx)));
                ctx.status(200);
            }
        } else
            ctx.status(401);
    }

    public static void getOneUserTran(Context ctx) throws SQLException, JsonProcessingException {
        if (SecurityService.authentication(ctx)) {
            int id = Integer.parseInt(ctx.pathParam("id"));
            if (SecurityService.authorization(ctx) == Role.ADMIN) {
                ObjectMapper obMap = ObjectMapperFactory.createObjectMapper(UserTransaction.class);
                ctx.result(obMap.writeValueAsString(DatabaseConfiguration.usTranDao.queryForId(id)));
                ctx.status(200);
            } else if (SecurityService.authorization(ctx) == Role.USER) {
                if (DatabaseConfiguration.usTranDao.queryForId(id).getBuyer() == SecurityService.searchUser(ctx)) {
                    ObjectMapper obMap = ObjectMapperFactory.createObjectMapper(UserTransaction.class);
                    ctx.result(obMap.writeValueAsString(DatabaseConfiguration.usTranDao.queryForId(id)));
                    ctx.status(200);
                } else
                    ctx.status(403);

            }
        } else
            ctx.status(401);
    }

    public static void deleteUserTran(Context ctx) throws SQLException {
        int id = Integer.parseInt(ctx.pathParam("id"));
        if (SecurityService.authentication(ctx)) {
            if (SecurityService.authorization(ctx) == Role.ADMIN) {
                if (DatabaseConfiguration.usTranDao.queryForId(id) != null) {
                    DatabaseConfiguration.usTranDao.deleteById(id);
                    ctx.status(204);
                } else {
                    ctx.status(404);
                }
            } else if (SecurityService.authorization(ctx) == Role.USER) {
                if (DatabaseConfiguration.usTranDao.queryForId(id) != null) {
                    if (SecurityService.searchUser(ctx).getId() == DatabaseConfiguration.usTranDao.queryForId(id).getBuyer().getId()) {
                        DatabaseConfiguration.usTranDao.deleteById(id);
                        ctx.status(204);
                    } else {
                        ctx.status(403);
                    }
                } else {
                    ctx.status(404);
                }
            }
        } else {
            ctx.status(401);
        }
    }
}
