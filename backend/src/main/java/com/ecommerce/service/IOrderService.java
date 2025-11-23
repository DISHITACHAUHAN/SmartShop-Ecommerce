package com.ecommerce.service;

import com.ecommerce.model.Order;
import com.ecommerce.model.OrderStatus;
import java.util.List;

/**
 * Order Service Interface
 * Demonstrates abstraction - defines contract for order operations
 */
public interface IOrderService {
    
    /**
     * Create a new order from user's cart
     * @param userId user ID
     * @param shippingAddress shipping address
     * @return created order
     */
    Order createOrder(Long userId, String shippingAddress);
    
    /**
     * Get order by ID
     * @param orderId order ID
     * @return order
     */
    Order getOrderById(Long orderId);
    
    /**
     * Get all orders for a user
     * @param userId user ID
     * @return list of orders
     */
    List<Order> getUserOrders(Long userId);
    
    /**
     * Update order status
     * @param orderId order ID
     * @param status new status
     * @return updated order
     */
    Order updateOrderStatus(Long orderId, OrderStatus status);
    
    /**
     * Process payment for order
     * @param orderId order ID
     * @param paymentId payment transaction ID
     * @return updated order
     */
    Order processPayment(Long orderId, String paymentId);
    
    /**
     * Get all orders (admin)
     * @return list of all orders
     */
    List<Order> getAllOrders();
}
