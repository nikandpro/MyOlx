package com.github.nikita.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.nikita.ObjectMapperFactory;
import com.github.nikita.SecurityService;
import com.github.nikita.configuration.DatabaseConfiguration;
import com.github.nikita.model.*;
import io.javalin.http.Context;

import java.io.IOException;
import java.sql.SQLException;

public class CategoriesController {

    public static void createCategories(Context ctx) throws IOException, SQLException {
        String json = ctx.body();
        Categories categ;
        ObjectMapper obMap = ObjectMapperFactory.createObjectMapper(CategoriesProduct.class);
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

    public static void getAllCategories(Context ctx) throws SQLException, JsonProcessingException {
        if (SecurityService.authentication(ctx)) {
            ObjectMapper obMap = ObjectMapperFactory.createObjectMapper(Categories.class);
            ctx.result(obMap.writeValueAsString(DatabaseConfiguration.categDao.queryForAll()));
        } else
            ctx.status(401);
    }

    public static void getOneCategories(Context ctx) throws SQLException, JsonProcessingException {
        if (SecurityService.authentication(ctx)) {
            ObjectMapper obMap = ObjectMapperFactory.createObjectMapper(Categories.class);
            int id = Integer.parseInt(ctx.pathParam("id"));
            Categories categ = DatabaseConfiguration.categDao.queryForId(id);
            if (categ != null) {
                ctx.result(obMap.writeValueAsString(categ));
                ctx.status(201);
            } else
                ctx.status(404);
        } else
            ctx.status(401);
    }

    public static void updateCategories(Context ctx) throws SQLException, IOException {
        String json = ctx.body();
        Categories categ;
        if (SecurityService.authentication(ctx)) {
            if (SecurityService.authorization(ctx) == Role.ADMIN) {
                int id = Integer.parseInt(ctx.pathParam("id"));
                ObjectMapper obMap = ObjectMapperFactory.createObjectMapper(Categories.class);
                categ = obMap.readValue(json, Categories.class);
                categ.setId(id);
                DatabaseConfiguration.categDao.update(categ);
                ctx.status(201);
            } else {
                ctx.status(403);
            }
        } else {
            ctx.status(401);
        }
    }

    public static void deleteCategories(Context ctx) throws SQLException {
        if (SecurityService.authentication(ctx)) {
            if (SecurityService.authorization(ctx) == Role.ADMIN) {
                int id = Integer.parseInt(ctx.pathParam("id"));
                DatabaseConfiguration.categDao.deleteById(id);
                System.out.println(id);
                ctx.status(204);
            } else {
                ctx.status(403);
            }
        } else {
            ctx.status(401);
        }
    }
}
