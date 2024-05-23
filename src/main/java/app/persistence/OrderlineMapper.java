package app.persistence;

import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderlineMapper {

    public static void addOrderline(int orderID, int materialID, int amount, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO orderline (\"orderID\", \"materialID\", \"amountofmaterial\") VALUES (?, ?, ?)";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setInt(1, orderID);
            ps.setInt(2, materialID);
            ps.setInt(3, amount);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Database error in OrderMapper addOrderline", e.getMessage());
        }
    }

    public static void deleteOrderlines(int orderID, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "DELETE FROM orderline WHERE \"orderID\" = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, orderID);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting order lines for order ID " + orderID, e.getMessage());
        }
    }

}
