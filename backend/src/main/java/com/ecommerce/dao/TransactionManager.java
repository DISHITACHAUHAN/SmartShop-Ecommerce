package com.ecommerce.dao;

import com.ecommerce.exception.TransactionException;
import com.ecommerce.util.DatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Transaction Manager for manual JDBC transaction management
 * Demonstrates transaction management with commit() and rollback()
 * 
 * This class provides methods to execute operations within a transaction,
 * ensuring data integrity through proper commit/rollback handling
 */
@Component("jdbcTransactionManager")
public class TransactionManager {
    
    private static final Logger logger = LoggerFactory.getLogger(TransactionManager.class);
    
    @Autowired
    private DataSource dataSource;
    
    /**
     * Execute a callback within a transaction
     * Demonstrates manual transaction management with commit/rollback
     * 
     * @param callback transaction callback containing business logic
     * @param <T> return type
     * @return result from callback
     * @throws TransactionException if transaction fails
     */
    public <T> T executeInTransaction(TransactionCallback<T> callback) throws TransactionException {
        Connection conn = null;
        boolean originalAutoCommit = true;
        
        try {
            // Get connection from pool
            conn = dataSource.getConnection();
            originalAutoCommit = conn.getAutoCommit();
            
            // Disable auto-commit for manual transaction management
            conn.setAutoCommit(false);
            logger.debug("Transaction started - auto-commit disabled");
            
            // Execute business logic
            T result = callback.execute(conn);
            
            // Commit transaction if successful
            conn.commit();
            logger.info("Transaction committed successfully");
            
            return result;
            
        } catch (Exception e) {
            // Rollback transaction on any error
            if (conn != null) {
                try {
                    conn.rollback();
                    logger.warn("Transaction rolled back due to error: {}", e.getMessage());
                } catch (SQLException rollbackEx) {
                    logger.error("Error during rollback", rollbackEx);
                    throw new TransactionException("Transaction rollback failed", rollbackEx);
                }
            }
            throw new TransactionException("Transaction failed: " + e.getMessage(), e);
            
        } finally {
            // Restore original auto-commit state and close connection
            if (conn != null) {
                try {
                    conn.setAutoCommit(originalAutoCommit);
                    conn.close();
                    logger.debug("Connection closed and returned to pool");
                } catch (SQLException e) {
                    logger.error("Error closing connection", e);
                }
            }
        }
    }
    
    /**
     * Execute a void callback within a transaction
     * 
     * @param callback transaction callback with no return value
     * @throws TransactionException if transaction fails
     */
    public void executeInTransactionVoid(TransactionCallbackVoid callback) throws TransactionException {
        executeInTransaction(conn -> {
            callback.execute(conn);
            return null;
        });
    }
    
    /**
     * Execute multiple operations in a single transaction
     * Demonstrates batch operations with transaction management
     * 
     * @param callbacks array of callbacks to execute
     * @throws TransactionException if any operation fails
     */
    public void executeMultipleInTransaction(TransactionCallbackVoid... callbacks) 
            throws TransactionException {
        Connection conn = null;
        boolean originalAutoCommit = true;
        
        try {
            conn = dataSource.getConnection();
            originalAutoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            
            logger.debug("Batch transaction started with {} operations", callbacks.length);
            
            // Execute all callbacks
            for (int i = 0; i < callbacks.length; i++) {
                logger.debug("Executing operation {}/{}", i + 1, callbacks.length);
                callbacks[i].execute(conn);
            }
            
            // Commit all operations
            conn.commit();
            logger.info("Batch transaction committed successfully - {} operations", callbacks.length);
            
        } catch (Exception e) {
            // Rollback all operations on any error
            DatabaseUtil.rollback(conn);
            logger.error("Batch transaction failed and rolled back", e);
            throw new TransactionException("Batch transaction failed: " + e.getMessage(), e);
            
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(originalAutoCommit);
                    conn.close();
                } catch (SQLException e) {
                    logger.error("Error closing connection", e);
                }
            }
        }
    }
    
    /**
     * Get a connection with transaction started (auto-commit disabled)
     * Caller is responsible for commit/rollback and closing connection
     * 
     * @return connection with transaction started
     * @throws SQLException if connection cannot be obtained
     */
    public Connection beginTransaction() throws SQLException {
        Connection conn = dataSource.getConnection();
        conn.setAutoCommit(false);
        logger.debug("Transaction begun - connection provided to caller");
        return conn;
    }
    
    /**
     * Commit a transaction on the given connection
     * 
     * @param conn connection to commit
     * @throws SQLException if commit fails
     */
    public void commit(Connection conn) throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.commit();
            logger.info("Transaction committed");
        }
    }
    
    /**
     * Rollback a transaction on the given connection
     * 
     * @param conn connection to rollback
     */
    public void rollback(Connection conn) {
        DatabaseUtil.rollback(conn);
    }
    
    /**
     * Functional interface for transaction callback with return value
     * 
     * @param <T> return type
     */
    @FunctionalInterface
    public interface TransactionCallback<T> {
        /**
         * Execute business logic within transaction
         * 
         * @param connection database connection
         * @return result
         * @throws Exception if operation fails
         */
        T execute(Connection connection) throws Exception;
    }
    
    /**
     * Functional interface for transaction callback without return value
     */
    @FunctionalInterface
    public interface TransactionCallbackVoid {
        /**
         * Execute business logic within transaction
         * 
         * @param connection database connection
         * @throws Exception if operation fails
         */
        void execute(Connection connection) throws Exception;
    }
}
