package app.controllers;

import app.entities.Orders;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import app.services.CarportPartsCalculator;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.Locale;
import app.services.CarportSvg;

import java.util.List;


public class OrderController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
       app.post("/makeorder", ctx -> makeOrder(ctx, connectionPool));
       app.post("/orderlist", ctx -> getAllOrdersForSearchedUser(ctx, connectionPool));
       app.get("/orderlist", ctx -> ctx.render("orderlist.html"));
       app.post("/removeorder", ctx -> removeOrder(ctx, connectionPool));
    }

    public static void showCarport(Context ctx)
    {
        Locale.setDefault(new Locale("US"));
        CarportSvg svg = new CarportSvg(250, 500);

        ctx.attribute("svg", svg.toString());
        ctx.render("showCarport.html");
    }

    public static void makeOrder(Context ctx, ConnectionPool connectionPool) {

        User currentUser = ctx.sessionAttribute("currentUser");
        int materialID = Integer.parseInt(ctx.formParam("materialID"));

        //OrderMapper.addOrderline(currentUser.getUserID(),materialID, connectionPool);
        ctx.attribute("message", "makeOrder successfull (OrderController");
    }


    private static void getAllOrdersForSearchedUser(Context ctx, ConnectionPool connectionPool) {
        System.out.println("DU ER NU I GETALLORDERSFORCURRENTUSER I CONTROLLEREN");

        int userID = Integer.parseInt(ctx.formParam("userID"));

        try {
            List<Orders> orderList = OrderMapper.getAllOrdersForSearchedUser(userID, connectionPool);
            ctx.attribute("orderList", orderList);
            ctx.render("orderlist.html");
        } catch (DatabaseException e) {
            ctx.status(500).result("Error retrieving orders for the current user." + e.getMessage());
        }
    }


    private static void removeOrder(Context ctx, ConnectionPool connectionPool) {
        System.out.println("NU ER DU I REMOVEORDER I CONTROLLEREN");

        int orderID = Integer.parseInt(ctx.formParam("orderID"));
        try {
            OrderMapper.removeOrder(orderID, connectionPool);
            ctx.redirect("/orderlist");
        } catch (DatabaseException e) {
            ctx.status(500).result("Error removing order: " + e.getMessage());
        }
    }
}
