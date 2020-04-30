package com.github.nikita;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.j256.ormlite.dao.Dao;
import io.javalin.http.Context;

import java.sql.SQLException;
import java.util.List;

public class Service {

    public static <T, U> List<T> get(Context ctx, Dao<T, U> dao, ObjectMapper obMap) throws SQLException, JsonProcessingException {
        if (ctx.queryParam("size")!=null) {
            Long page = Long.parseLong(ctx.queryParam("page"));
            Long size = Long.parseLong(ctx.queryParam("size"));
            dao.queryBuilder().offset(page).limit(size);
            return dao.queryBuilder().offset(page).limit(size).query();
        } else {
            return dao.queryForAll();
        }
    }

}
