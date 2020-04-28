package com.github.nikita.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.nikita.ObjectMapperFactory;
import com.github.nikita.SecurityService;
import com.github.nikita.configuration.DatabaseConfiguration;
import com.github.nikita.model.Product;
import com.github.nikita.model.Reviews;
import com.github.nikita.model.Role;
import com.github.nikita.model.User;
import io.javalin.http.Context;

import java.io.IOException;
import java.sql.SQLException;

public class ReviewsController {
    public static void createReviews(Context ctx) throws IOException, SQLException {
        String json = ctx.body();
        Reviews reviews;
        ObjectMapper obMap = ObjectMapperFactory.createObjectMapper(Reviews.class);
        reviews = obMap.readValue(json, Reviews.class);
        reviews.setUser(SecurityService.searchUser(ctx));
        DatabaseConfiguration.revDao.create(reviews);
        ctx.status(201);
    }

    public static void getAllReviews(Context ctx) throws SQLException, JsonProcessingException {
        if (SecurityService.authentication(ctx)) {
            ObjectMapper obMap = ObjectMapperFactory.createObjectMapper(Reviews.class);
            SecurityService.get(ctx, DatabaseConfiguration.revDao, obMap);
            ctx.status(200);
        } else
            ctx.status(401);
    }

    public static void getOneReviews(Context ctx) throws SQLException, JsonProcessingException {
        if (SecurityService.authentication(ctx)) {
            ObjectMapper obMap = ObjectMapperFactory.createObjectMapper(Reviews.class);
            int id = Integer.parseInt(ctx.pathParam("id"));
            Reviews reviews = DatabaseConfiguration.revDao.queryForId(id);
            if (reviews != null) {
                ctx.result(obMap.writeValueAsString(reviews));
                ctx.status(200);
            } else
                ctx.status(404);
        } else
            ctx.status(401);
    }

    public static void updateReviews(Context ctx) throws SQLException, IOException {
        String json = ctx.body();
        Reviews reviews;
        if (SecurityService.authentication(ctx)) {
            ObjectMapper obMap = ObjectMapperFactory.createObjectMapper(Reviews.class);
            reviews = obMap.readValue(json, Reviews.class);
            int id = Integer.parseInt(ctx.pathParam("id"));
            if (SecurityService.authorization(ctx) == Role.ADMIN) {
                if (DatabaseConfiguration.revDao.queryForId(id) != null) {
                    reviews.setId(id);
                    reviews.setUser(SecurityService.searchUser(ctx));
                    DatabaseConfiguration.revDao.update(reviews);
                    ctx.status(201);
                } else {
                    ctx.status(404);
                }
            } else if (SecurityService.authorization(ctx) == Role.USER) {
                if (DatabaseConfiguration.revDao.queryForId(id) != null) {
                    if (DatabaseConfiguration.revDao.queryForId(id).getUser().getId() == SecurityService.searchUser(ctx).getId()) {
                        reviews.setId(id);
                        reviews.setUser(SecurityService.searchUser(ctx));
                        DatabaseConfiguration.revDao.update(reviews);
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

    public static void deleteReviews(Context ctx) throws SQLException {
        int id = Integer.parseInt(ctx.pathParam("id"));
        if (SecurityService.authentication(ctx)) {
            if (SecurityService.authorization(ctx) == Role.ADMIN) {
                if (DatabaseConfiguration.revDao.queryForId(id) != null) {
                    DatabaseConfiguration.revDao.deleteById(id);
                    ctx.status(204);
                } else {
                    ctx.status(404);
                }
            } else if (SecurityService.authorization(ctx) == Role.USER) {
                if (DatabaseConfiguration.revDao.queryForId(id) != null) {
                    if (SecurityService.searchUser(ctx).getId() == DatabaseConfiguration.revDao.queryForId(id).getUser().getId()) {
                        DatabaseConfiguration.revDao.deleteById(id);
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
