package com.ecommerce.dao;

import com.ecommerce.util.DatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Abstract base DAO implementation
 * Demonstrates OOP principles: Abstraction, Generics, Encapsulation
 * 
 * Provides common JDBC operations and connection management
 * Subclasses implement entity-specific operations
 * 
 * @param <T> Entity type
 */
public abstract class AbstractDAO<T> implements BaseDAO<T> {
    
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    protected DataSource dataSource;
    
    /**
     * Get a database connection from the connection pool
     * 
     * @return database connection
     * @throws SQLException if connection cannot be obtained
     */
    protected Connection getConnection() throws SQLException {
        Connection conn = dataSource.getConnection();
        logger.debug("Connection obtained from pool");
        return conn;
    }
    
    /**
     * Close database resources safely
     * 
     * @param conn Connection to close
     * @param stmt PreparedStatement to close
     * @param rs ResultSet to close
     */
    protected void closeResources(Connection conn, PreparedStatement stmt, ResultSet rs) {
        DatabaseUtil.closeResources(conn, stmt, rs);
    }
    
    /**
     * Execute a query and return a single result
     * 
     * @param sql SQL query
     * @param mapper ResultSet mapper function
     * @param params query parameters
     * @return entity if found, null otherwise
     * @throws SQLException if query fails
     */
    protected T executeQuerySingle(String sql, ResultSetMapper<T> mapper, Object... params) 
            throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            
            // Set parameters
            setParameters(stmt, params);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapper.map(rs);
            }
            return null;
            
        } finally {
            closeResources(conn, stmt, rs);
        }
    }
    
    /**
     * Execute a query and return multiple results
     * 
     * @param sql SQL query
     * @param mapper ResultSet mapper function
     * @param params query parameters
     * @return list of entities
     * @throws SQLException if query fails
     */
    protected java.util.List<T> executeQueryList(String sql, ResultSetMapper<T> mapper, Object... params) 
            throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        java.util.List<T> results = new java.util.ArrayList<>();
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            
            // Set parameters
            setParameters(stmt, params);
            
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                results.add(mapper.map(rs));
            }
            
            return results;
            
        } finally {
            closeResources(conn, stmt, rs);
        }
    }
    
    /**
     * Execute an update query (INSERT, UPDATE, DELETE)
     * 
     * @param sql SQL query
     * @param params query parameters
     * @return number of affected rows
     * @throws SQLException if query fails
     */
    protected int executeUpdate(String sql, Object... params) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            
            // Set parameters
            setParameters(stmt, params);
            
            int affectedRows = stmt.executeUpdate();
            logger.debug("Affected rows: {}", affectedRows);
            
            return affectedRows;
            
        } finally {
            closeResources(conn, stmt, null);
        }
    }
    
    /**
     * Execute an update query and return generated key
     * 
     * @param sql SQL query
     * @param params query parameters
     * @return generated key (ID)
     * @throws SQLException if query fails
     */
    protected Long executeUpdateWithGeneratedKey(String sql, Object... params) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            
            // Set parameters
            setParameters(stmt, params);
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating entity failed, no rows affected.");
            }
            
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                Long generatedId = rs.getLong(1);
                logger.debug("Generated ID: {}", generatedId);
                return generatedId;
            } else {
                throw new SQLException("Creating entity failed, no ID obtained.");
            }
            
        } finally {
            closeResources(conn, stmt, rs);
        }
    }
    
    /**
     * Set parameters on PreparedStatement
     * Uses PreparedStatement to prevent SQL injection
     * 
     * @param stmt PreparedStatement
     * @param params parameters to set
     * @throws SQLException if parameter setting fails
     */
    protected void setParameters(PreparedStatement stmt, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }
    }
    
    /**
     * Count entities using a SQL query
     * 
     * @param sql count query
     * @param params query parameters
     * @return count
     * @throws SQLException if query fails
     */
    protected long executeCount(String sql, Object... params) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            
            setParameters(stmt, params);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            return 0;
            
        } finally {
            closeResources(conn, stmt, rs);
        }
    }
    
    /**
     * Functional interface for mapping ResultSet to entity
     * 
     * @param <T> Entity type
     */
    @FunctionalInterface
    protected interface ResultSetMapper<T> {
        T map(ResultSet rs) throws SQLException;
    }
    
    /**
     * Default implementation of count
     * Subclasses should override with specific table name
     */
    @Override
    public long count() throws SQLException {
        throw new UnsupportedOperationException("Count method must be implemented by subclass");
    }
    
    /**
     * Default implementation of existsById
     */
    @Override
    public boolean existsById(Long id) throws SQLException {
        return findById(id) != null;
    }
}
