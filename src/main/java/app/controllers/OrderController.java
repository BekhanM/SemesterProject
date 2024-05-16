package app.controllers;

import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import app.services.CarportPartsCalculator;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class OrderController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.post("/makeorder", ctx -> makeOrder(ctx, connectionPool));
    }

    public static void makeOrder(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        User currentUser = ctx.sessionAttribute("currentUser");
        int carportLength = Integer.parseInt(ctx.formParam("carportLength"));
        int carportWidth = Integer.parseInt(ctx.formParam("carportWidth"));

        CarportPartsCalculator calculator = new CarportPartsCalculator(new OrderMapper(), connectionPool);
        calculator.calcCarportParts(currentUser.getUserID(), carportLength, carportWidth);
        ctx.attribute("message", "makeOrder successful (OrderController)");
    }
}
