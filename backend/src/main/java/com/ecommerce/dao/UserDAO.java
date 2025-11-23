package com.ecommerce.dao;

import com.ecommerce.model.User;
import com.ecommerce.model.Customer;
import com.ecommerce.model.Admin;
import com.ecommerce.model.UserRole;
import com.ecommerce.model.AccessLevel;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

/**
 * UserDAO implementation
 * Demonstrates generic DAO usage with User entity
 * Handles polymorphic User types (Customer and Admin)
 */
@Repository
public class UserDAO extends AbstractDAO<User> {
    
    @Override
    public User save(User user) throws SQLException {
        String sql = "INSERT INTO users (email, password, first_name, last_name, role) VALUES (?, ?, ?, ?, ?)";
        
        Long userId = executeUpdateWithGeneratedKey(sql,
            user.getEmail(),
            user.getPassword(),
            user.getFirstName(),
            user.getLastName(),
            user.getRole().name()
        );
        
        user.setId(userId);
        
        // Save type-specific data
        if (user instanceof Customer) {
            saveCustomerDetails((Customer) user);
        } else if (user instanceof Admin) {
            saveAdminDetails((Admin) user);
        }
        
        logger.info("User saved with ID: {}", userId);
        return user;
    }
    
    /**
     * Save customer-specific details
     */
    private void saveCustomerDetails(Customer customer) throws SQLException {
        String sql = "INSERT INTO customers (user_id, phone_number, shipping_address) VALUES (?, ?, ?)";
        executeUpdate(sql,
            customer.getId(),
            customer.getPhoneNumber(),
            customer.getShippingAddress()
        );
    }
    
    /**
     * Save admin-specific details
     */
    private void saveAdminDetails(Admin admin) throws SQLException {
        String sql = "INSERT INTO admins (user_id, department, access_level) VALUES (?, ?, ?)";
        executeUpdate(sql,
            admin.getId(),
            admin.getDepartment(),
            admin.getAccessLevel() != null ? admin.getAccessLevel().name() : AccessLevel.LIMITED.name()
        );
    }
    
    @Override
    public User findById(Long id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        return executeQuerySingle(sql, this::mapUser, id);
    }
    
    /**
     * Find user by email
     * Used for authentication
     */
    public User findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ?";
        return executeQuerySingle(sql, this::mapUser, email);
    }
    
    @Override
    public List<User> findAll() throws SQLException {
        String sql = "SELECT * FROM users ORDER BY created_at DESC";
        return executeQueryList(sql, this::mapUser);
    }
    
    /**
     * Find all users by role
     */
    public List<User> findByRole(UserRole role) throws SQLException {
        String sql = "SELECT * FROM users WHERE role = ? ORDER BY created_at DESC";
        return executeQueryList(sql, this::mapUser, role.name());
    }
    
    @Override
    public User update(User user) throws SQLException {
        String sql = "UPDATE users SET email = ?, first_name = ?, last_name = ?, " +
                    "updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        
        executeUpdate(sql,
            user.getEmail(),
            user.getFirstName(),
            user.getLastName(),
            user.getId()
        );
        
        // Update type-specific data
        if (user instanceof Customer) {
            updateCustomerDetails((Customer) user);
        } else if (user instanceof Admin) {
            updateAdminDetails((Admin) user);
        }
        
        logger.info("User updated: {}", user.getId());
        return user;
    }
    
    /**
     * Update customer-specific details
     */
    private void updateCustomerDetails(Customer customer) throws SQLException {
        String sql = "UPDATE customers SET phone_number = ?, shipping_address = ? WHERE user_id = ?";
        executeUpdate(sql,
            customer.getPhoneNumber(),
            customer.getShippingAddress(),
            customer.getId()
        );
    }
    
    /**
     * Update admin-specific details
     */
    private void updateAdminDetails(Admin admin) throws SQLException {
        String sql = "UPDATE admins SET department = ?, access_level = ? WHERE user_id = ?";
        executeUpdate(sql,
            admin.getDepartment(),
            admin.getAccessLevel().name(),
            admin.getId()
        );
    }
    
    /**
     * Update user password
     */
    public void updatePassword(Long userId, String newPassword) throws SQLException {
        String sql = "UPDATE users SET password = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        executeUpdate(sql, newPassword, userId);
        logger.info("Password updated for user: {}", userId);
    }
    
    @Override
    public boolean delete(Long id) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        int affected = executeUpdate(sql, id);
        logger.info("User deleted: {}", id);
        return affected > 0;
    }
    
    @Override
    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM users";
        return executeCount(sql);
    }
    
    /**
     * Check if email already exists
     */
    public boolean existsByEmail(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        return executeCount(sql, email) > 0;
    }
    
    /**
     * Map ResultSet to User object (polymorphic)
     * Demonstrates polymorphism - returns Customer or Admin based on role
     */
    private User mapUser(ResultSet rs) throws SQLException {
        UserRole role = UserRole.valueOf(rs.getString("role"));
        User user;
        
        // Create appropriate subclass based on role
        if (role == UserRole.CUSTOMER) {
            user = new Customer();
            loadCustomerDetails((Customer) user, rs.getLong("id"));
        } else {
            user = new Admin();
            loadAdminDetails((Admin) user, rs.getLong("id"));
        }
        
        // Set common fields
        user.setId(rs.getLong("id"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setRole(role);
        user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        user.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        
        return user;
    }
    
    /**
     * Load customer-specific details
     */
    private void loadCustomerDetails(Customer customer, Long userId) throws SQLException {
        String sql = "SELECT * FROM customers WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, userId);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                customer.setPhoneNumber(rs.getString("phone_number"));
                customer.setShippingAddress(rs.getString("shipping_address"));
            }
        } finally {
            closeResources(conn, stmt, rs);
        }
    }
    
    /**
     * Load admin-specific details
     */
    private void loadAdminDetails(Admin admin, Long userId) throws SQLException {
        String sql = "SELECT * FROM admins WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, userId);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                admin.setDepartment(rs.getString("department"));
                String accessLevel = rs.getString("access_level");
                admin.setAccessLevel(accessLevel != null ? AccessLevel.valueOf(accessLevel) : AccessLevel.LIMITED);
            }
        } finally {
            closeResources(conn, stmt, rs);
        }
    }
}
