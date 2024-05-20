package app.entities;

public class Materials {
    private int materialID;
    private String name;
    private int pricePrMeter;
    private int length;

    public Materials(int materialID, String name, int pricePrMeter, int length) {
        this.materialID = materialID;
        this.name = name;
        this.pricePrMeter = pricePrMeter;
        this.length = length;
    }

    public int getMaterialID() {
        return materialID;
    }

    public void setMaterialID(int materialID) {
        this.materialID = materialID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "Materials{" +
                "materialID=" + materialID +
                ", name='" + name + '\'' +
                ", pricePrMeter=" + pricePrMeter +
                ", length=" + length +
                '}';
    }
}
