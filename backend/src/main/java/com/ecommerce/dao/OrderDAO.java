package com.ecommerce.dao;

import com.ecommerce.model.Order;
import com.ecommerce.model.OrderItem;
import com.ecommerce.model.OrderStatus;
import com.ecommerce.model.PaymentStatus;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.math.BigDecimal;

/**
 * OrderDAO implementation
 * Demonstrates transaction management with order and order items
 */
@Repository
public class OrderDAO extends AbstractDAO<Order> {
    
    @Override
    public Order save(Order order) throws SQLException {
        String sql = "INSERT INTO orders (user_id, total_amount, status, payment_status, shipping_address) " +
                    "VALUES (?, ?, ?, ?, ?)";
        
        Long orderId = executeUpdateWithGeneratedKey(sql,
            order.getUserId(),
            order.getTotalAmount(),
            order.getStatus().name(),
            order.getPaymentStatus().name(),
            order.getShippingAddress()
        );
        
        order.setId(orderId);
        
        // Save order items
        if (order.getItems() != null) {
            for (OrderItem item : order.getItems()) {
                saveOrderItem(orderId, item);
            }
        }
        
        logger.info("Order saved with ID: {}", orderId);
        return order;
    }
    
    private void saveOrderItem(Long orderId, OrderItem item) throws SQLException {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        Long itemId = executeUpdateWithGeneratedKey(sql,
            orderId,
            item.getProductId(),
            item.getQuantity(),
            item.getPrice()
        );
        item.setId(itemId);
        item.setOrderId(orderId);
    }
    
    @Override
    public Order findById(Long id) throws SQLException {
        String sql = "SELECT * FROM orders WHERE id = ?";
        Order order = executeQuerySingle(sql, this::mapOrder, id);
        
        if (order != null) {
            order.setItems(findOrderItems(id));
        }
        
        return order;
    }
    
    private List<OrderItem> findOrderItems(Long orderId) throws SQLException {
        String sql = "SELECT oi.*, p.name as product_name FROM order_items oi " +
                    "JOIN products p ON oi.product_id = p.id WHERE oi.order_id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<OrderItem> items = new java.util.ArrayList<>();
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, orderId);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                items.add(mapOrderItem(rs));
            }
            
            return items;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }
    
    @Override
    public List<Order> findAll() throws SQLException {
        String sql = "SELECT * FROM orders ORDER BY order_date DESC";
        return executeQueryList(sql, this::mapOrder);
    }
    
    public List<Order> findByUserId(Long userId) throws SQLException {
        String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY order_date DESC";
        return executeQueryList(sql, this::mapOrder, userId);
    }
    
    public List<Order> findByStatus(OrderStatus status) throws SQLException {
        String sql = "SELECT * FROM orders WHERE status = ? ORDER BY order_date DESC";
        return executeQueryList(sql, this::mapOrder, status.name());
    }
    
    public List<Order> findByDateRange(LocalDate startDate, LocalDate endDate) throws SQLException {
        String sql = "SELECT * FROM orders WHERE DATE(order_date) BETWEEN ? AND ? ORDER BY order_date DESC";
        return executeQueryList(sql, this::mapOrder, startDate, endDate);
    }
    
    @Override
    public Order update(Order order) throws SQLException {
        String sql = "UPDATE orders SET status = ?, payment_id = ?, payment_status = ? WHERE id = ?";
        executeUpdate(sql,
            order.getStatus().name(),
            order.getPaymentId(),
            order.getPaymentStatus().name(),
            order.getId()
        );
        logger.info("Order updated: {}", order.getId());
        return order;
    }
    
    public void updateStatus(Long orderId, OrderStatus status) throws SQLException {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        executeUpdate(sql, status.name(), orderId);
    }
    
    public void updatePaymentStatus(Long orderId, String paymentId, PaymentStatus status) throws SQLException {
        String sql = "UPDATE orders SET payment_id = ?, payment_status = ? WHERE id = ?";
        executeUpdate(sql, paymentId, status.name(), orderId);
    }
    
    @Override
    public boolean delete(Long id) throws SQLException {
        String sql = "DELETE FROM orders WHERE id = ?";
        return executeUpdate(sql, id) > 0;
    }
    
    @Override
    public long count() throws SQLException {
        return executeCount("SELECT COUNT(*) FROM orders");
    }
    
    public BigDecimal getTotalSalesByDateRange(LocalDate startDate, LocalDate endDate) throws SQLException {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) FROM orders " +
                    "WHERE DATE(order_date) BETWEEN ? AND ? AND payment_status = 'COMPLETED'";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setObject(1, startDate);
            stmt.setObject(2, endDate);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getBigDecimal(1);
            }
            return BigDecimal.ZERO;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }
    
    public long countOrdersByDateRange(LocalDate startDate, LocalDate endDate) throws SQLException {
        String sql = "SELECT COUNT(*) FROM orders WHERE DATE(order_date) BETWEEN ? AND ?";
        return executeCount(sql, startDate, endDate);
    }
    
    private Order mapOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getLong("id"));
        order.setUserId(rs.getLong("user_id"));
        order.setTotalAmount(rs.getBigDecimal("total_amount"));
        order.setStatus(OrderStatus.valueOf(rs.getString("status")));
        order.setPaymentId(rs.getString("payment_id"));
        order.setPaymentStatus(PaymentStatus.valueOf(rs.getString("payment_status")));
        order.setShippingAddress(rs.getString("shipping_address"));
        order.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());
        return order;
    }
    
    private OrderItem mapOrderItem(ResultSet rs) throws SQLException {
        OrderItem item = new OrderItem();
        item.setId(rs.getLong("id"));
        item.setOrderId(rs.getLong("order_id"));
        item.setProductId(rs.getLong("product_id"));
        item.setProductName(rs.getString("product_name"));
        item.setQuantity(rs.getInt("quantity"));
        item.setPrice(rs.getBigDecimal("price"));
        return item;
    }
}
