package app.entities;

public class Materials {

private String materialID;
private String name;
private int quantity;
private int pricePrMeter;
private int length;
private int width;
private int heigth;

    public Materials(String materialID, String name, int quantity, int pricePrMeter, int length, int width, int heigth) {
        this.materialID = materialID;
        this.name = name;
        this.quantity = quantity;
        this.pricePrMeter = pricePrMeter;
        this.length = length;
        this.width = width;
        this.heigth = heigth;
    }

    public String getMaterialID() {
        return materialID;
    }

    public void setMaterialID(String materialID) {
        this.materialID = materialID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPricePrMeter() {
        return pricePrMeter;
    }

    public void setPricePrMeter(int pricePrMeter) {
        this.pricePrMeter = pricePrMeter;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeigth() {
        return heigth;
    }

    public void setHeigth(int heigth) {
        this.heigth = heigth;
    }

    @Override
    public String toString() {
        return "Materials{" +
                "materialID='" + materialID + '\'' +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", pricePrMeter=" + pricePrMeter +
                ", length=" + length +
                ", width=" + width +
                ", heigth=" + heigth +
                '}';
    }
}
