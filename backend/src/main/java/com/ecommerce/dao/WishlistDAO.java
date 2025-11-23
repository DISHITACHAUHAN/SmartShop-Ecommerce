package com.ecommerce.dao;

import com.ecommerce.model.Wishlist;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * WishlistDAO implementation
 * Manages user wishlist operations
 */
@Repository
public class WishlistDAO extends AbstractDAO<Wishlist> {
    
    @Override
    public Wishlist save(Wishlist wishlist) throws SQLException {
        String sql = "INSERT INTO wishlist (user_id, product_id) VALUES (?, ?)";
        Long id = executeUpdateWithGeneratedKey(sql, wishlist.getUserId(), wishlist.getProductId());
        wishlist.setId(id);
        return wishlist;
    }
    
    @Override
    public Wishlist findById(Long id) throws SQLException {
        String sql = "SELECT * FROM wishlist WHERE id = ?";
        return executeQuerySingle(sql, this::mapWishlist, id);
    }
    
    @Override
    public List<Wishlist> findAll() throws SQLException {
        String sql = "SELECT * FROM wishlist";
        return executeQueryList(sql, this::mapWishlist);
    }
    
    public List<Wishlist> findByUserId(Long userId) throws SQLException {
        String sql = "SELECT * FROM wishlist WHERE user_id = ?";
        return executeQueryList(sql, this::mapWishlist, userId);
    }
    
    @Override
    public Wishlist update(Wishlist wishlist) throws SQLException {
        throw new UnsupportedOperationException("Wishlist items cannot be updated");
    }
    
    @Override
    public boolean delete(Long id) throws SQLException {
        String sql = "DELETE FROM wishlist WHERE id = ?";
        return executeUpdate(sql, id) > 0;
    }
    
    public boolean deleteByUserAndProduct(Long userId, Long productId) throws SQLException {
        String sql = "DELETE FROM wishlist WHERE user_id = ? AND product_id = ?";
        return executeUpdate(sql, userId, productId) > 0;
    }
    
    @Override
    public long count() throws SQLException {
        return executeCount("SELECT COUNT(*) FROM wishlist");
    }
    
    private Wishlist mapWishlist(ResultSet rs) throws SQLException {
        Wishlist wishlist = new Wishlist();
        wishlist.setId(rs.getLong("id"));
        wishlist.setUserId(rs.getLong("user_id"));
        wishlist.setProductId(rs.getLong("product_id"));
        wishlist.setAddedAt(rs.getTimestamp("added_at").toLocalDateTime());
        return wishlist;
    }
}
