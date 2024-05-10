package app.persistence;

import app.exceptions.DatabaseException;
import com.fasterxml.jackson.core.JsonParseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderlineMapper {
    private static int orderID;
    private static int materialID;

    public static void createOrderline(int orderID, int materialID, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO orderline (\"orderID\", \"materialID\") VALUES (?,?)";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setInt(1, orderID);
            ps.setInt(2, materialID);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Fejl ved createOrderline");
            }
        } catch (SQLException e) {
            String msg = "Fejl ved createOrderline";
            throw new DatabaseException(msg, e.getMessage());
        }

    }
}
