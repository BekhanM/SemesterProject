package app.entities;

public class Orderline {

    private int orderlineID;
    private int orderID;
    private int materialID;

    public Orderline(int orderlineID, int orderID, int materialID) {
        this.orderlineID = orderlineID;
        this.orderID = orderID;
        this.materialID = materialID;
    }

    public int getOrderlineID() {
        return orderlineID;
    }

    public void setOrderlineID(int orderlineID) {
        this.orderlineID = orderlineID;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getMaterialID() {
        return materialID;
    }

    public void setMaterialID(int materialID) {
        this.materialID = materialID;
    }

    @Override
    public String toString() {
        return "Orderline{" +
                "orderlineID=" + orderlineID +
                ", orderID=" + orderID +
                ", materialID=" + materialID +
                '}';
    }
}
