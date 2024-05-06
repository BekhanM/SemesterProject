package app.persistence;

import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    public static void createuser(String userName, String password, String firstname, String lastname, String adresse, int postnr, String by, int tlfnr, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "insert into users (email, password, firstname, lastname, adresse, postnr, by, tlfnr) values (?,?,?,?,?,?,?,?)";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setString(1, userName);
            ps.setString(2, password);
            ps.setString(4, firstname);
            ps.setString(5, lastname);
            ps.setString(6, adresse);
            ps.setInt(7, postnr);
            ps.setString(8, by);
            ps.setInt(9, tlfnr);
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
}