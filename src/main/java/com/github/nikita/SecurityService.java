package com.github.nikita;

import com.github.nikita.configuration.DatabaseConfiguration;
import com.github.nikita.model.Role;
import com.github.nikita.model.User;
import io.javalin.http.Context;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

public class SecurityService {
    public static String encryption(String password) {
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt(10));
        return hashed;
    }

    public static boolean authentication(Context ctx) throws SQLException {
        boolean check=false;
        String userLogin = ctx.basicAuthCredentials().getUsername();
        String userPas = ctx.basicAuthCredentials().getPassword();
        for (User us: DatabaseConfiguration.userDao.queryForAll()) {
            if (us.getLogin().equals(userLogin)  && BCrypt.checkpw(userPas, us.getPassword())) {
                check=true;
            }
        }
        return check;
    }

    public static Role authorization(Context ctx) throws SQLException {
        if (searchUser(ctx).getRole()==Role.ADMIN) return Role.ADMIN;
        else return Role.USER;
    }

    public static User searchUser(Context ctx) throws SQLException {
        String userName = ctx.basicAuthCredentials().getUsername();
        User user=null;
        for (User us : DatabaseConfiguration.userDao.queryForAll()) {
            if (us.getLogin().equals(userName)) {
                user=us;
            }
        }
        if (user!=null)
            return user;
        else {
            ctx.status(404);
            throw new RuntimeException();
        }
    }
}
