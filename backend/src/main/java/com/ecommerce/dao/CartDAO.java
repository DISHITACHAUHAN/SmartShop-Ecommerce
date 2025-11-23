package com.ecommerce.dao;

import com.ecommerce.model.CartItem;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * CartDAO implementation
 * Manages shopping cart operations
 */
@Repository
public class CartDAO extends AbstractDAO<CartItem> {
    
    @Override
    public CartItem save(CartItem cartItem) throws SQLException {
        String sql = "INSERT INTO cart_items (user_id, product_id, quantity, price) VALUES (?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE quantity = quantity + VALUES(quantity)";
        
        Long id = executeUpdateWithGeneratedKey(sql,
            cartItem.getUserId(),
            cartItem.getProductId(),
            cartItem.getQuantity(),
            cartItem.getPrice()
        );
        
        cartItem.setId(id);
        logger.info("Cart item saved: {}", id);
        return cartItem;
    }
    
    @Override
    public CartItem findById(Long id) throws SQLException {
        String sql = "SELECT ci.*, p.name as product_name, p.image_url FROM cart_items ci " +
                    "JOIN products p ON ci.product_id = p.id WHERE ci.id = ?";
        return executeQuerySingle(sql, this::mapCartItem, id);
    }
    
    @Override
    public List<CartItem> findAll() throws SQLException {
        String sql = "SELECT ci.*, p.name as product_name, p.image_url FROM cart_items ci " +
                    "JOIN products p ON ci.product_id = p.id";
        return executeQueryList(sql, this::mapCartItem);
    }
    
    public List<CartItem> findByUserId(Long userId) throws SQLException {
        String sql = "SELECT ci.*, p.name as product_name, p.image_url FROM cart_items ci " +
                    "JOIN products p ON ci.product_id = p.id WHERE ci.user_id = ?";
        return executeQueryList(sql, this::mapCartItem, userId);
    }
    
    @Override
    public CartItem update(CartItem cartItem) throws SQLException {
        String sql = "UPDATE cart_items SET quantity = ?, price = ? WHERE id = ?";
        executeUpdate(sql, cartItem.getQuantity(), cartItem.getPrice(), cartItem.getId());
        return cartItem;
    }
    
    @Override
    public boolean delete(Long id) throws SQLException {
        String sql = "DELETE FROM cart_items WHERE id = ?";
        return executeUpdate(sql, id) > 0;
    }
    
    public void deleteByUserId(Long userId) throws SQLException {
        String sql = "DELETE FROM cart_items WHERE user_id = ?";
        executeUpdate(sql, userId);
        logger.info("Cart cleared for user: {}", userId);
    }
    
    @Override
    public long count() throws SQLException {
        return executeCount("SELECT COUNT(*) FROM cart_items");
    }
    
    private CartItem mapCartItem(ResultSet rs) throws SQLException {
        CartItem item = new CartItem();
        item.setId(rs.getLong("id"));
        item.setUserId(rs.getLong("user_id"));
        item.setProductId(rs.getLong("product_id"));
        item.setProductName(rs.getString("product_name"));
        item.setQuantity(rs.getInt("quantity"));
        item.setPrice(rs.getBigDecimal("price"));
        item.setImageUrl(rs.getString("image_url"));
        item.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return item;
    }
}
