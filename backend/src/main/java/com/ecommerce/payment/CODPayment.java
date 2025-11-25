package com.ecommerce.payment;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Cash on Delivery Payment Implementation
 * Implements PaymentStrategy interface
 * Demonstrates Polymorphism
 */
public class CODPayment implements PaymentStrategy {
    
    private String deliveryAddress;
    private String phoneNumber;
    private String transactionId;
    private String statusMessage;
    
    public CODPayment(String deliveryAddress, String phoneNumber) {
        this.deliveryAddress = deliveryAddress;
        this.phoneNumber = phoneNumber;
    }
    
    @Override
    public boolean processPayment(BigDecimal amount) {
        if (!validatePaymentDetails()) {
            statusMessage = "Invalid delivery details";
            return false;
        }
        
        try {
            System.out.println("Processing COD order for ₹" + amount);
            System.out.println("Delivery Address: " + deliveryAddress);
            System.out.println("Contact: " + phoneNumber);
            
            // Generate transaction ID
            this.transactionId = "COD-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase();
            
            statusMessage = "COD order placed successfully. Pay on delivery.";
            System.out.println("✓ COD Order placed! Reference ID: " + transactionId);
            return true;
            
        } catch (Exception e) {
            statusMessage = "COD order failed: " + e.getMessage();
            System.err.println(statusMessage);
            return false;
        }
    }
    
    @Override
    public String getPaymentType() {
        return "COD";
    }
    
    @Override
    public boolean validatePaymentDetails() {
        // Validate delivery address
        if (deliveryAddress == null || deliveryAddress.trim().isEmpty()) {
            return false;
        }
        
        // Validate phone number (10 digits)
        if (phoneNumber == null || !phoneNumber.matches("\\d{10}")) {
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
    public String getDeliveryAddress() {
        return deliveryAddress;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
}
