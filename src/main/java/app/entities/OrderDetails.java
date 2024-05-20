package app.entities;

public class OrderDetails {
    private int orderID;
    private int totalPrice;
    private int carportWidth;
    private int carportLength;
    private int amountOfMaterial;
    private Materials material;

    public OrderDetails(int orderID, int totalPrice, int carportWidth, int carportLength, int amountOfMaterial, Materials material) {
        this.orderID = orderID;
        this.totalPrice = totalPrice;
        this.carportWidth = carportWidth;
        this.carportLength = carportLength;
        this.amountOfMaterial = amountOfMaterial;
        this.material = material;
    }

    // Getters and Setters

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getCarportWidth() {
        return carportWidth;
    }

    public void setCarportWidth(int carportWidth) {
        this.carportWidth = carportWidth;
    }

    public int getCarportLength() {
        return carportLength;
    }

    public void setCarportLength(int carportLength) {
        this.carportLength = carportLength;
    }

    public int getAmountOfMaterial() {
        return amountOfMaterial;
    }

    public void setAmountOfMaterial(int amountOfMaterial) {
        this.amountOfMaterial = amountOfMaterial;
    }

    public Materials getMaterial() {
        return material;
    }

    public void setMaterial(Materials material) {
        this.material = material;
    }

    @Override
    public String toString() {
        return "OrderDetails{" +
                "orderID=" + orderID +
                ", totalPrice=" + totalPrice +
                ", carportWidth=" + carportWidth +
                ", carportLength=" + carportLength +
                ", amountOfMaterial=" + amountOfMaterial +
                ", material=" + material +
                '}';
    }
}
