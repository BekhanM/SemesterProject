package app.persistence;

import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserMapper {

    public static User login(String email, String password, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "select * from users where email=? and password=?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int userID = rs.getInt("userID");
                String role = rs.getString("role");
                String firstname = rs.getString("firstName");
                String lastname = rs.getString("lastName");
                String address = rs.getString("adresse");
                int postnr = rs.getInt("postnr");
                String city = rs.getString("by");
                int tlfnr = rs.getInt("tlfnr");
                return new User(userID, email, password, role, firstname, lastname, address, postnr, city, tlfnr);
            } else {
                throw new DatabaseException("Fejl i login. Prøv igen");
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB fejl", e.getMessage());
        }
    }

    public static void createuser(String email, String password, String firstname, String lastname, String adresse, int postnr, String by, int tlfnr, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "insert into users (email, password, firstname, lastname, adresse, postnr, by, tlfnr) values (?,?,?,?,?,?,?,?)";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setString(1, email);
            ps.setString(2, password);
            ps.setString(3, firstname);
            ps.setString(4, lastname);
            ps.setString(5, adresse);
            ps.setInt(6, postnr);
            ps.setString(7, by);
            ps.setInt(8, tlfnr);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Fejl ved oprettelse af ny bruger");
            }
        } catch (SQLException e) {
            String msg = "Der er sket en fejl. Prøv igen";
            if (e.getMessage().startsWith("ERROR: duplicate key value ")) {
                msg = "Brugernavnet findes allerede. Vælg et andet";
            }
            throw new DatabaseException(msg, e.getMessage());
        }
    }

    public static List<User> getUserDetails(int userID, ConnectionPool connectionPool) throws DatabaseException {

        System.out.println("Nu er du i getUserDetails mapper");

        System.out.println("Executing SQL query to retrieve user details for userID: " + userID);

        List<User> userDetailsList = new ArrayList<>();

        String sql = "SELECT * FROM users WHERE userID = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)

        ) {
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int ID = rs.getInt("userID");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String role = rs.getString("role");
                String firstname = rs.getString("firstname");
                String lastname = rs.getString("lastname");
                String adresse = rs.getString("adresse");
                int postnr = rs.getInt("postnr");
                String by = rs.getString("by");
                int tlfnr = rs.getInt("tlfnr");
                User userDetails = new User(ID, email, password, role, firstname, lastname, adresse, postnr, by, tlfnr);
                userDetailsList.add(userDetails);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Database error while retrieving user1 details", e.getMessage());
        }
        return userDetailsList;
    }

    public static List<User> getAllUsersDetail(String userEmail, ConnectionPool connectionPool) throws DatabaseException {
        System.out.println("Nu er du i getAllUsersDetail mapper");

        List<User> userDetailsList = new ArrayList<>();

        String sql = "SELECT * FROM users WHERE email LIKE ?";


        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, "%" + userEmail + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int ID = rs.getInt("userID");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String role = rs.getString("role");
                String firstname = rs.getString("firstname");
                String lastname = rs.getString("lastname");
                String adresse = rs.getString("adresse");
                int postnr = rs.getInt("postnr");
                String by = rs.getString("by");
                int tlfnr = rs.getInt("tlfnr");
                User userDetails = new User(ID, email, password, role, firstname, lastname, adresse, postnr, by, tlfnr);
                userDetailsList.add(userDetails);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Database error while retrieving user2 details", e.getMessage());
        }

        return userDetailsList;
    }

    public static void removeUser(int userID, ConnectionPool connectionPool) throws DatabaseException {
        System.out.println("Nu er du i removeUser mapper");
        String sql = "DELETE FROM users WHERE \"userID\" = ?";

        try (Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, userID);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("User with ID " + userID + " not found.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting user with ID " + userID, e.getMessage());
        }
    }
}