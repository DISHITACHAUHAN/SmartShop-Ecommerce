package com.ecommerce.exception;

/**
 * Exception thrown when payment processing fails
 * Extends SmartShopException
 */
public class PaymentFailedException extends SmartShopException {
    
    private String paymentMethod;
    private String transactionId;
    private String failureReason;
    
    /**
     * Constructor with payment method and message
     */
    public PaymentFailedException(String paymentMethod, String message) {
        super("PAYMENT_FAILED", "Payment via " + paymentMethod + " failed: " + message);
        this.paymentMethod = paymentMethod;
        this.failureReason = message;
    }
    
    /**
     * Constructor with payment method, transaction ID, and message
     */
    public PaymentFailedException(String paymentMethod, String transactionId, String message) {
        super("PAYMENT_FAILED", 
              "Payment via " + paymentMethod + " failed (Transaction: " + transactionId + "): " + message);
        this.paymentMethod = paymentMethod;
        this.transactionId = transactionId;
        this.failureReason = message;
    }
    
    /**
     * Constructor with payment method and cause
     */
    public PaymentFailedException(String paymentMethod, String message, Throwable cause) {
        super("PAYMENT_FAILED", "Payment via " + paymentMethod + " failed: " + message, cause);
        this.paymentMethod = paymentMethod;
        this.failureReason = message;
    }
    
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public String getTransactionId() {
        return transactionId;
    }
    
    public String getFailureReason() {
        return failureReason;
    }
}
