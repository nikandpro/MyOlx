package com.github.nikita.configuration;

import com.github.nikita.model.*;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.sql.SQLException;

public class DatabaseConfiguration {
    public static ConnectionSource connectionSource;
    public static Dao<User, Integer> userDao;
    public static Dao<Categories, Integer> categDao;
    public static Dao<CategoriesProduct, Integer> catProdDao;
    public static Dao<Product, Integer> prodDao;
    public static Dao<Reviews, Integer> revDao;
    public static Dao<UserTransaction, Integer> usTranDao;

    static {
        try {
            connectionSource = new JdbcConnectionSource("jdbc:sqlite:C:\\Users\\User\\Desktop\\БД\\UserAuthentication.db");
            TableUtils.createTableIfNotExists(connectionSource, User.class);
            TableUtils.createTableIfNotExists(connectionSource, Categories.class);
            TableUtils.createTableIfNotExists(connectionSource, CategoriesProduct.class);
            TableUtils.createTableIfNotExists(connectionSource, Product.class);
            TableUtils.createTableIfNotExists(connectionSource, Reviews.class);
            TableUtils.createTableIfNotExists(connectionSource, UserTransaction.class);

            userDao = DaoManager.createDao(connectionSource, User.class);
            categDao = DaoManager.createDao(connectionSource, Categories.class);
            catProdDao = DaoManager.createDao(connectionSource, CategoriesProduct.class);
            prodDao = DaoManager.createDao(connectionSource, Product.class);
            revDao = DaoManager.createDao(connectionSource, Reviews.class);
            usTranDao = DaoManager.createDao(connectionSource, UserTransaction.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
