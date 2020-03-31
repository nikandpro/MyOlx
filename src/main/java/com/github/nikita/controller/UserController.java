package com.github.nikita.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.nikita.ObjectMapperFactory;
import com.github.nikita.SecurityService;
import com.github.nikita.configuration.DatabaseConfiguration;
import com.github.nikita.model.Product;
import com.github.nikita.model.Role;
import com.github.nikita.model.User;
import io.javalin.http.Context;

import java.io.IOException;
import java.sql.SQLException;

public class UserController {

    public static void createUser(Context ctx) throws IOException, SQLException {
        String json = ctx.body();
        User user;
        ObjectMapper obMap = ObjectMapperFactory.createObjectMapper(User.class);
        user = obMap.readValue(json, User.class);
        DatabaseConfiguration.userDao.create(user);
        ctx.status(201);
    }

    public static void getAllUser(Context ctx) throws SQLException, JsonProcessingException {
        if (SecurityService.authentication(ctx)) {
            ObjectMapper obMap = ObjectMapperFactory.createObjectMapper(User.class);
            ctx.result(obMap.writeValueAsString(DatabaseConfiguration.userDao.queryForAll()));
            ctx.status(201);
        } else
            ctx.status(401);
    }

    public static void getOneUser(Context ctx) throws SQLException, JsonProcessingException {
        if (SecurityService.authentication(ctx)) {
            ObjectMapper obMap = ObjectMapperFactory.createObjectMapper(User.class);
            int id = Integer.parseInt(ctx.pathParam("id"));
            User user = DatabaseConfiguration.userDao.queryForId(id);
            if (user != null) {
                ctx.result(obMap.writeValueAsString(user));
                ctx.status(201);
            } else
                ctx.status(404);
        } else
            ctx.status(401);
    }

    public static void updateUser(Context ctx) throws SQLException, IOException {
        String json = ctx.body();
        User user;
        ObjectMapper obMap = ObjectMapperFactory.createObjectMapper(User.class);
        if (SecurityService.authentication(ctx)) {
            int id = Integer.parseInt(ctx.pathParam("id"));
            if (SecurityService.authorization(ctx) == Role.ADMIN) {
                user = obMap.readValue(json, User.class);
                user.setId(id);
                DatabaseConfiguration.userDao.update(user);
                ctx.status(201);
            } else if (SecurityService.authorization(ctx) == Role.USER) {
                user = obMap.readValue(json, User.class);
                if (id == SecurityService.searchUser(ctx).getId()) {
                    user.setId(id);
                    DatabaseConfiguration.userDao.update(user);
                    ctx.status(201);
                } else {
                    ctx.status(403);
                }
            }
        } else {
            ctx.status(401);
        }
    }

    public static void deleteUser(Context ctx) throws SQLException {
        int id = Integer.parseInt(ctx.pathParam("id"));
        if (SecurityService.authentication(ctx)) {
            if (SecurityService.authorization(ctx) == Role.ADMIN) {
                DatabaseConfiguration.userDao.deleteById(id);
                ctx.status(204);
            } else if (SecurityService.authorization(ctx) == Role.USER) {
                if (SecurityService.searchUser(ctx).getId() == id) {
                    DatabaseConfiguration.userDao.deleteById(id);
                    ctx.status(204);
                } else {
                    ctx.status(403);
                }
            }
        } else {
            ctx.status(401);
        }
    }
}

