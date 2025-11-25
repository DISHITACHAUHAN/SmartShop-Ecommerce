package com.ecommerce.payment;

import java.math.BigDecimal;

/**
 * Payment Strategy Interface
 * Demonstrates Polymorphism through Strategy Pattern
 * 
 * Different payment methods implement this interface,
 * allowing the system to work with any payment type uniformly
 */
public interface PaymentStrategy {
    
    /**
     * Process payment for the given amount
     * @param amount amount to be paid
     * @return true if payment successful, false otherwise
     */
    boolean processPayment(BigDecimal amount);
    
    /**
     * Get the payment type/method name
     * @return payment type (e.g., "CARD", "UPI", "COD")
     */
    String getPaymentType();
    
    /**
     * Validate payment details before processing
     * @return true if payment details are valid
     */
    boolean validatePaymentDetails();
    
    /**
     * Get transaction ID after successful payment
     * @return transaction ID or null if payment not processed
     */
    String getTransactionId();
    
    /**
     * Get payment status message
     * @return status message
     */
    String getStatusMessage();
}
