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

public class ProductController {

    public static void createProduct(Context ctx) throws IOException, SQLException {
        String json = ctx.body();
        Product product;
        ObjectMapper obMap = ObjectMapperFactory.createObjectMapper(Product.class);
        product = obMap.readValue(json, Product.class);
        product.setSeller(SecurityService.searchUser(ctx));
        DatabaseConfiguration.prodDao.create(product);
        ctx.status(201);
    }

    public static void getAllProduct(Context ctx) throws SQLException, JsonProcessingException {
        if (SecurityService.authentication(ctx)) {
            ObjectMapper obMap = ObjectMapperFactory.createObjectMapper(Product.class);
            ctx.result(obMap.writeValueAsString(DatabaseConfiguration.prodDao.queryForAll()));
            ctx.status(201);
        } else
            ctx.status(401);
    }

    public static void getOneProduct(Context ctx) throws SQLException, JsonProcessingException {
        if (SecurityService.authentication(ctx)) {
            ObjectMapper obMap = ObjectMapperFactory.createObjectMapper(Product.class);
            int id = Integer.parseInt(ctx.pathParam("id"));
            Product product = DatabaseConfiguration.prodDao.queryForId(id);
            if (product != null) {
                ctx.result(obMap.writeValueAsString(product));
                ctx.status(201);
            } else
                ctx.status(404);
        } else
            ctx.status(401);
    }

    public static void updateProduct(Context ctx) throws SQLException, IOException {
        String json = ctx.body();
        Product prod;
        if (SecurityService.authentication(ctx)) {
            ObjectMapper obMap = ObjectMapperFactory.createObjectMapper(Product.class);
            prod = obMap.readValue(json, Product.class);
            prod.setSeller(SecurityService.searchUser(ctx));
            int id = Integer.parseInt(ctx.pathParam("id"));
            if (SecurityService.authorization(ctx) == Role.ADMIN) {
                if (DatabaseConfiguration.prodDao.queryForId(id) != null) {
                    prod.setId(id);
                    DatabaseConfiguration.prodDao.update(prod);
                    ctx.status(201);
                } else {
                    ctx.status(404);
                }
            } else if (SecurityService.authorization(ctx) == Role.USER) {
                if (DatabaseConfiguration.prodDao.queryForId(id) != null) {
                    if (DatabaseConfiguration.prodDao.queryForId(id).getSeller().getId() == SecurityService.searchUser(ctx).getId()) {
                        prod.setId(id);
                        DatabaseConfiguration.prodDao.update(prod);
                        ctx.status(201);
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

    public static void deleteProduct(Context ctx) throws SQLException {
        int id = Integer.parseInt(ctx.pathParam("id"));
        if (SecurityService.authentication(ctx)) {
            if (SecurityService.authorization(ctx) == Role.ADMIN) {
                if (DatabaseConfiguration.prodDao.queryForId(id) != null) {
                    DatabaseConfiguration.prodDao.deleteById(id);
                    ctx.status(204);
                } else {
                    ctx.status(404);
                }
            } else if (SecurityService.authorization(ctx) == Role.USER) {
                if (DatabaseConfiguration.prodDao.queryForId(id) != null) {
                    if (SecurityService.searchUser(ctx).getId() == DatabaseConfiguration.prodDao.queryForId(id).getSeller().getId()) {
                        DatabaseConfiguration.prodDao.deleteById(id);
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
