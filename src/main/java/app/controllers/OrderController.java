package app.controllers;

import app.entities.OrderDetails;
import app.entities.Orders;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import app.services.CarportPartsCalculator;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class OrderController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
       app.post("/makeorder", ctx -> makeOrder(ctx, connectionPool));
       app.post("/orderlist", ctx -> getAllOrdersForSearchedUser(ctx, connectionPool));
       app.get("/orderlist", ctx -> ctx.render("orderlist.html"));
       app.post("/removeorder", ctx -> removeOrder(ctx, connectionPool));
       app.get("/userorders", ctx -> getUserOrdersWithDetails(ctx, connectionPool));

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


    private static void getUserOrdersWithDetails(Context ctx, ConnectionPool connectionPool) {
        User currentUser = ctx.sessionAttribute("currentUser");

        // Finder brugeren, som er logget ind og tager fat i hans unikke userID.
        int userID = currentUser.getUserID();

        try {
            // Henter ordre detaljer for brugeren vha. ordermapperen. Og gemmer dem i en ny orderDetailsListe af orderDetails objekter.
            // OrderDetails er af en entitet som har alle nødvendige ordre detaljer, vi gerne vil vise brugeren.
            List<OrderDetails> orderDetailsList = OrderMapper.getUserOrdersWithDetails(userID, connectionPool);

            // Vi gruppere listen af ordredetaljer, som er tilhørende currentlyLoggedUser vha. Map og Collectors.
            // Hver orderdetail har naturligvis sin egen orderID. Og det er den vi grupperer efter.
            // Nøglen er orderID, og værdien er listen af orderdetails.
            // Så i sidste ende samler vi bare alle orderdetails i et Map vha. deres orderID
            // Se denne video hvis der brug for det, boys: https://www.youtube.com/watch?v=U_jszwBOHLM
            Map<Integer, List<OrderDetails>> orderDetailsGroupedByOrderID = orderDetailsList.stream()
                    .collect(Collectors.groupingBy(OrderDetails::getOrderID));

            ctx.attribute("orderDetailsGroupedByOrderID", orderDetailsGroupedByOrderID);
            ctx.render("userorders.html");
        } catch (DatabaseException e) {
            ctx.status(500).result("Error retrieving detailed orders for the user: " + e.getMessage());
        }
    }
}
