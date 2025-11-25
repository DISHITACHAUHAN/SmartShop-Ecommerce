package com.ecommerce.payment;

import java.math.BigDecimal;

/**
 * Payment Processor
 * Demonstrates Polymorphism in action
 * 
 * This class can work with any PaymentStrategy implementation,
 * showing how polymorphism allows flexible payment processing
 */
public class PaymentProcessor {
    
    /**
     * Process payment using any payment strategy
     * Demonstrates polymorphism - same method works with different implementations
     * 
     * @param paymentStrategy the payment strategy to use
     * @param amount amount to process
     * @return true if payment successful
     */
    public boolean processPayment(PaymentStrategy paymentStrategy, BigDecimal amount) {
        System.out.println("\n=== Payment Processing Started ===");
        System.out.println("Payment Method: " + paymentStrategy.getPaymentType());
        System.out.println("Amount: ₹" + amount);
        System.out.println("================================");
        
        // Validate payment details first
        if (!paymentStrategy.validatePaymentDetails()) {
            System.err.println("✗ Payment validation failed!");
            return false;
        }
        
        // Process payment
        boolean success = paymentStrategy.processPayment(amount);
        
        if (success) {
            System.out.println("Transaction ID: " + paymentStrategy.getTransactionId());
            System.out.println("Status: " + paymentStrategy.getStatusMessage());
            System.out.println("=== Payment Completed Successfully ===\n");
        } else {
            System.err.println("✗ Payment Failed!");
            System.err.println("Reason: " + paymentStrategy.getStatusMessage());
            System.out.println("=== Payment Failed ===\n");
        }
        
        return success;
    }
    
    /**
     * Process payment with retry logic
     * 
     * @param paymentStrategy the payment strategy
     * @param amount amount to process
     * @param maxRetries maximum number of retries
     * @return true if payment successful
     */
    public boolean processPaymentWithRetry(PaymentStrategy paymentStrategy, 
                                          BigDecimal amount, 
                                          int maxRetries) {
        int attempts = 0;
        
        while (attempts < maxRetries) {
            attempts++;
            System.out.println("Payment attempt " + attempts + " of " + maxRetries);
            
            boolean success = processPayment(paymentStrategy, amount);
            
            if (success) {
                return true;
            }
            
            if (attempts < maxRetries) {
                System.out.println("Retrying payment...\n");
                try {
                    Thread.sleep(1000); // Wait 1 second before retry
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
        
        System.err.println("Payment failed after " + maxRetries + " attempts");
        return false;
    }
    
    /**
     * Example usage demonstrating polymorphism
     */
    public static void main(String[] args) {
        PaymentProcessor processor = new PaymentProcessor();
        BigDecimal amount = new BigDecimal("1999.99");
        
        System.out.println("=== Demonstrating Payment Polymorphism ===\n");
        
        // Example 1: Card Payment
        PaymentStrategy cardPayment = new CardPayment(
            "1234567890123456",
            "John Doe",
            "12/25",
            "123"
        );
        processor.processPayment(cardPayment, amount);
        
        // Example 2: UPI Payment
        PaymentStrategy upiPayment = new UPIPayment("john@upi", "1234");
        processor.processPayment(upiPayment, amount);
        
        // Example 3: COD Payment
        PaymentStrategy codPayment = new CODPayment(
            "123 Main Street, City, State 12345",
            "9876543210"
        );
        processor.processPayment(codPayment, amount);
        
        System.out.println("=== All payment methods processed using same interface! ===");
        System.out.println("This demonstrates POLYMORPHISM in action.");
    }
}
