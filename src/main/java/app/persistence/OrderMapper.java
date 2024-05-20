package app.persistence;

import app.entities.Orders;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderMapper {

    public static int createOrderID(int userID, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO orders (\"userID\") VALUES ? RETURNING \"orderID\"";

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

    public static List<Orders> getAllOrdersForSearchedUser(int userID, ConnectionPool connectionPool) throws DatabaseException {
        System.out.println("Du er nu i getAllOrdersForSearchedUser");

        List<Orders> orderList = new ArrayList<>();

        String sql = "SELECT * FROM orders o " +
                "JOIN users u ON o.\"userID\" =  u.\"userID\" "+
                "WHERE o.\"userID\" = ?";

        try (Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, userID); // Set the user ID parameter in the query
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                // Retrieve values from the result set
                int orderID = rs.getInt("orderID");
                int totalprice = rs.getInt("totalprice");


                Orders orderDetails = new Orders(orderID, userID, totalprice);
                orderList.add(orderDetails);

            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving orders for user " + userID, e.getMessage());
        }
        return orderList;
    }

    public static void removeOrder(int orderID, ConnectionPool connectionPool) throws DatabaseException {
        System.out.println("Nu er du i removeOrder mapper");

        String deleteOrderlineSql = "DELETE FROM orderline WHERE \"orderID\" = ?";
        String deleteOrderSql = "DELETE FROM orders WHERE \"orderID\" = ?";

        try (Connection connection = connectionPool.getConnection()) {
            // Slet først fra orderline table
            try (PreparedStatement ps = connection.prepareStatement(deleteOrderlineSql)) {
                ps.setInt(1, orderID);
                ps.executeUpdate();
            }

            // Derefter slet fra order table
            try (PreparedStatement ps = connection.prepareStatement(deleteOrderSql)) {
                ps.setInt(1, orderID);
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected == 0) {
                    throw new DatabaseException("Order with ID " + orderID + " not found.");
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error deleting order with ID " + orderID, e.getMessage());
        }
    }
}
