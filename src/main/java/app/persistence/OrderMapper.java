package app.persistence;

import app.entities.Materials;
import app.entities.OrderDetails;
import app.entities.Orders;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderMapper {

    public static void updateOrder(Orders order, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "UPDATE orders SET \"totalprice\" = ?, \"carportlength\" = ?, \"carportwidth\" = ?, \"rooftiles\" = ? WHERE \"orderID\" = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, order.getTotalPrice());
            ps.setInt(2, order.getCarportLength());
            ps.setInt(3, order.getCarportWidth());
            ps.setBoolean(4, order.isRoofTiles());
            ps.setInt(5, order.getOrderID());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error updating order with ID " + order.getOrderID(), e.getMessage());
        }
    }


    public static int createOrderID(Orders order, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO orders (\"userID\", \"totalprice\", \"carportwidth\", \"carportlength\", \"rooftiles\") VALUES (?, ?, ?, ?, ?) RETURNING \"orderID\"";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setInt(1, order.getUserID());
            ps.setInt(2, order.getTotalPrice());
            ps.setInt(3, order.getCarportWidth());
            ps.setInt(4, order.getCarportLength());
            ps.setBoolean(5, order.isRoofTiles());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("orderID");
            } else {
                throw new DatabaseException("Order ID not found");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Database error in OrderMapper createOrder", e.getMessage());
        }
    }

    public static List<OrderDetails> getUserOrdersWithDetails(int userID, ConnectionPool connectionPool) throws DatabaseException {
        List<OrderDetails> orderDetailsList = new ArrayList<>();

        String sql = "SELECT o.\"orderID\", o.\"totalprice\", o.\"carportwidth\", o.\"carportlength\", " +
                "ol.\"amountofmaterial\", m.\"name\", m.\"priceprmeter\", m.\"length\" " +
                "FROM orders o " +
                "JOIN orderline ol ON o.\"orderID\" = ol.\"orderID\" " +
                "JOIN materials m ON ol.\"materialID\" = m.\"materialID\" " +
                "WHERE o.\"userID\" = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                int orderID = rs.getInt("orderID");
                int totalPrice = rs.getInt("totalprice");
                int carportWidth = rs.getInt("carportwidth");
                int carportLength = rs.getInt("carportlength");
                int amountOfMaterial = rs.getInt("amountofmaterial");
                String materialName = rs.getString("name");
                int pricePerMeter = rs.getInt("priceprmeter");
                int materialLength = rs.getInt("length");


                Materials material = new Materials(0, materialName, pricePerMeter, materialLength);


                OrderDetails orderDetails = new OrderDetails(orderID, totalPrice, carportWidth, carportLength, amountOfMaterial, material);

                orderDetailsList.add(orderDetails);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving order details for user " + userID, e.getMessage());
        }
        return orderDetailsList;
    }


    public static List<Orders> getAllOrdersForSearchedUser(int userID, ConnectionPool connectionPool) throws DatabaseException {
        System.out.println("Du er nu i getAllOrdersForSearchedUser");

        List<Orders> orderList = new ArrayList<>();

        String sql = "SELECT * FROM orders o " +
                "JOIN users u ON o.\"userID\" =  u.\"userID\" " +
                "WHERE o.\"userID\" = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

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
