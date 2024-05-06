package app.entities;

public class User {
    private int userID;
    private String email;
    private String password;
    private String role;
    private String firstName;
    private String lastName;
    private String address;
    private int postnr;
    private String city;

    public User(int userID, String email, String password, String role, String firstName, String lastName, String address, int postnr, String city) {
        this.userID = userID;
        this.email = email;
        this.password = password;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.postnr = postnr;
        this.city = city;
    }
    public int getUserID() {
        return userID;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public String getRole() {
        return role;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getAddress() {
        return address;
    }
    public int getPostnr() {
        return postnr;
    }
    public String getCity() {
        return city;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPostnr(int postnr) {
        this.postnr = postnr;
    }

    public void setCity(String city) {
        this.city = city;
    }
    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", postalCode=" + postnr +
                ", city='" + city + '\'' +
                '}';
    }
}
