package com.ecommerce.payment;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Card Payment Implementation
 * Implements PaymentStrategy interface
 * Demonstrates Polymorphism
 */
public class CardPayment implements PaymentStrategy {
    
    private String cardNumber;
    private String cardHolderName;
    private String expiryDate;
    private String cvv;
    private String transactionId;
    private String statusMessage;
    
    public CardPayment(String cardNumber, String cardHolderName, String expiryDate, String cvv) {
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }
    
    @Override
    public boolean processPayment(BigDecimal amount) {
        if (!validatePaymentDetails()) {
            statusMessage = "Invalid card details";
            return false;
        }
        
        try {
            // Simulate card payment processing
            System.out.println("Processing card payment of ₹" + amount);
            System.out.println("Card: " + maskCardNumber());
            System.out.println("Holder: " + cardHolderName);
            
            // Generate transaction ID
            this.transactionId = "CARD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            
            // Simulate payment gateway call (2 seconds delay)
            Thread.sleep(2000);
            
            statusMessage = "Payment successful via Card";
            System.out.println("✓ Payment successful! Transaction ID: " + transactionId);
            return true;
            
        } catch (InterruptedException e) {
            statusMessage = "Payment interrupted";
            Thread.currentThread().interrupt();
            return false;
        } catch (Exception e) {
            statusMessage = "Card payment failed: " + e.getMessage();
            System.err.println(statusMessage);
            return false;
        }
    }
    
    @Override
    public String getPaymentType() {
        return "CARD";
    }
    
    @Override
    public boolean validatePaymentDetails() {
        // Validate card number (should be 16 digits)
        if (cardNumber == null || !cardNumber.matches("\\d{16}")) {
            return false;
        }
        
        // Validate card holder name
        if (cardHolderName == null || cardHolderName.trim().isEmpty()) {
            return false;
        }
        
        // Validate CVV (should be 3 digits)
        if (cvv == null || !cvv.matches("\\d{3}")) {
            return false;
        }
        
        // Validate expiry date (MM/YY format)
        if (expiryDate == null || !expiryDate.matches("(0[1-9]|1[0-2])/\\d{2}")) {
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
    
    /**
     * Mask card number for security (show only last 4 digits)
     */
    private String maskCardNumber() {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        return "**** **** **** " + cardNumber.substring(12);
    }
    
    // Getters
    public String getCardHolderName() {
        return cardHolderName;
    }
    
    public String getMaskedCardNumber() {
        return maskCardNumber();
    }
}
