package app.entities;

public class Orders {
    private int orderID;
    private int userID;
    private int totalPrice;
    private int carportWidth;
    private int carportLength;
    private boolean roofTiles;

    public Orders(int orderID, int userID, int totalPrice) {
        this.orderID = orderID;
        this.userID = userID;
        this.totalPrice = totalPrice;
    }

    public void setCarportLength(int carportLength) {
        this.carportLength = carportLength;
    }

    public void setCarportWidth(int carportWidth) {
        this.carportWidth = carportWidth;
    }

    public void setRoofTiles(boolean roofTiles) {
        this.roofTiles = roofTiles;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getCarportLength() {
        return carportLength;
    }

    public int getCarportWidth() {
        return carportWidth;
    }

    public boolean isRoofTiles() {
        return roofTiles;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "orderID=" + orderID +
                ", userID=" + userID +
                ", totalPrice=" + totalPrice +
                ", carportWidth=" + carportWidth +
                ", carportLength=" + carportLength +
                ", roofTiles=" + roofTiles +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Orders orders)) return false;

        if (getOrderID() != orders.getOrderID()) return false;
        if (getUserID() != orders.getUserID()) return false;
        return getTotalPrice() == orders.getTotalPrice();
    }

    @Override
    public int hashCode() {
        int result = getOrderID();
        result = 31 * result + getUserID();
        result = 31 * result + getTotalPrice();
        return result;
    }
}
