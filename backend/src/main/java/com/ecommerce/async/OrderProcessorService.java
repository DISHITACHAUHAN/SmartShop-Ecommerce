package com.ecommerce.async;

import com.ecommerce.dao.OrderDAO;
import com.ecommerce.model.Order;
import com.ecommerce.model.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Order Processor Service for background order processing
 * Demonstrates ScheduledExecutorService and synchronized blocks
 */
@Service
public class OrderProcessorService {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderProcessorService.class);
    
    @Autowired
    private OrderDAO orderDAO;
    
    @Autowired
    private AsyncEmailService emailService;
    
    // ScheduledExecutorService for periodic tasks
    private ScheduledExecutorService scheduler;
    
    // Lock object for synchronization
    private final Object inventoryLock = new Object();
    
    @PostConstruct
    public void init() {
        scheduler = Executors.newScheduledThreadPool(2);
        logger.info("OrderProcessorService initialized");
        
        // Schedule periodic order processing every 5 minutes
        scheduler.scheduleAtFixedRate(
            this::processPendingOrders,
            1, // initial delay
            5, // period
            TimeUnit.MINUTES
        );
    }
    
    @PreDestroy
    public void destroy() {
        if (scheduler != null) {
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(30, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                }
                logger.info("OrderProcessorService shut down successfully");
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
    
    /**
     * Process orders in background
     * Demonstrates synchronized block for thread safety
     * 
     * @param orders list of orders to process
     */
    public void processOrdersInBackground(List<Order> orders) {
        orders.forEach(order -> 
            scheduler.submit(() -> {
                try {
                    // Synchronized block to prevent concurrent modifications
                    synchronized(inventoryLock) {
                        processOrder(order);
                    }
                } catch (Exception e) {
                    logger.error("Error processing order: " + order.getId(), e);
                }
            })
        );
        
        logger.info("Submitted {} orders for background processing", orders.size());
    }
    
    /**
     * Process a single order
     * Updates order status and sends notifications
     * 
     * @param order the order to process
     */
    private void processOrder(Order order) {
        try {
            logger.info("Processing order: {}", order.getId());
            
            // Simulate order processing logic
            if (order.getStatus() == OrderStatus.PENDING) {
                // Move to processing
                orderDAO.updateStatus(order.getId(), OrderStatus.PROCESSING);
                logger.info("Order {} moved to PROCESSING", order.getId());
                
                // Simulate processing delay
                Thread.sleep(1000);
                
                // Move to shipped
                orderDAO.updateStatus(order.getId(), OrderStatus.SHIPPED);
                logger.info("Order {} moved to SHIPPED", order.getId());
                
                // Send shipping notification email
                // emailService.sendShippingNotification(order);
            }
            
        } catch (Exception e) {
            logger.error("Error in order processing", e);
        }
    }
    
    /**
     * Process pending orders periodically
     * Scheduled task that runs in background
     */
    private void processPendingOrders() {
        try {
            logger.debug("Running scheduled pending orders processing");
            
            List<Order> pendingOrders = orderDAO.findByStatus(OrderStatus.PENDING);
            
            if (!pendingOrders.isEmpty()) {
                logger.info("Found {} pending orders to process", pendingOrders.size());
                processOrdersInBackground(pendingOrders);
            }
            
        } catch (Exception e) {
            logger.error("Error in scheduled order processing", e);
        }
    }
    
    /**
     * Update order status with thread safety
     * Demonstrates synchronized method
     * 
     * @param orderId order ID
     * @param status new status
     */
    public synchronized void updateOrderStatusSafe(Long orderId, OrderStatus status) {
        try {
            orderDAO.updateStatus(orderId, status);
            logger.info("Order {} status updated to {}", orderId, status);
        } catch (Exception e) {
            logger.error("Error updating order status", e);
        }
    }
}
