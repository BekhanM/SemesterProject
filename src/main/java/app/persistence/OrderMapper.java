package app.persistence;

import app.entities.Materials;
import app.entities.Orders;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderMapper {

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

    public static List<Materials> getMaterials(ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT * FROM materials";
        List<Materials> materials = new ArrayList<>();

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
        ) {
            while (rs.next()) {
                int materialID = rs.getInt("materialID");
                String name = rs.getString("name");
                int pricePrMeter = rs.getInt("priceprmeter");
                int length = rs.getInt("length");

                materials.add(new Materials(materialID, name, pricePrMeter, length));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching materials", e.getMessage());
        }

        return materials;
    }

    public static List<Materials> selectMaterials(int carportLength, String name, ConnectionPool connectionPool) throws DatabaseException {
        List<Materials> materials = getMaterials(connectionPool);
        List<Materials> selectedMaterials = new ArrayList<>();

        for (Materials material : materials) {
            if (material.getName().equals(name) && material.getLength() >= carportLength) {
                selectedMaterials.add(material);
                break;
            }
        }

        if (selectedMaterials.isEmpty()) {
            throw new DatabaseException("No suitable material length found");
        }

        return selectedMaterials;
    }

    public static void updateMaterialQuantities(List<Materials> selectedMaterials, ConnectionPool connectionPool) throws DatabaseException {
        // Update material quantities logic here if necessary
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
