package com.ecommerce.service.impl;

import com.ecommerce.dao.*;
import com.ecommerce.exception.InsufficientStockException;
import com.ecommerce.model.*;
import com.ecommerce.service.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Order Service Implementation
 * Demonstrates transaction management with commit/rollback
 */
@Service
public class OrderServiceImpl implements IOrderService {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    
    @Autowired
    private OrderDAO orderDAO;
    
    @Autowired
    private CartDAO cartDAO;
    
    @Autowired
    private ProductDAO productDAO;
    
    @Autowired
    private TransactionManager transactionManager;
    
    @Override
    public Order createOrder(Long userId, String shippingAddress) {
        // Execute order creation within transaction
        return transactionManager.executeInTransaction(conn -> {
            try {
                // Get cart items
                List<CartItem> cartItems = cartDAO.findByUserId(userId);
                if (cartItems.isEmpty()) {
                    throw new IllegalStateException("Cart is empty");
                }
                
                // Create order
                Order order = new Order(userId, shippingAddress);
                List<OrderItem> orderItems = new ArrayList<>();
                BigDecimal totalAmount = BigDecimal.ZERO;
                
                // Process each cart item
                for (CartItem cartItem : cartItems) {
                    // Check and reduce stock
                    boolean stockReduced = productDAO.reduceStock(
                        cartItem.getProductId(), 
                        cartItem.getQuantity()
                    );
                    
                    if (!stockReduced) {
                        throw new InsufficientStockException(
                            "Insufficient stock for product: " + cartItem.getProductName());
                    }
                    
                    // Create order item
                    OrderItem orderItem = new OrderItem();
                    orderItem.setProductId(cartItem.getProductId());
                    orderItem.setProductName(cartItem.getProductName());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setPrice(cartItem.getPrice());
                    orderItems.add(orderItem);
                    
                    totalAmount = totalAmount.add(cartItem.getTotal());
                }
                
                order.setItems(orderItems);
                order.setTotalAmount(totalAmount);
                
                // Save order
                Order savedOrder = orderDAO.save(order);
                
                // Clear cart
                cartDAO.deleteByUserId(userId);
                
                logger.info("Order created successfully: {}", savedOrder.getId());
                return savedOrder;
                
            } catch (SQLException e) {
                logger.error("Error creating order", e);
                throw new RuntimeException("Order creation failed: " + e.getMessage());
            }
        });
    }
    
    @Override
    public Order getOrderById(Long orderId) {
        try {
            return orderDAO.findById(orderId);
        } catch (SQLException e) {
            logger.error("Error fetching order", e);
            throw new RuntimeException("Failed to fetch order: " + e.getMessage());
        }
    }
    
    @Override
    public List<Order> getUserOrders(Long userId) {
        try {
            return orderDAO.findByUserId(userId);
        } catch (SQLException e) {
            logger.error("Error fetching user orders", e);
            throw new RuntimeException("Failed to fetch orders: " + e.getMessage());
        }
    }
    
    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus status) {
        try {
            orderDAO.updateStatus(orderId, status);
            return orderDAO.findById(orderId);
        } catch (SQLException e) {
            logger.error("Error updating order status", e);
            throw new RuntimeException("Failed to update order: " + e.getMessage());
        }
    }
    
    @Override
    public Order processPayment(Long orderId, String paymentId) {
        try {
            orderDAO.updatePaymentStatus(orderId, paymentId, PaymentStatus.COMPLETED);
            Order order = orderDAO.findById(orderId);
            logger.info("Payment processed for order: {}", orderId);
            return order;
        } catch (SQLException e) {
            logger.error("Error processing payment", e);
            throw new RuntimeException("Payment processing failed: " + e.getMessage());
        }
    }
    
    @Override
    public List<Order> getAllOrders() {
        try {
            return orderDAO.findAll();
        } catch (SQLException e) {
            logger.error("Error fetching all orders", e);
            throw new RuntimeException("Failed to fetch orders: " + e.getMessage());
        }
    }
}
