package com.ecommerce.thread;

import com.ecommerce.model.Order;
import java.util.concurrent.BlockingQueue;

/**
 * Order Processing Thread
 * Demonstrates Multithreading by extending Thread class
 * 
 * Processes orders asynchronously in background
 */
public class OrderProcessingThread extends Thread {
    
    private BlockingQueue<Order> orderQueue;
    private volatile boolean running = true;
    private int processedOrders = 0;
    
    /**
     * Constructor
     * 
     * @param orderQueue queue of orders to process
     */
    public OrderProcessingThread(BlockingQueue<Order> orderQueue) {
        this.orderQueue = orderQueue;
        setName("OrderProcessor-" + getId());
    }
    
    /**
     * Run method - executed when thread starts
     * Demonstrates thread execution
     */
    @Override
    public void run() {
        System.out.println(getName() + " started");
        
        while (running) {
            try {
                // Take order from queue (blocks if empty)
                Order order = orderQueue.take();
                
                System.out.println(getName() + " processing order: " + order.getId());
                
                // Process order
                processOrder(order);
                
                processedOrders++;
                System.out.println(getName() + " completed order: " + order.getId() + 
                                 " (Total processed: " + processedOrders + ")");
                
            } catch (InterruptedException e) {
                System.out.println(getName() + " interrupted");
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                System.err.println(getName() + " error: " + e.getMessage());
            }
        }
        
        System.out.println(getName() + " stopped. Total orders processed: " + processedOrders);
    }
    
    /**
     * Process a single order
     * Simulates order processing steps
     * 
     * @param order order to process
     */
    private void processOrder(Order order) throws InterruptedException {
        // Step 1: Validate order
        System.out.println("  [" + getName() + "] Validating order " + order.getId());
        Thread.sleep(500);
        
        // Step 2: Check inventory
        System.out.println("  [" + getName() + "] Checking inventory for order " + order.getId());
        Thread.sleep(500);
        
        // Step 3: Process payment
        System.out.println("  [" + getName() + "] Processing payment for order " + order.getId());
        Thread.sleep(1000);
        
        // Step 4: Update order status
        System.out.println("  [" + getName() + "] Updating status for order " + order.getId());
        order.setStatus(com.ecommerce.model.OrderStatus.PROCESSING);
        Thread.sleep(500);
        
        // Step 5: Prepare for shipping
        System.out.println("  [" + getName() + "] Preparing shipment for order " + order.getId());
        Thread.sleep(500);
        
        order.setStatus(com.ecommerce.model.OrderStatus.SHIPPED);
    }
    
    /**
     * Stop the thread gracefully
     */
    public void stopProcessing() {
        running = false;
        interrupt();
    }
    
    /**
     * Get number of processed orders
     * 
     * @return processed order count
     */
    public int getProcessedOrders() {
        return processedOrders;
    }
    
    /**
     * Check if thread is still running
     * 
     * @return true if running
     */
    public boolean isRunning() {
        return running;
    }
}
