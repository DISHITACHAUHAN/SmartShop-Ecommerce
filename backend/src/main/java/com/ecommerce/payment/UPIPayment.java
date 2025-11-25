package com.ecommerce.payment;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * UPI Payment Implementation
 * Implements PaymentStrategy interface
 * Demonstrates Polymorphism
 */
public class UPIPayment implements PaymentStrategy {
    
    private String upiId;
    private String pin;
    private String transactionId;
    private String statusMessage;
    
    public UPIPayment(String upiId, String pin) {
        this.upiId = upiId;
        this.pin = pin;
    }
    
    @Override
    public boolean processPayment(BigDecimal amount) {
        if (!validatePaymentDetails()) {
            statusMessage = "Invalid UPI details";
            return false;
        }
        
        try {
            System.out.println("Processing UPI payment of ₹" + amount);
            System.out.println("UPI ID: " + upiId);
            
            // Generate transaction ID
            this.transactionId = "UPI-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase();
            
            // Simulate UPI payment (1.5 seconds delay)
            Thread.sleep(1500);
            
            statusMessage = "Payment successful via UPI";
            System.out.println("✓ UPI Payment successful! Transaction ID: " + transactionId);
            return true;
            
        } catch (InterruptedException e) {
            statusMessage = "Payment interrupted";
            Thread.currentThread().interrupt();
            return false;
        } catch (Exception e) {
            statusMessage = "UPI payment failed: " + e.getMessage();
            System.err.println(statusMessage);
            return false;
        }
    }
    
    @Override
    public String getPaymentType() {
        return "UPI";
    }
    
    @Override
    public boolean validatePaymentDetails() {
        // Validate UPI ID format (username@bank)
        if (upiId == null || !upiId.matches("^[\\w.]+@[\\w]+$")) {
            return false;
        }
        
        // Validate PIN (should be 4 or 6 digits)
        if (pin == null || !pin.matches("\\d{4,6}")) {
            return false;
        }
        
        return true;
    }
    
    @Override
    public String getTransactionId() {
        return transactionId;
    }
    
    @Override
    public String getStatusMessage() {
        return statusMessage;
    }
    
    // Getters
    public String getUpiId() {
        return upiId;
    }
}
