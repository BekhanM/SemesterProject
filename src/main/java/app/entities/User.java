package app.entities;

public class User {
    private int userID;
    private String email;
    private String password;
    private String role;
    private String firstName;
    private String lastName;
    private String adresse;
    private int postnr;
    private String by;
    private int tlfnr;

    public User(int userID, String email, String password, String role, String firstName, String lastName, String adresse, int postnr, String by, int tlfnr) {
        this.userID = userID;
        this.email = email;
        this.password = password;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.adresse = adresse;
        this.postnr = postnr;
        this.by = by;
        this.tlfnr = tlfnr;
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
    public String getAdresse() {
        return adresse;
    }
    public int getPostnr() {
        return postnr;
    }
    public String getBy() {
        return by;
    }
    public int getTlfnr() {
        return tlfnr;
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

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setPostnr(int postnr) {
        this.postnr = postnr;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public void setTlfnr(int tlfnr) {
        this.tlfnr = tlfnr;
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
                ", address='" + adresse + '\'' +
                ", postnr=" + postnr +
                ", city='" + by + '\'' +
                ", tlfnr=" + tlfnr +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;

        if (getUserID() != user.getUserID()) return false;
        if (getPostnr() != user.getPostnr()) return false;
        if (getTlfnr() != user.getTlfnr()) return false;
        if (!getEmail().equals(user.getEmail())) return false;
        if (!getPassword().equals(user.getPassword())) return false;
        if (!getRole().equals(user.getRole())) return false;
        if (!getFirstName().equals(user.getFirstName())) return false;
        if (!getLastName().equals(user.getLastName())) return false;
        if (!getAdresse().equals(user.getAdresse())) return false;
        return getBy().equals(user.getBy());
    }

    @Override
    public int hashCode() {
        int result = getUserID();
        result = 31 * result + getEmail().hashCode();
        result = 31 * result + getPassword().hashCode();
        result = 31 * result + getRole().hashCode();
        result = 31 * result + getFirstName().hashCode();
        result = 31 * result + getLastName().hashCode();
        result = 31 * result + getAdresse().hashCode();
        result = 31 * result + getPostnr();
        result = 31 * result + getBy().hashCode();
        result = 31 * result + getTlfnr();
        return result;
    }
}
