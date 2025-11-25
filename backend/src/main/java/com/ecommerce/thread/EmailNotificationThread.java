package com.ecommerce.thread;

import com.ecommerce.model.Order;
import java.util.concurrent.BlockingQueue;

/**
 * Email Notification Thread
 * Demonstrates Multithreading by implementing Runnable interface
 * 
 * Sends email notifications asynchronously
 */
public class EmailNotificationThread implements Runnable {
    
    private BlockingQueue<Order> orderQueue;
    private volatile boolean running = true;
    private int emailsSent = 0;
    private String threadName;
    
    /**
     * Constructor
     * 
     * @param orderQueue queue of orders for email notification
     */
    public EmailNotificationThread(BlockingQueue<Order> orderQueue) {
        this.orderQueue = orderQueue;
        this.threadName = "EmailNotifier-" + System.currentTimeMillis();
    }
    
    /**
     * Run method - executed when thread starts
     * Demonstrates Runnable implementation
     */
    @Override
    public void run() {
        System.out.println(threadName + " started");
        
        while (running) {
            try {
                // Take order from queue (blocks if empty)
                Order order = orderQueue.take();
                
                System.out.println(threadName + " sending email for order: " + order.getId());
                
                // Send email notification
                sendOrderConfirmationEmail(order);
                
                emailsSent++;
                System.out.println(threadName + " email sent for order: " + order.getId() +
                                 " (Total emails: " + emailsSent + ")");
                
            } catch (InterruptedException e) {
                System.out.println(threadName + " interrupted");
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                System.err.println(threadName + " error: " + e.getMessage());
            }
        }
        
        System.out.println(threadName + " stopped. Total emails sent: " + emailsSent);
    }
    
    /**
     * Send order confirmation email
     * Simulates email sending
     * 
     * @param order order to send email for
     */
    private void sendOrderConfirmationEmail(Order order) throws InterruptedException {
        System.out.println("  [" + threadName + "] Composing email for order " + order.getId());
        Thread.sleep(300);
        
        System.out.println("  [" + threadName + "] Connecting to email server...");
        Thread.sleep(500);
        
        System.out.println("  [" + threadName + "] Sending email to user...");
        Thread.sleep(700);
        
        System.out.println("  [" + threadName + "] ✓ Email sent successfully!");
    }
    
    /**
     * Stop the thread gracefully
     */
    public void stop() {
        running = false;
    }
    
    /**
     * Get number of emails sent
     * 
     * @return email count
     */
    public int getEmailsSent() {
        return emailsSent;
    }
    
    /**
     * Check if thread is still running
     * 
     * @return true if running
     */
    public boolean isRunning() {
        return running;
    }
    
    public String getThreadName() {
        return threadName;
    }
}
