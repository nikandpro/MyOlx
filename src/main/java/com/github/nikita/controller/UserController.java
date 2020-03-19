package com.github.nikita.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.nikita.ObjectMapperFactory;
import com.github.nikita.SecurityService;
import com.github.nikita.configuration.DatabaseConfiguration;
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
        } else
            ctx.status(401);
    }

    public static void getOneUser(Context ctx) throws SQLException, JsonProcessingException {
        if (SecurityService.authentication(ctx)) {
            ObjectMapper obMap = ObjectMapperFactory.createObjectMapper(User.class);
            int id = Integer.parseInt(ctx.pathParam("id"));
            for (User user : DatabaseConfiguration.userDao.queryForAll()) {
                if (user.getId() == id) {
                    ctx.result(obMap.writeValueAsString(user));
                }
            }
        } else
            ctx.status(401);
    }

    public static void updateUser(Context ctx) throws SQLException, IOException {
        String json = ctx.body();
        User user;
        ObjectMapper obMap = ObjectMapperFactory.createObjectMapper(User.class);
        if (SecurityService.authentication(ctx)) {
            if (SecurityService.authorization(ctx) == Role.ADMIN) {
                user = obMap.readValue(json, User.class);
                DatabaseConfiguration.userDao.update(user);
            } else if (SecurityService.authorization(ctx) == Role.USER) {
                user = obMap.readValue(json, User.class);
                if (user.getId() == SecurityService.searchUser(ctx).getId()) {
                    DatabaseConfiguration.userDao.update(user);
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
            } else if (SecurityService.authorization(ctx) == Role.USER) {
                if (SecurityService.searchUser(ctx).getId() == id) {
                    DatabaseConfiguration.userDao.deleteById(id);
                } else {
                    ctx.status(403);
                }
            }
        } else {
            ctx.status(401);
        }
    }
}
    /*
    {
	"login":"mama",
	"password":"123rea",
	"fname":"Maria",
	"lname":"Rea",
	"balance":1500,
	"phone":"2456322",
	"email":"rea@mail.ru"
	} */

