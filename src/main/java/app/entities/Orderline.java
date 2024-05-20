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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Orderline orderline)) return false;

        if (getOrderlineID() != orderline.getOrderlineID()) return false;
        if (getOrderID() != orderline.getOrderID()) return false;
        if (getMaterialID() != orderline.getMaterialID()) return false;
        return getAmountOfMaterials() == orderline.getAmountOfMaterials();
    }

    @Override
    public int hashCode() {
        int result = getOrderlineID();
        result = 31 * result + getOrderID();
        result = 31 * result + getMaterialID();
        result = 31 * result + getAmountOfMaterials();
        return result;
    }
}
