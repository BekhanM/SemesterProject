package app.controllers;

import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.Objects;

public class UserController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.post("login", ctx -> login(ctx, connectionPool));
        app.get("createuser", ctx -> ctx.render("login.html"));
        app.post("createuser", ctx -> createUser(ctx, connectionPool));
        app.get("makeyourowncarport", ctx -> ctx.render("makeyourowncarport.html"));
    }

    private static void createUser(Context ctx, ConnectionPool connectionPool) {
        //Hent form parametre
        String email = ctx.formParam("email");
        String password1 = ctx.formParam("password1");
        String firstname = ctx.formParam("firstname");
        String lastname = ctx.formParam("lastname");
        String address = ctx.formParam("address");
        int postnr = Integer.parseInt(ctx.formParam("postnr"));
        String city = ctx.formParam("city");
        int tlfnr = Integer.parseInt(ctx.formParam("tlfnr"));


        if (validatePassword(password1) && checkEmailAt(Objects.requireNonNull(email))) {
            try {
                UserMapper.createuser(email, password1, firstname, lastname, address, postnr, city, tlfnr, connectionPool);
                ctx.attribute("message", "Account -" + email + "- has been created. \n Please login.");
                ctx.render("homepage.html");
            } catch (DatabaseException e) {
                ctx.attribute("message", "Username already exists.");
                ctx.render("login.html");
            }
        } else if (email.equals(null)){
            ctx.attribute("message", "The passwords need to match.");
            ctx.render("login.html");
        } else {
            ctx.attribute("message", "Email is not valid or password does not meet requirements.");
            ctx.render("login.html");
        }
    }

    private static void logout(Context ctx) {
        ctx.req().getSession().invalidate();
        ctx.redirect("/");
    }

    public static void login(Context ctx, ConnectionPool connectionPool) {
        System.out.println("NU ER DU LOGGET IND ");
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");

        //Check om bruger findes i DB med de angivne username + password
        try {
            User user = UserMapper.login(email, password, connectionPool);
            ctx.sessionAttribute("currentUser", user);

            ctx.attribute("role", user.getRole());

            if (user.getRole().equals("admin")) {
                ctx.render("admin.html");
            } else {
                ctx.render("/homepage");
            }


        } catch (DatabaseException e) {
            //Hvis nej, send tilbage til login side med fejl besked
            ctx.attribute("message", e.getMessage());
            ctx.render("login.html");
        }
    }

    public static boolean checkUpperCase(String str) {
        char c;
        boolean upperCaseFlag = false;
        boolean lowerCaseFlag = false;
        for (int i = 0; i < str.length(); i++) {
            c = str.charAt(i);
            if (Character.isUpperCase(c)) {
                upperCaseFlag = true;
            } else if (Character.isLowerCase(c)) {
                lowerCaseFlag = true;
            }
            if (upperCaseFlag && lowerCaseFlag)
                return true;
        }
        System.out.println("Kodeordet skal have et stort bogstav");
        return false;
    }

    public static boolean checkLength(String str) {

        if (str.length() < 129 && str.length() > 7) {
            return true;
        } else {
            System.out.println("Kodeordet skal mindst være 8 karakterer langt");
            return false;
        }
    }

    public static boolean checkNumeric(String str) {
        char c;
        boolean numberFlag = false;
        for (int i = 0; i < str.length(); i++) {
            c = str.charAt(i);
            if (Character.isDigit(c)) {
                return true;
            }
        }

        if (!numberFlag) {
            System.out.println("Kodeordet skal have tal.");
        }
        return false;
    }

    public static boolean validatePassword(String password) {
        boolean i = checkNumeric(password);
        boolean j = checkLength(password);
        boolean k = checkUpperCase(password);
        if (i && j && k) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkEmailAt(String email) {
        for (int i = 0; i < email.length(); i++) {
            if (email.charAt(i) == '@') {
                return true;
            }
        }
        return false;
    }

}
