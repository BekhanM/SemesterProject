package app.entities;

public class Orderline {
    private int orderlineID;
    private int orderID;
    private int materialID;
    private int amountOfMaterial;

    public Orderline(int orderlineID, int orderID, int materialID, int amountOfMaterial) {
        this.orderlineID = orderlineID;
        this.orderID = orderID;
        this.materialID = materialID;
        this.amountOfMaterial = amountOfMaterial;
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

    public int getAmountOfMaterial() {
        return amountOfMaterial;
    }

    public void setAmountOfMaterial(int amountOfMaterial) {
        this.amountOfMaterial = amountOfMaterial;
    }

    @Override
    public String toString() {
        return "Orderline{" +
                "amountOfMaterial=" + amountOfMaterial +
                ", orderlineID=" + orderlineID +
                ", orderID=" + orderID +
                ", materialID=" + materialID +
                '}';
    }
}
