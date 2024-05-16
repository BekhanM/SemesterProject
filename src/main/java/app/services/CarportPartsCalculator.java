package app.services;

import app.entities.Materials;
import app.entities.Orders;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;

import java.util.ArrayList;
import java.util.List;

public class CarportPartsCalculator {
    private OrderMapper orderMapper;
    private ConnectionPool connectionPool;

    public CarportPartsCalculator(OrderMapper orderMapper, ConnectionPool connectionPool) {
        this.orderMapper = orderMapper;
        this.connectionPool = connectionPool;
    }

    public void calcCarportParts(int userID, int carportLength, int carportWidth) throws DatabaseException {
        int posts = calcNrOfPosts(carportLength, carportWidth);
        int rafters = calcNrOfRafters(carportLength, carportWidth);
        int beams = calcNrOfBeams(carportLength, carportWidth);

        int totalPrice = (posts * 40 + rafters * 35 + beams * 70);
        Orders order = new Orders(0, userID, totalPrice);
        order.setCarportLength(carportLength);
        order.setCarportWidth(carportWidth);
        order.setRoofTiles(false); // indtil videre, videre udregning prøves af bagefter
                                   // skal have styr på det andet lort først

        System.out.println("Carport length: " + carportLength);
        System.out.println("Carport width:  " + carportWidth);

        int orderID = orderMapper.createOrderID(order, connectionPool);

        List<Materials> postMaterials = orderMapper.selectMaterials(300, "97x97 mm. trykimp. Stolpe", connectionPool);
        List<Materials> beamMaterials = orderMapper.selectMaterials(carportLength, "45x195 mm. sprærtræ ubh.", connectionPool);
        List<Materials> rafterMaterials = orderMapper.selectMaterials(carportWidth, "45x195 mm. sprærtræ ubh.", connectionPool);

        // tilføj materialet
        for (Materials material : postMaterials) {
            orderMapper.addOrderline(orderID, material.getMaterialID(), posts, connectionPool);
        }
        for (Materials material : beamMaterials) {
            orderMapper.addOrderline(orderID, material.getMaterialID(), beams, connectionPool);
        }
        for (Materials material : rafterMaterials) {
            orderMapper.addOrderline(orderID, material.getMaterialID(), rafters, connectionPool);
        }

    }

    private static int calcNrOfPosts(int carportLength, int carportWidth) {
        int maxSpaceBetweenPosts = 310;
        int minNrOfPosts = 4; // vigtigt, ellers tag kaput

        //tjek hvor meget plads der tilbage, så vi kan tilføje flere
        //stolper mellem de første to
        int remainingLength = carportLength - maxSpaceBetweenPosts;

        // tilføj flere stolper hvis der mere plads
        while (remainingLength > 0) {
            minNrOfPosts += 2; // +2 stolper (en på hver side)
            remainingLength -= maxSpaceBetweenPosts;
        }

        System.out.println("minNrOfPosts: " + minNrOfPosts);
        return minNrOfPosts;
    }

    private static int calcNrOfRafters(int carportLength, int carportWidth) {
        int maxSpaceBetweenRafters = 55;
        int amountOfRafters = 0;

        while (carportLength >= maxSpaceBetweenRafters) {
            amountOfRafters += 1;
            carportLength -= maxSpaceBetweenRafters;
        }

        System.out.println("Amount of rafters: " + amountOfRafters);
        return amountOfRafters;
    }

    private static int calcNrOfBeams(int carportLength, int carportWidth) {
        // beams, greens, potatoes, tomatoes
        int minNrOfBeams = 2;

        System.out.println("nrOfBeams: " + minNrOfBeams);
        return minNrOfBeams;
    }
}
