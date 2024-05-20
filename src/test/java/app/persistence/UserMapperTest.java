package app.persistence;

import app.entities.Orders;
import app.entities.User;
import app.exceptions.DatabaseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Integrationstest for UserMapper
class UserMapperTest {
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
                stmt.execute("DROP SEQUENCE IF EXISTS test.\"users_userID_seq\" CASCADE;");
                // Create tables as copy of original public schema structure
                stmt.execute("CREATE TABLE test.users AS (SELECT * from public.users) WITH NO DATA");;
                // Create sequences for auto generating id's for users and orders
                stmt.execute("CREATE SEQUENCE test.\"users_userID_seq\"");
                stmt.execute("ALTER TABLE test.users ALTER COLUMN \"userID\" SET DEFAULT nextval('test.\"users_userID_seq\"')");
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
                stmt.execute("DELETE FROM test.users");

                stmt.execute("INSERT INTO test.users (\"userID\", email, password, role, firstname, lastname, adresse, postnr, by, tlfnr) " +
                        "VALUES  (1, 'jon@gmail.com', '1234', 'customer','Jon', 'Dahl-Thomasson','Spillervej 12', '2000', 'Frederiksberg', '20202020'), (2, 'benny@gmail.com', '1234', 'admin', 'Benny', 'Blanco', 'Godthåbsvej 12', '3000', 'København K', '45454545')");
                // Set sequence to continue from the largest member_id
                stmt.execute("SELECT setval('test.\"users_userID_seq\"', COALESCE((SELECT MAX(\"userID\") + 1 FROM test.users), 1), false)");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            fail("Database connection failed");
        }
    }

    @Test
    void login() {
        // Test valid login
        try {
            User user = UserMapper.login("jon@gmail.com", "1234", connectionPool);
            assertNotNull(user);
            assertEquals(1, user.getUserID());
            assertEquals("jon@gmail.com", user.getEmail());
            assertEquals("1234", user.getPassword());
            assertEquals("customer", user.getRole());
            assertEquals("Jon", user.getFirstName());
            assertEquals("Dahl-Thomasson", user.getLastName());
            assertEquals("Spillervej 12", user.getAdresse());
            assertEquals(2000, user.getPostnr());
            assertEquals("Frederiksberg", user.getBy());
            assertEquals(20202020, user.getTlfnr());
        } catch (DatabaseException e) {
            fail("DatabaseException thrown: " + e.getMessage());
        }
    }

    @Test
    void createuser() {
        // Prepare test data
        String email = "testbruger@example.com";
        String password = "testpass";
        String firstname = "Test";
        String lastname = "Bruger";
        String adresse = "Test Adresse";
        int postnr = 1234;
        String by = "Test By";
        int tlfnr = 55555555;

        // Call createuser method
        try {
            UserMapper.createuser(email, password, firstname, lastname, adresse, postnr, by, tlfnr, connectionPool);

            // Verify the user was added
            try (Connection connection = connectionPool.getConnection()) {
                String sql = "SELECT * FROM test.users WHERE email = ?";
                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                    ps.setString(1, email);
                    try (ResultSet rs = ps.executeQuery()) {
                        assertTrue(rs.next(), "User should be found in the database");
                        assertEquals(email, rs.getString("email"));
                        assertEquals(password, rs.getString("password"));
                        assertEquals(firstname, rs.getString("firstname"));
                        assertEquals(lastname, rs.getString("lastname"));
                        assertEquals(adresse, rs.getString("adresse"));
                        assertEquals(postnr, rs.getInt("postnr"));
                        assertEquals(by, rs.getString("by"));
                        assertEquals(tlfnr, rs.getInt("tlfnr"));
                    }
                }
            }
        } catch (DatabaseException | SQLException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    void getUserDetails() {
        try
        {
            List<User> users = UserMapper.getUserDetails(1,connectionPool);
            assertEquals(1, users.size());
            System.out.println(users);
        }
        catch (DatabaseException e)
        {
            fail("Database fejl: " + e.getMessage());
        }
    }

    @Test
    void getAllUsersDetail() {
    }


    /*
    @Test
    void removeUser() {
        try
        {
            UserMapper.removeUser(1, connectionPool);
            List<User> remainingUsers = UserMapper.getAllUsersDetail(, connectionPool);
                    assertEquals(0, remainingOrders.size());
        }
        catch (DatabaseException e) {
            fail("Database fejl: " + e.getMessage());
        }
    } */
}