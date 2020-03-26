package com.github.nikita.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.nikita.ObjectMapperFactory;
import com.github.nikita.SecurityService;
import com.github.nikita.configuration.DatabaseConfiguration;
import com.github.nikita.model.Categories;
import com.github.nikita.model.Role;
import com.github.nikita.model.User;
import io.javalin.http.Context;

import java.io.IOException;
import java.sql.SQLException;

public class CategoriesController {

    public static void createUser(Context ctx) throws IOException, SQLException {
        String json = ctx.body();
        Categories categ;
        ObjectMapper obMap = ObjectMapperFactory.createObjectMapper(User.class);
        if (SecurityService.authentication(ctx)) {
            if (SecurityService.authorization(ctx) == Role.ADMIN) {
                categ = obMap.readValue(json, Categories.class);
                DatabaseConfiguration.categDao.create(categ);
                ctx.status(201);
            } else {
                ctx.status(403);
            }
        } else {
            ctx.status(401);
        }
    }

    public static void getAllUser(Context ctx) throws SQLException, JsonProcessingException {
        if (SecurityService.authentication(ctx)) {
            ObjectMapper obMap = ObjectMapperFactory.createObjectMapper(Categories.class);
            ctx.result(obMap.writeValueAsString(DatabaseConfiguration.categDao.queryForAll()));
        } else
            ctx.status(401);
    }

    public static void getOneUser(Context ctx) throws SQLException, JsonProcessingException {
        if (SecurityService.authentication(ctx)) {
            ObjectMapper obMap = ObjectMapperFactory.createObjectMapper(Categories.class);
            int id = Integer.parseInt(ctx.pathParam("id"));
            for (Categories categ : DatabaseConfiguration.categDao.queryForAll()) {
                if (categ.getId() == id) {
                    ctx.result(obMap.writeValueAsString(categ));
                }
            }
        } else
            ctx.status(401);
    }

    public static void updateUser(Context ctx) throws SQLException, IOException {
        String json = ctx.body();
        Categories categ;
        ObjectMapper obMap = ObjectMapperFactory.createObjectMapper(User.class);
        if (SecurityService.authentication(ctx)) {
            if (SecurityService.authorization(ctx) == Role.ADMIN) {
                categ = obMap.readValue(json, Categories.class);
                DatabaseConfiguration.categDao.update(categ);
            } else {
                ctx.status(403);
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
            } else {
                ctx.status(403);
            }
        } else {
            ctx.status(401);
        }
    }
}
