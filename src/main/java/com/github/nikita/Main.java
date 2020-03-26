package com.github.nikita;

import com.github.nikita.controller.ProductController;
import com.github.nikita.controller.UserController;
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
        //app.patch("product/:id", ProductController:);
        app.delete("product/:id", ProductController::deleteProduct);
    }

}
