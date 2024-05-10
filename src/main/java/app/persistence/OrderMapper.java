package app.persistence;

import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderMapper {

    public static int createOrderID(int userID, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO order (userID) VALUES ? RETURNING orderID";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return rs.getInt("orderID");
            } else {
                throw new DatabaseException("Order ID not found");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Database bummelum problemer kig i orderMapper createOrder", e.getMessage());
        }
    }

    public static void orderOrderline(int userID, int materialID, ConnectionPool connectionPool) {
        try {
            int orderID = OrderMapper.createOrderID(userID, connectionPool);

            OrderlineMapper.createOrderline(orderID, materialID, connectionPool);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }
}
