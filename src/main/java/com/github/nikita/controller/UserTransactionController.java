package com.github.nikita.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.nikita.ObjectMapperFactory;
import com.github.nikita.SecurityService;
import com.github.nikita.configuration.DatabaseConfiguration;
import com.github.nikita.model.Reviews;
import com.github.nikita.model.Role;
import com.github.nikita.model.Status;
import com.github.nikita.model.UserTransaction;
import io.javalin.http.Context;

import java.io.IOException;
import java.sql.SQLException;

public class UserTransactionController {
    public static void createUserTran(Context ctx) throws IOException, SQLException {
        String json = ctx.body();
        UserTransaction userTran;
        ObjectMapper obMap = ObjectMapperFactory.createObjectMapper(UserTransaction.class);
        userTran = obMap.readValue(json, UserTransaction.class);
        userTran.setBuyer(SecurityService.searchUser(ctx));
        if (userTran.getStatus()==Status.CONFIRM) {
            boolean seach = false;
            for (UserTransaction usTr : DatabaseConfiguration.usTranDao.queryForAll()) {
                if (usTr.getStatus() == Status.BUY && usTr.getSeller() == userTran.getSeller() && usTr.getBuyer() == userTran.getBuyer()) {
                    if (!seach) {
                        userTran.setBeginTime(usTr.getBeginTime());
                        userTran.setId(usTr.getId());
                        DatabaseConfiguration.userDao.queryForId(userTran.getSeller().getId()).setBalance(DatabaseConfiguration.userDao.queryForId(userTran.getSeller().getId()).getBalance()+userTran.getMoney());
                        DatabaseConfiguration.usTranDao.delete(usTr);
                        seach = true;
                    }
                }
            }

        } else {
            DatabaseConfiguration.userDao.queryForId(userTran.getBuyer().getId()).setBalance(DatabaseConfiguration.userDao.queryForId(userTran.getBuyer().getId()).getBalance()-userTran.getMoney());

        }
        DatabaseConfiguration.usTranDao.create(userTran);
        ctx.status(201);
    }

    public static void getAllUserTran(Context ctx) throws SQLException, JsonProcessingException {
        if (SecurityService.authentication(ctx)) {
            if (SecurityService.authorization(ctx) == Role.ADMIN) {
                ObjectMapper obMap = ObjectMapperFactory.createObjectMapper(UserTransaction.class);
                ctx.result(obMap.writeValueAsString(DatabaseConfiguration.usTranDao.queryForAll()));
                ctx.status(201);
            } else if (SecurityService.authorization(ctx) == Role.USER) {
                ObjectMapper obMap = ObjectMapperFactory.createObjectMapper(UserTransaction.class);
                ctx.result(obMap.writeValueAsString(SecurityService.searchUserTran(ctx)));
                ctx.status(201);
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
                ctx.status(201);
            } else if (SecurityService.authorization(ctx) == Role.USER) {
                if (DatabaseConfiguration.usTranDao.queryForId(id).getBuyer() == SecurityService.searchUser(ctx)) {
                    ObjectMapper obMap = ObjectMapperFactory.createObjectMapper(UserTransaction.class);
                    ctx.result(obMap.writeValueAsString(DatabaseConfiguration.usTranDao.queryForId(id)));
                    ctx.status(201);
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
                    if (SecurityService.searchUser(ctx) == DatabaseConfiguration.usTranDao.queryForId(id).getBuyer()) {
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
