package app.controllers;

import app.entities.OrderDetails;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.post("login", ctx -> login(ctx, connectionPool));
        app.get("createuser", ctx -> ctx.render("login.html"));
        app.post("createuser", ctx -> createUser(ctx, connectionPool));
        app.get("makeyourowncarport", ctx -> ctx.render("makeyourowncarport.html"));
        app.get("admin", ctx -> getAllUsersDetail(ctx, connectionPool));
        app.post("admin", ctx -> getAllUsersDetail(ctx, connectionPool));
        app.get("admin/viewUserOrders", ctx -> viewUserOrders(ctx, connectionPool));
        app.post("removeuser", ctx -> removeUser(ctx, connectionPool));
        app.post("logout", ctx -> logout(ctx));
        app.post("allUsers", ctx -> showAllUsers(ctx, connectionPool));
    }

    private static void viewUserOrders(Context ctx, ConnectionPool connectionPool) {
        int userID = Integer.parseInt(ctx.queryParam("userID"));

        try {
            User user = UserMapper.getUserById(userID, connectionPool);

            List<OrderDetails> orderDetailsList = OrderMapper.getUserOrdersWithDetails(userID, connectionPool);
            Map<Integer, List<OrderDetails>> orderDetailsGroupedByOrderID = orderDetailsList.stream()
                    .collect(Collectors.groupingBy(OrderDetails::getOrderID));
            ctx.attribute("orderDetailsGroupedByOrderID", orderDetailsGroupedByOrderID);
            ctx.attribute("userID", userID);
            ctx.attribute("fullname", user.getFirstName() + " " + user.getLastName());
            ctx.render("adminSearchSeesOrderdetails.html");
        } catch (DatabaseException e) {
            ctx.status(500).result("Error retrieving user orders: " + e.getMessage());
        }
    }

    private static void createUser(Context ctx, ConnectionPool connectionPool) {
        String email = ctx.formParam("email");
        String password1 = ctx.formParam("password1");
        String firstname = ctx.formParam("firstname");
        String lastname = ctx.formParam("lastname");
        String address = ctx.formParam("adresse");
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
                ctx.render("homepage.html");
            }
        } else if (email.equals(null)) {
            ctx.attribute("message", "The passwords need to match.");
            ctx.render("homepage.html");
        } else {
            ctx.attribute("message", "Email is not valid or password does not meet requirements.");
            ctx.render("homepage.html");
        }
    }

    private static void logout(Context ctx) {
        ctx.req().getSession().invalidate();
        ctx.redirect("/");
    }

    public static void login(Context ctx, ConnectionPool connectionPool) {
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");

        try {
            User user = UserMapper.login(email, password, connectionPool);
            ctx.sessionAttribute("currentUser", user);

            ctx.attribute("role", user.getRole());

            if (user.getRole().equals("admin")) {
                ctx.redirect("admin");
            } else {
                ctx.render("/homepage");
            }

        } catch (DatabaseException e) {
            ctx.attribute("loginError", "Forkert email eller kodeord. Prøv igen.");
            ctx.render("homepage.html");
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
        return false;
    }

    public static boolean checkLength(String str) {
        return str.length() < 129 && str.length() > 7;
    }

    public static boolean checkNumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static boolean validatePassword(String password) {
        return checkNumeric(password) && checkLength(password) && checkUpperCase(password);
    }

    public static boolean checkEmailAt(String email) {
        return email.contains("@");
    }

    private static void getAllUsersDetail(Context ctx, ConnectionPool connectionPool) {
        String userEmail = ctx.formParam("email");
        try {
            List<User> userList;
            if (userEmail == null || userEmail.isEmpty()) {
                System.out.println("have we even beeen here?");
                userList = UserMapper.showAllUsers(connectionPool); // Fetch all users if no email is provided
            } else {
                userList = UserMapper.getAllUsersDetail(userEmail, connectionPool); // Filter users by email
            }
            ctx.attribute("userList", userList);
            System.out.println("Number of users fetched: " + userList.size()); // Debug statement
            ctx.render("admin.html");
        } catch (DatabaseException e) {
            ctx.status(500).result("Error retrieving user details: " + e.getMessage());
        }
    }

    public static void showAllUsers(Context ctx, ConnectionPool connectionPool) {
        try {
            List<User> allUsers = UserMapper.showAllUsers(connectionPool);
            ctx.attribute("allUsers", allUsers);
            ctx.render("admin.html");
        } catch (DatabaseException e) {
            ctx.status(500).result("Error retrieving user details: " + e.getMessage());
        }
    }

    private static void removeUser(Context ctx, ConnectionPool connectionPool) {
        int userID = Integer.parseInt(ctx.formParam("userID"));
        try {
            UserMapper.removeUser(userID, connectionPool);
            ctx.redirect("/admin");
        } catch (DatabaseException e) {
            ctx.status(500).result("Error removing user: " + e.getMessage());
        }
    }
}
