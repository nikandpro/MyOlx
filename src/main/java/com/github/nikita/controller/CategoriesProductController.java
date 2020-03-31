package com.github.nikita.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.nikita.ObjectMapperFactory;
import com.github.nikita.SecurityService;
import com.github.nikita.configuration.DatabaseConfiguration;
import com.github.nikita.model.Product;
import com.github.nikita.model.CategoriesProduct;
import com.github.nikita.model.Role;
import io.javalin.http.Context;

import java.io.IOException;
import java.sql.SQLException;

public class CategoriesProductController {

    public static void createCategoriesProduct(Context ctx) throws IOException, SQLException {
        String json = ctx.body();
        CategoriesProduct categProduct;
        ObjectMapper obMap = ObjectMapperFactory.createObjectMapper(CategoriesProduct.class);
        categProduct = obMap.readValue(json, CategoriesProduct.class);
        DatabaseConfiguration.catProdDao.create(categProduct);
        ctx.status(201);
    }

    public static void getAllCategoriesProduct(Context ctx) throws SQLException, JsonProcessingException {
        if (SecurityService.authentication(ctx)) {
            ObjectMapper obMap = ObjectMapperFactory.createObjectMapper(CategoriesProduct.class);
            ctx.result(obMap.writeValueAsString(DatabaseConfiguration.catProdDao.queryForAll()));
            ctx.status(201);
        } else
            ctx.status(401);
    }

    public static void getOneCategoriesProduct(Context ctx) throws SQLException, JsonProcessingException {
        if (SecurityService.authentication(ctx)) {
            ObjectMapper obMap = ObjectMapperFactory.createObjectMapper(CategoriesProduct.class);
            int id = Integer.parseInt(ctx.pathParam("id"));
            CategoriesProduct categProd = DatabaseConfiguration.catProdDao.queryForId(id);
            if (categProd != null) {
                ctx.result(obMap.writeValueAsString(categProd));
                ctx.status(201);
            } else
                ctx.status(404);
        } else
            ctx.status(401);
    }

    public static void updateCategoriesProduct(Context ctx) throws SQLException, IOException {
        String json = ctx.body();
        CategoriesProduct categProd;
        if (SecurityService.authentication(ctx)) {
            ObjectMapper obMap = ObjectMapperFactory.createObjectMapper(CategoriesProduct.class);
            categProd = obMap.readValue(json, CategoriesProduct.class);
            int id = Integer.parseInt(ctx.pathParam("id"));
            if (SecurityService.authorization(ctx) == Role.ADMIN) {
                categProd.setId(id);
                DatabaseConfiguration.catProdDao.update(categProd);
                ctx.status(201);
            } else if (SecurityService.authorization(ctx) == Role.USER) {
                System.out.println(DatabaseConfiguration.catProdDao.queryForId(id).getProduct().getSeller().getId());
                if (DatabaseConfiguration.catProdDao.queryForId(id).getProduct().getSeller().getId() == SecurityService.searchUser(ctx).getId()) {
                    System.out.println("dfghfh");
                    categProd.setId(id);
                    DatabaseConfiguration.catProdDao.update(categProd);
                    ctx.status(201);
                } else {
                    ctx.status(403);
                }
            }
        } else {
            ctx.status(401);
        }
    }

    public static void deleteCategoriesProduct(Context ctx) throws SQLException {
        int id = Integer.parseInt(ctx.pathParam("id"));
        if (SecurityService.authentication(ctx)) {
            if (SecurityService.authorization(ctx) == Role.ADMIN) {
                DatabaseConfiguration.catProdDao.deleteById(id);
                ctx.status(204);
            } else if (SecurityService.authorization(ctx) == Role.USER) {
                if (DatabaseConfiguration.catProdDao.queryForId(id) != null) {
                    if (SecurityService.searchUser(ctx).getId() == DatabaseConfiguration.catProdDao.queryForId(id).getProduct().getSeller().getId()) {
                        DatabaseConfiguration.catProdDao.deleteById(id);
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
