package app.controllers;

import app.entities.OrderDetails;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import app.persistence.OrderlineMapper;
import app.persistence.MaterialMapper;
import app.services.OrderService;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class OrderController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
       app.post("/admin/removeorder", ctx -> adminRemoveOrder(ctx, connectionPool));
       app.get("/userorders", ctx -> getUserOrdersWithDetails(ctx, connectionPool));
       app.post("/admin/updateorder", ctx -> updateOrder(ctx, connectionPool));
    }

    private static void updateOrder(Context ctx, ConnectionPool connectionPool) {
        int orderID = Integer.parseInt(ctx.formParam("orderID"));
        int userID = Integer.parseInt(ctx.formParam("userID"));
        int carportLength = Integer.parseInt(ctx.formParam("carportLength"));
        int carportWidth = Integer.parseInt(ctx.formParam("carportWidth"));

        OrderService orderService = new OrderService(new OrderMapper(), new OrderlineMapper(), new MaterialMapper(), connectionPool);

        try {
            orderService.updateOrder(orderID, userID, carportLength, carportWidth);

            ctx.redirect("/admin/viewUserOrders?userID=" + userID);
        } catch (DatabaseException e) {
            ctx.status(500).result("Error updating order: " + e.getMessage());
        }
    }

    private static void adminRemoveOrder(Context ctx, ConnectionPool connectionPool) {
        int orderID = Integer.parseInt(ctx.formParam("orderID"));
        int userID = Integer.parseInt(ctx.formParam("userID"));

        try {
            OrderMapper.removeOrder(orderID, connectionPool);
            ctx.redirect("/admin/viewUserOrders?userID=" + userID);
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
