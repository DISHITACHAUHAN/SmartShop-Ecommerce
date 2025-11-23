package com.ecommerce.dao;

import com.ecommerce.model.Product;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * ProductDAO implementation
 * Demonstrates generic DAO usage with Product entity
 * Includes inventory management operations
 */
@Repository
public class ProductDAO extends AbstractDAO<Product> {
    
    @Override
    public Product save(Product product) throws SQLException {
        String sql = "INSERT INTO products (name, description, price, stock_quantity, category, image_url) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        Long productId = executeUpdateWithGeneratedKey(sql,
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getStockQuantity(),
            product.getCategory(),
            product.getImageUrl()
        );
        
        product.setId(productId);
        logger.info("Product saved with ID: {}", productId);
        return product;
    }
    
    @Override
    public Product findById(Long id) throws SQLException {
        String sql = "SELECT * FROM products WHERE id = ?";
        return executeQuerySingle(sql, this::mapProduct, id);
    }
    
    @Override
    public List<Product> findAll() throws SQLException {
        String sql = "SELECT * FROM products ORDER BY created_at DESC";
        return executeQueryList(sql, this::mapProduct);
    }
    
    /**
     * Find products by category
     */
    public List<Product> findByCategory(String category) throws SQLException {
        String sql = "SELECT * FROM products WHERE category = ? ORDER BY name";
        return executeQueryList(sql, this::mapProduct, category);
    }
    
    /**
     * Find products by multiple categories
     * Demonstrates Collections usage
     */
    public List<Product> findByCategories(List<String> categories) throws SQLException {
        if (categories == null || categories.isEmpty()) {
            return findAll();
        }
        
        // Build dynamic SQL with IN clause
        StringBuilder sql = new StringBuilder("SELECT * FROM products WHERE category IN (");
        for (int i = 0; i < categories.size(); i++) {
            sql.append("?");
            if (i < categories.size() - 1) {
                sql.append(", ");
            }
        }
        sql.append(") ORDER BY name");
        
        return executeQueryList(sql.toString(), this::mapProduct, categories.toArray());
    }
    
    /**
     * Search products by name or description
     */
    public List<Product> searchProducts(String query) throws SQLException {
        String sql = "SELECT * FROM products WHERE name LIKE ? OR description LIKE ? ORDER BY name";
        String searchPattern = "%" + query + "%";
        return executeQueryList(sql, this::mapProduct, searchPattern, searchPattern);
    }
    
    /**
     * Find products by price range
     */
    public List<Product> findByPriceRange(java.math.BigDecimal minPrice, java.math.BigDecimal maxPrice) 
            throws SQLException {
        String sql = "SELECT * FROM products WHERE price BETWEEN ? AND ? ORDER BY price";
        return executeQueryList(sql, this::mapProduct, minPrice, maxPrice);
    }
    
    /**
     * Find products in stock
     */
    public List<Product> findInStock() throws SQLException {
        String sql = "SELECT * FROM products WHERE stock_quantity > 0 ORDER BY name";
        return executeQueryList(sql, this::mapProduct);
    }
    
    @Override
    public Product update(Product product) throws SQLException {
        String sql = "UPDATE products SET name = ?, description = ?, price = ?, " +
                    "stock_quantity = ?, category = ?, image_url = ?, updated_at = CURRENT_TIMESTAMP " +
                    "WHERE id = ?";
        
        executeUpdate(sql,
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getStockQuantity(),
            product.getCategory(),
            product.getImageUrl(),
            product.getId()
        );
        
        logger.info("Product updated: {}", product.getId());
        return product;
    }
    
    /**
     * Update product stock quantity
     * Used for inventory management
     */
    public void updateStock(Long productId, int newQuantity) throws SQLException {
        String sql = "UPDATE products SET stock_quantity = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        executeUpdate(sql, newQuantity, productId);
        logger.info("Stock updated for product {}: new quantity = {}", productId, newQuantity);
    }
    
    /**
     * Reduce product stock (for order processing)
     * Returns true if stock was reduced successfully
     */
    public boolean reduceStock(Long productId, int quantity) throws SQLException {
        String sql = "UPDATE products SET stock_quantity = stock_quantity - ?, " +
                    "updated_at = CURRENT_TIMESTAMP " +
                    "WHERE id = ? AND stock_quantity >= ?";
        
        int affected = executeUpdate(sql, quantity, productId, quantity);
        
        if (affected > 0) {
            logger.info("Stock reduced for product {}: quantity = {}", productId, quantity);
            return true;
        } else {
            logger.warn("Insufficient stock for product {}: requested = {}", productId, quantity);
            return false;
        }
    }
    
    /**
     * Increase product stock (for restocking)
     */
    public void increaseStock(Long productId, int quantity) throws SQLException {
        String sql = "UPDATE products SET stock_quantity = stock_quantity + ?, " +
                    "updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        executeUpdate(sql, quantity, productId);
        logger.info("Stock increased for product {}: quantity = {}", productId, quantity);
    }
    
    @Override
    public boolean delete(Long id) throws SQLException {
        String sql = "DELETE FROM products WHERE id = ?";
        int affected = executeUpdate(sql, id);
        logger.info("Product deleted: {}", id);
        return affected > 0;
    }
    
    @Override
    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM products";
        return executeCount(sql);
    }
    
    /**
     * Count products by category
     */
    public long countByCategory(String category) throws SQLException {
        String sql = "SELECT COUNT(*) FROM products WHERE category = ?";
        return executeCount(sql, category);
    }
    
    /**
     * Get all unique categories
     * Demonstrates Set usage for unique values
     */
    public java.util.Set<String> getAllCategories() throws SQLException {
        String sql = "SELECT DISTINCT category FROM products WHERE category IS NOT NULL ORDER BY category";
        List<Product> products = executeQueryList(sql, rs -> {
            Product p = new Product();
            p.setCategory(rs.getString("category"));
            return p;
        });
        
        java.util.Set<String> categories = new java.util.HashSet<>();
        for (Product p : products) {
            categories.add(p.getCategory());
        }
        
        return categories;
    }
    
    /**
     * Map ResultSet to Product object
     */
    private Product mapProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getLong("id"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setStockQuantity(rs.getInt("stock_quantity"));
        product.setCategory(rs.getString("category"));
        product.setImageUrl(rs.getString("image_url"));
        product.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        product.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return product;
    }
}
