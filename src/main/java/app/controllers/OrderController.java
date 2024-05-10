package app.controllers;

import app.entities.User;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;


public class OrderController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
       app.post("/makeorder", ctx -> makeOrder(ctx, connectionPool));
    }

    public static void makeOrder(Context ctx, ConnectionPool connectionPool) {

        User currentUser = ctx.sessionAttribute("currentUser");
        int materialID = Integer.parseInt(ctx.formParam("materialID"));

        OrderMapper.orderOrderline(currentUser.getUserID(),materialID, connectionPool);
        ctx.attribute("message", "makeOrder successfull (OrderController");
    }
}
