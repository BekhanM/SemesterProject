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
        app.get("admin", ctx -> ctx.render("admin.html"));
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
        //Hent form parametre
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

    public static void getUserDetails(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        // Retrieve the userID from the current session

        User currentUser = ctx.sessionAttribute("currentUser");

        int userID = currentUser.getUserID();

        try {
            System.out.println("UserID: " + userID);
            List<User> users = UserMapper.getUserDetails(userID, connectionPool);
            System.out.println("Retrieved UserDetails: " + users.size()); // Log the size of the orderLine list

            ctx.attribute("userList", users);
            ctx.render("admin.html");
        } catch (DatabaseException e) {
            System.err.println("Error retrieving users details: " + e.getMessage());
            ctx.status(500).result("Error retrieving users details: " + e.getMessage());
        }
    }

    private static void getAllUsersDetail(Context ctx, ConnectionPool connectionPool) {
        String userEmail = ctx.formParam("email");
        try {
            List<User> userList = UserMapper.getAllUsersDetail(userEmail, connectionPool);
            ctx.attribute("userList", userList);
            ctx.render("admin.html");
        } catch (DatabaseException e) {
            ctx.status(500).result("Error retrieving user details: " + e.getMessage());
        }
    }

    public static void showAllUsers(Context ctx, ConnectionPool connectionPool) {
        System.out.println("DU ER I SHOWALL USERSCONTROLLER");

        try {
            List<User> allUsers = UserMapper.showAllUsers(connectionPool);
            ctx.attribute("allUsers", allUsers);
            ctx.render("admin.html");
        } catch (DatabaseException e) {
            System.out.println("sum ting wong");
            ctx.status(500).result("Error retrieving user details: " + e.getMessage());
        }
    }

    private static void removeUser(Context ctx, ConnectionPool connectionPool) {
        System.out.println("NU ER DU I REMOVEUSER I CONTROLLEREN");

        int userID = Integer.parseInt(ctx.formParam("userID"));
        try {
            UserMapper.removeUser(userID, connectionPool);
            ctx.redirect("/admin");
        } catch (DatabaseException e) {
            ctx.status(500).result("Error removing user: " + e.getMessage());
        }
    }
}