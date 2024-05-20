package app.persistence;

import app.entities.Orders;
import app.entities.User;
import app.exceptions.DatabaseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Integrationstest for OrderMapper

class OrderMapperTest {
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance();


    @BeforeAll
    static void setupClass()
    {
        try (Connection connection = connectionPool.getConnection())
        {
            try (Statement stmt = connection.createStatement())
            {
                // The test schema is already created, so we only need to delete/create test tables
                stmt.execute("DROP TABLE IF EXISTS test.users");
                stmt.execute("DROP TABLE IF EXISTS test.orders");
                stmt.execute("DROP TABLE IF EXISTS test.orderline");
                stmt.execute("DROP SEQUENCE IF EXISTS test.\"users_userID_seq\" CASCADE;");
                stmt.execute("DROP SEQUENCE IF EXISTS test.\"orders_orderID_seq\" CASCADE;");
                stmt.execute("DROP SEQUENCE IF EXISTS test.\"orderline_orderlineID_seq\" CASCADE");
                // Create tables as copy of original public schema structure
                stmt.execute("CREATE TABLE test.users AS (SELECT * from public.users) WITH NO DATA");
                stmt.execute("CREATE TABLE test.orders AS (SELECT * from public.orders) WITH NO DATA");
                stmt.execute("CREATE TABLE test.orderline AS (SELECT * from public.orderline) WITH NO DATA");
                // Create sequences for auto generating id's for users and orders
                stmt.execute("CREATE SEQUENCE test.\"users_userID_seq\"");
                stmt.execute("ALTER TABLE test.users ALTER COLUMN \"userID\" SET DEFAULT nextval('test.\"users_userID_seq\"')");
                stmt.execute("CREATE SEQUENCE test.\"orders_orderID_seq\"");
                stmt.execute("ALTER TABLE test.orders ALTER COLUMN \"orderID\" SET DEFAULT nextval('test.\"orders_orderID_seq\"')");
                stmt.execute("CREATE SEQUENCE test.\"orderline_orderlineID_seq\"");
                stmt.execute("ALTER TABLE test.orderline ALTER COLUMN \"orderlineID\" SET DEFAULT nextval('test.\"orderline_orderlineID_seq\"')");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            fail("Database connection failed");
        }
    }

    @BeforeEach
    void setUp() {
        try (Connection connection = connectionPool.getConnection())
        {
            try (Statement stmt = connection.createStatement())
            {
                // Remove all rows from all tables
                stmt.execute("DELETE FROM test.orders");
                stmt.execute("DELETE FROM test.users");
                stmt.execute("DELETE FROM test.orderline");

                stmt.execute("INSERT INTO test.users (\"userID\", email, password, role, firstname, lastname, adresse, postnr, by, tlfnr) " +
                        "VALUES  (1, 'jon', '1234', 'customer','Jon', 'Dahl-Thomasson','Spillervej 12', '2000', 'Frederiksberg', '20202020'), (2, 'benny', '1234', 'admin', 'Benny', 'Blanco', 'Godthåbsvej 12', '3000', 'København K', '45454545')");

                stmt.execute("INSERT INTO test.orders (\"orderID\", \"userID\", totalprice, carportwidth, carportlength, rooftiles) " +
                        "VALUES (1, 1, 20000, 600, 780, false), (2, 2, 40000, 540, 700, false), (3, 1, 15000, 480, 600, true)");

                stmt.execute("INSERT INTO test.orderline (\"orderlineID\", \"orderID\", amountofmaterial)" +
                        "VALUES (1, 1, 300), (2, 3, 500), (3, 2, 400)");
                // Set sequence to continue from the largest member_id
                stmt.execute("SELECT setval('test.\"orders_orderID_seq\"', COALESCE((SELECT MAX(\"orderID\") + 1 FROM test.orders), 1), false)");
                stmt.execute("SELECT setval('test.\"users_userID_seq\"', COALESCE((SELECT MAX(\"userID\") + 1 FROM test.users), 1), false)");
                stmt.execute("SELECT setval('test.\"orderline_orderlineID_seq\"', COALESCE((SELECT MAX(\"orderlineID\") + 1 FROM test.orderline), 1), false)");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            fail("Database connection failed");
        }
    }

    /*
    @Test
    void getOrderById()
    {
        try
        {
            User user = new User(1, "jon", "1234", "customer");
            Orders expected = new Order(1, 1, 600, 780, 20000, user);
            Orders dbOrder = OrderMapper.getOrderById(1, connectionPool);
            assertEquals(expected, dbOrder);
        }
        catch (DatabaseException e)
        {
            fail("Database fejl: " + e.getMessage());
        }
    } */

    /*
    @Test
    void createOrderID() {
        try {
            int userID = 1; // Assume userID 1 exists in the test database
            int newOrderID = OrderMapper.createOrderID(userID, connectionPool);

            // Verify that the new order ID is a positive integer
            assertTrue(newOrderID > 0, "New order ID should be a positive integer.");

            // Verify that the new order is present in the database
            try (Connection connection = connectionPool.getConnection();
                 PreparedStatement ps = connection.prepareStatement("SELECT * FROM orders WHERE \"orderID\" = ?")) {
                ps.setInt(1, newOrderID);
                ResultSet rs = ps.executeQuery();
                assertTrue(rs.next(), "New order should be present in the database.");
                assertEquals(userID, rs.getInt("userID"), "User ID of the new order should match the provided userID.");
            } catch (SQLException e) {
                fail("Database query failed: " + e.getMessage());
            }

        } catch (DatabaseException e) {
            fail("Database error: " + e.getMessage());
        }
    } */

    @Test
    void getAllOrdersForSearchedUser() {
        try
        {
            List<Orders> orders = OrderMapper.getAllOrdersForSearchedUser(1,connectionPool);
            assertEquals(2, orders.size());
        }
        catch (DatabaseException e)
        {
            fail("Database fejl: " + e.getMessage());
        }
    }


    /*
    @Test
    void removeOrder() {
        try
        {
            OrderMapper.removeOrder(1, connectionPool);
            List<Orders> remainingOrders = OrderMapper.
            assertEquals(0, remainingOrders.size());
        }
        catch (DatabaseException e) {
            fail("Database fejl: " + e.getMessage());
        }
    } */
}