package app.entities;

public class Orderline {

    private int orderlineID;
    private int orderID;
    private int materialID;

    private int amountOfMaterials;

    public Orderline(int orderlineID, int orderID, int materialID, int amountOfMaterials) {
        this.orderlineID = orderlineID;
        this.orderID = orderID;
        this.materialID = materialID;
        this.amountOfMaterials = amountOfMaterials;
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

    public int getAmountOfMaterials() {
        return amountOfMaterials;
    }

    public void setAmountOfMaterials(int amountOfMaterials) {
        this.amountOfMaterials = amountOfMaterials;
    }

    @Override
    public String toString() {
        return "Orderline{" +
                "orderlineID=" + orderlineID +
                ", orderID=" + orderID +
                ", materialID=" + materialID +
                ", amountOfMaterials=" + amountOfMaterials +
                '}';
    }
}
