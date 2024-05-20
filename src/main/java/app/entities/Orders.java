package app.entities;

public class Orders {

    private int orderID;
    private int userID;
    private int totalPrice;

    public Orders(int orderID, int userID, int totalPrice) {
        this.orderID = orderID;
        this.userID = userID;
        this.totalPrice = totalPrice;
    }
    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
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
