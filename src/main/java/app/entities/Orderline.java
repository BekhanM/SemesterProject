package app.entities;

public class Orderline {

    private int orderlineID;
    private int orderID;
    private int materialsID;

    public Orderline(int orderlineID, int orderID, int materialsID) {
        this.orderlineID = orderlineID;
        this.orderID = orderID;
        this.materialsID = materialsID;
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

    public int getMaterialsID() {
        return materialsID;
    }

    public void setMaterialsID(int materialsID) {
        this.materialsID = materialsID;
    }

    @Override
    public String toString() {
        return "Orderline{" +
                "orderlineID=" + orderlineID +
                ", orderID=" + orderID +
                ", materialsID=" + materialsID +
                '}';
    }
}
