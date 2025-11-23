package com.ecommerce.dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Generic Base DAO interface
 * Demonstrates OOP principles: Abstraction and Generics
 * 
 * This interface defines common CRUD operations for all DAOs
 * Using Java Generics for type safety
 * 
 * @param <T> Entity type
 */
public interface BaseDAO<T> {
    
    /**
     * Save a new entity to the database
     * 
     * @param entity entity to save
     * @return saved entity with generated ID
     * @throws SQLException if database operation fails
     */
    T save(T entity) throws SQLException;
    
    /**
     * Find an entity by its ID
     * 
     * @param id entity ID
     * @return entity if found, null otherwise
     * @throws SQLException if database operation fails
     */
    T findById(Long id) throws SQLException;
    
    /**
     * Find all entities
     * 
     * @return list of all entities
     * @throws SQLException if database operation fails
     */
    List<T> findAll() throws SQLException;
    
    /**
     * Update an existing entity
     * 
     * @param entity entity to update
     * @return updated entity
     * @throws SQLException if database operation fails
     */
    T update(T entity) throws SQLException;
    
    /**
     * Delete an entity by its ID
     * 
     * @param id entity ID
     * @return true if deleted successfully, false otherwise
     * @throws SQLException if database operation fails
     */
    boolean delete(Long id) throws SQLException;
    
    /**
     * Count total number of entities
     * 
     * @return count of entities
     * @throws SQLException if database operation fails
     */
    long count() throws SQLException;
    
    /**
     * Check if an entity exists by ID
     * 
     * @param id entity ID
     * @return true if exists, false otherwise
     * @throws SQLException if database operation fails
     */
    boolean existsById(Long id) throws SQLException;
}
