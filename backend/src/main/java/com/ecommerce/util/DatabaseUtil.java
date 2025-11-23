package com.ecommerce.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Utility class for common database operations
 * Provides helper methods for resource cleanup and error handling
 */
public class DatabaseUtil {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseUtil.class);

    /**
     * Closes database resources safely
     * 
     * @param conn Connection to close
     * @param stmt PreparedStatement to close
     * @param rs ResultSet to close
     */
    public static void closeResources(Connection conn, PreparedStatement stmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
                logger.debug("ResultSet closed successfully");
            } catch (SQLException e) {
                logger.error("Error closing ResultSet", e);
            }
        }
        
        if (stmt != null) {
            try {
                stmt.close();
                logger.debug("PreparedStatement closed successfully");
            } catch (SQLException e) {
                logger.error("Error closing PreparedStatement", e);
            }
        }
        
        if (conn != null) {
            try {
                conn.close();
                logger.debug("Connection closed successfully");
            } catch (SQLException e) {
                logger.error("Error closing Connection", e);
            }
        }
    }

    /**
     * Closes connection safely
     * 
     * @param conn Connection to close
     */
    public static void closeConnection(Connection conn) {
        closeResources(conn, null, null);
    }

    /**
     * Closes statement safely
     * 
     * @param stmt PreparedStatement to close
     */
    public static void closeStatement(PreparedStatement stmt) {
        closeResources(null, stmt, null);
    }

    /**
     * Closes ResultSet safely
     * 
     * @param rs ResultSet to close
     */
    public static void closeResultSet(ResultSet rs) {
        closeResources(null, null, rs);
    }

    /**
     * Rolls back transaction safely
     * 
     * @param conn Connection to rollback
     */
    public static void rollback(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
                logger.info("Transaction rolled back successfully");
            } catch (SQLException e) {
                logger.error("Error rolling back transaction", e);
            }
        }
    }

    /**
     * Commits transaction safely
     * 
     * @param conn Connection to commit
     * @throws SQLException if commit fails
     */
    public static void commit(Connection conn) throws SQLException {
        if (conn != null) {
            conn.commit();
            logger.info("Transaction committed successfully");
        }
    }

    /**
     * Sets auto-commit mode safely
     * 
     * @param conn Connection
     * @param autoCommit auto-commit mode
     */
    public static void setAutoCommit(Connection conn, boolean autoCommit) {
        if (conn != null) {
            try {
                conn.setAutoCommit(autoCommit);
                logger.debug("Auto-commit set to: {}", autoCommit);
            } catch (SQLException e) {
                logger.error("Error setting auto-commit", e);
            }
        }
    }
}
