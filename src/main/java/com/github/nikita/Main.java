package com.github.nikita;

import com.github.nikita.controller.*;
import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7123);
        app.post("user", UserController::createUser);
        app.get("user", UserController::getAllUser);
        app.get("user/:id", UserController::getOneUser);
        app.patch("user/:id", UserController::updateUser);
        app.delete("user/:id", UserController::deleteUser);

        app.post("product", ProductController::createProduct);
        app.get("product", ProductController::getAllProduct);
        app.get("product/:id", ProductController::getOneProduct);
        app.patch("product/:id", ProductController::updateProduct);
        app.delete("product/:id", ProductController::deleteProduct);

        app.post("categories", CategoriesController::createCategories);
        app.get("categories", CategoriesController::getAllCategories);
        app.get("categories/:id", CategoriesController::getOneCategories);
        app.patch("categories/:id", CategoriesController::updateCategories);
        app.delete("categories/:id", CategoriesController::deleteCategories);

        app.post("categoriesProduct", CategoriesProductController::createCategoriesProduct);
        app.get("categoriesProduct", CategoriesProductController::getAllCategoriesProduct);
        app.get("categoriesProduct/:id", CategoriesProductController::getOneCategoriesProduct);
        app.patch("categoriesProduct/:id", CategoriesProductController::updateCategoriesProduct);
        app.delete("categoriesProduct/:id", CategoriesProductController::deleteCategoriesProduct);

        app.post("reviews", ReviewsController::createReviews);
        app.get("reviews", ReviewsController::getAllReviews);
        app.get("reviews/:id", ReviewsController::getOneReviews);
        app.patch("reviews/:id", ReviewsController::updateReviews);
        app.delete("reviews/:id", ReviewsController::deleteReviews);

        app.post("usTran", UserTransactionController::createUserTran);
        app.get("usTran", UserTransactionController::getAllUserTran);
        app.get("usTran/:id", UserTransactionController::getOneUserTran);
        app.delete("usTran/:id", UserTransactionController::deleteUserTran);
    }

}
