package com.ecommerce.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for database configuration and connectivity
 */
@SpringBootTest
class DatabaseConfigTest {

    @Autowired
    private DataSource dataSource;

    /**
     * Test that DataSource bean is properly configured
     */
    @Test
    void testDataSourceConfiguration() {
        assertNotNull(dataSource, "DataSource should not be null");
    }

    /**
     * Test database connectivity
     */
    @Test
    void testDatabaseConnection() {
        try (Connection connection = dataSource.getConnection()) {
            assertNotNull(connection, "Connection should not be null");
            assertFalse(connection.isClosed(), "Connection should be open");
            
            // Test simple query
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT 1")) {
                assertTrue(rs.next(), "Query should return result");
                assertEquals(1, rs.getInt(1), "Query result should be 1");
            }
        } catch (Exception e) {
            fail("Database connection test failed: " + e.getMessage());
        }
    }

    /**
     * Test that tables are created
     */
    @Test
    void testTablesExist() {
        try (Connection connection = dataSource.getConnection()) {
            String[] tables = {"users", "customers", "admins", "products", 
                             "cart_items", "orders", "order_items", "wishlist"};
            
            for (String table : tables) {
                try (Statement stmt = connection.createStatement();
                     ResultSet rs = stmt.executeQuery(
                         "SELECT COUNT(*) FROM information_schema.tables " +
                         "WHERE table_schema = DATABASE() AND table_name = '" + table + "'")) {
                    assertTrue(rs.next(), "Query should return result");
                    assertEquals(1, rs.getInt(1), 
                        "Table '" + table + "' should exist");
                }
            }
        } catch (Exception e) {
            fail("Table existence test failed: " + e.getMessage());
        }
    }
}
