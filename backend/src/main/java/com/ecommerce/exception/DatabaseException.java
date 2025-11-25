package com.ecommerce.exception;

import java.sql.SQLException;

/**
 * Exception thrown when database operations fail
 * Extends SmartShopException
 */
public class DatabaseException extends SmartShopException {
    
    private String operation;
    private String tableName;
    
    /**
     * Constructor with message
     */
    public DatabaseException(String message) {
        super("DB_ERROR", message);
    }
    
    /**
     * Constructor with message and SQL exception
     */
    public DatabaseException(String message, SQLException cause) {
        super("DB_ERROR", message, cause);
    }
    
    /**
     * Constructor with SQL exception
     */
    public DatabaseException(SQLException cause) {
        super("DB_ERROR", "Database operation failed: " + cause.getMessage(), cause);
    }
    
    /**
     * Constructor with operation and table name
     */
    public DatabaseException(String operation, String tableName, SQLException cause) {
        super("DB_ERROR", 
              "Database " + operation + " failed on table " + tableName + ": " + cause.getMessage(),
              cause);
        this.operation = operation;
        this.tableName = tableName;
    }
    
    public String getOperation() {
        return operation;
    }
    
    public String getTableName() {
        return tableName;
    }
}
