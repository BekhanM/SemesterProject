package app;

import app.config.ThymeleafConfig;
import app.controllers.OrderController;
import app.controllers.UserController;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import app.services.CarportPartsCalculator;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;

public class Main {
    private static final String USER = "postgres";
    private static final String PASSWORD = "gruppebroersej";
    private static final String URL = "jdbc:postgresql://64.226.120.63:5432/Fog";
    private static final String DB = "Fog";

    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

    public static void main(String[] args) throws DatabaseException {
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
        }).start(7070);

        UserController.addRoutes(app, connectionPool);
        OrderController.addRoutes(app, connectionPool);
        app.get("/", ctx -> ctx.render("homepage.html"));

        //////////////////////////////////////////////// TEST /////////////////////////////////////////////////////////
        int carportLength = 600;
        int carportWidth = 600;
        int userID = 1;
        OrderMapper orderMapper = new OrderMapper();
        CarportPartsCalculator calculator = new CarportPartsCalculator(orderMapper, connectionPool);

        calculator.calcCarportParts(userID, carportLength, carportWidth);
        System.out.println("Order saved for carport with length " + carportLength + " and width " + carportWidth);
    }
}
