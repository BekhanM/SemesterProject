package app.services;

import app.exceptions.DatabaseException;

public class CarportPartsCalculator {
    private OrderService orderService;

    public CarportPartsCalculator(OrderService orderService) {
        this.orderService = orderService;
    }

    public void calcCarportParts(int userID, int carportLength, int carportWidth) throws DatabaseException {
        orderService.processOrder(userID, carportLength, carportWidth);
    }
}
