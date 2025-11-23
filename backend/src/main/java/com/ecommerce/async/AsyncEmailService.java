package com.ecommerce.async;

import com.ecommerce.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Async Email Service with ExecutorService
 * Demonstrates multithreading for sending emails asynchronously
 * Uses fixed thread pool to manage concurrent email sending
 */
@Service
public class AsyncEmailService {
    
    private static final Logger logger = LoggerFactory.getLogger(AsyncEmailService.class);
    private static final int THREAD_POOL_SIZE = 5;
    
    @Autowired(required = false)
    private JavaMailSender mailSender;
    
    // ExecutorService for managing email sending threads
    private ExecutorService executorService;
    
    /**
     * Initialize thread pool on service creation
     */
    @PostConstruct
    public void init() {
        executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        logger.info("AsyncEmailService initialized with {} threads", THREAD_POOL_SIZE);
    }
    
    /**
     * Shutdown thread pool gracefully on service destruction
     */
    @PreDestroy
    public void destroy() {
        if (executorService != null) {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
                logger.info("AsyncEmailService thread pool shut down successfully");
            } catch (InterruptedException e) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
    
    /**
     * Send order confirmation email asynchronously
     * Demonstrates ExecutorService.submit() for async execution
     * 
     * @param order the order to send confirmation for
     * @param email recipient email address
     */
    public void sendOrderConfirmationAsync(Order order, String email) {
        executorService.submit(() -> {
            try {
                logger.info("Sending order confirmation email to: {}", email);
                
                String subject = "Order Confirmation - Order #" + order.getId();
                String body = generateOrderConfirmationEmail(order);
                
                sendEmail(email, subject, body);
                
                logger.info("Order confirmation email sent successfully to: {}", email);
                
            } catch (Exception e) {
                logger.error("Failed to send order confirmation email to: " + email, e);
            }
        });
    }
    
    /**
     * Send bulk promotional emails asynchronously
     * Demonstrates parallel processing with ExecutorService
     * 
     * @param emails list of recipient emails
     * @param subject email subject
     * @param content email content
     */
    public void sendBulkPromotionalEmails(List<String> emails, String subject, String content) {
        logger.info("Starting bulk email send to {} recipients", emails.size());
        
        emails.forEach(email -> 
            executorService.submit(() -> {
                try {
                    sendEmail(email, subject, content);
                    logger.debug("Promotional email sent to: {}", email);
                } catch (Exception e) {
                    logger.error("Failed to send promotional email to: " + email, e);
                }
            })
        );
        
        logger.info("Bulk email tasks submitted to thread pool");
    }
    
    /**
     * Send password reset email asynchronously
     * 
     * @param email recipient email
     * @param resetToken password reset token
     */
    public void sendPasswordResetEmailAsync(String email, String resetToken) {
        executorService.submit(() -> {
            try {
                logger.info("Sending password reset email to: {}", email);
                
                String subject = "Password Reset Request";
                String body = "Click the link to reset your password: \n" +
                             "http://localhost:3000/reset-password?token=" + resetToken;
                
                sendEmail(email, subject, body);
                
                logger.info("Password reset email sent to: {}", email);
                
            } catch (Exception e) {
                logger.error("Failed to send password reset email to: " + email, e);
            }
        });
    }
    
    /**
     * Send welcome email to new users asynchronously
     * 
     * @param email recipient email
     * @param firstName user's first name
     */
    public void sendWelcomeEmailAsync(String email, String firstName) {
        executorService.submit(() -> {
            try {
                logger.info("Sending welcome email to: {}", email);
                
                String subject = "Welcome to E-Commerce Platform!";
                String body = "Hello " + firstName + ",\n\n" +
                             "Welcome to our E-Commerce Platform! " +
                             "We're excited to have you on board.\n\n" +
                             "Start shopping now and enjoy great deals!\n\n" +
                             "Best regards,\nE-Commerce Team";
                
                sendEmail(email, subject, body);
                
                logger.info("Welcome email sent to: {}", email);
                
            } catch (Exception e) {
                logger.error("Failed to send welcome email to: " + email, e);
            }
        });
    }
    
    /**
     * Send email using JavaMailSender
     * 
     * @param to recipient email
     * @param subject email subject
     * @param body email body
     */
    private void sendEmail(String to, String subject, String body) {
        if (mailSender == null) {
            logger.warn("JavaMailSender not configured. Email not sent to: {}", to);
            return;
        }
        
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom("noreply@ecommerce.com");
            
            mailSender.send(message);
            
        } catch (Exception e) {
            logger.error("Error sending email", e);
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }
    
    /**
     * Generate order confirmation email content
     * 
     * @param order the order
     * @return email body
     */
    private String generateOrderConfirmationEmail(Order order) {
        StringBuilder body = new StringBuilder();
        body.append("Thank you for your order!\n\n");
        body.append("Order ID: ").append(order.getId()).append("\n");
        body.append("Order Date: ").append(order.getOrderDate()).append("\n");
        body.append("Total Amount: $").append(order.getTotalAmount()).append("\n\n");
        body.append("Order Items:\n");
        
        if (order.getItems() != null) {
            order.getItems().forEach(item -> {
                body.append("- ").append(item.getProductName())
                    .append(" x ").append(item.getQuantity())
                    .append(" = $").append(item.getTotal())
                    .append("\n");
            });
        }
        
        body.append("\nShipping Address:\n");
        body.append(order.getShippingAddress()).append("\n\n");
        body.append("We'll send you another email when your order ships.\n\n");
        body.append("Best regards,\nE-Commerce Team");
        
        return body.toString();
    }
    
    /**
     * Get executor service statistics
     * 
     * @return statistics map
     */
    public java.util.Map<String, Object> getStatistics() {
        java.util.Map<String, Object> stats = new java.util.HashMap<>();
        stats.put("threadPoolSize", THREAD_POOL_SIZE);
        stats.put("isShutdown", executorService.isShutdown());
        stats.put("isTerminated", executorService.isTerminated());
        return stats;
    }
}
