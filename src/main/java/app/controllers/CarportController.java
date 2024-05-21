package app.controllers;

import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import app.persistence.OrderlineMapper;
import app.persistence.MaterialMapper;
import app.services.CarportPartsCalculator;
import app.services.CarportSvg;
import app.services.OrderService;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.Locale;

public class CarportController {


    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/makeyourowncarport", ctx -> showCarport(ctx));
        app.post("/showCarport", ctx -> showCarport(ctx));
        app.get("/showCarport", ctx -> ctx.render("showCarport.html"));
        app.post("/calculateCarport", ctx -> calculateCarport(ctx, connectionPool));
    }

    private static void calculateCarport(Context ctx, ConnectionPool connectionPool) {
        try {
            User currentUser = ctx.sessionAttribute("currentUser");
            if (currentUser == null) {
                ctx.status(401).result("User is not logged in");
                return;
            }

            int userID = currentUser.getUserID();
            int carportLength = Integer.parseInt(ctx.formParam("carportLength"));
            int carportWidth = Integer.parseInt(ctx.formParam("carportWidth"));

            OrderMapper orderMapper = new OrderMapper();
            OrderlineMapper orderlineMapper = new OrderlineMapper();
            MaterialMapper materialMapper = new MaterialMapper();
            OrderService orderService = new OrderService(orderMapper, orderlineMapper, materialMapper, connectionPool);
            CarportPartsCalculator carportPartsCalculator = new CarportPartsCalculator(orderService);

            carportPartsCalculator.calcCarportParts(userID, carportLength, carportWidth);

            ctx.attribute("message", "Vi har modtaget ordren for en carport med længde: " + carportLength + " cm., og bredde: " + carportWidth + " cm.");
            ctx.render("orderconfirmation.html");
        } catch (NumberFormatException | DatabaseException e) {
            ctx.attribute("message", "Kaput, kig i calccarport controller " + e.getMessage());
            ctx.render("makeyourowncarport.html");
        }
    }

    public static void showCarport(Context ctx) {
        Locale.setDefault(new Locale("US"));
        CarportSvg svg = new CarportSvg(500, 500);
        String svgPic = svg.toString();

        ctx.attribute("svg", svgPic);
        ctx.render("showCarport.html");
    }

}
