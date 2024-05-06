package app.entities;

public class Postnr {

    private int postNr;
    private String city;

    public Postnr(int postNr, String city) {
        this.postNr = postNr;
        this.city = city;
    }

    public int getPostNr() {
        return postNr;
    }

    public void setPostNr(int postNr) {
        this.postNr = postNr;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    @Override
    public String toString() {
        return "Postnr{" +
                "postNr=" + postNr +
                ", city='" + city + '\'' +
                '}';
    }
}
