package com.ecommerce.payment;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Payment Gateway Interface
 * Demonstrates abstraction and polymorphism
 * Different payment providers implement this interface
 */
public interface PaymentGateway {
    
    /**
     * Create a payment order
     * 
     * @param amount payment amount
     * @param currency currency code (e.g., USD, INR)
     * @param metadata additional payment metadata
     * @return payment response with order details
     */
    PaymentResponse createPaymentOrder(BigDecimal amount, String currency, Map<String, String> metadata);
    
    /**
     * Verify payment signature/status
     * 
     * @param paymentId payment transaction ID
     * @param signature payment signature for verification
     * @return payment response with verification status
     */
    PaymentResponse verifyPayment(String paymentId, String signature);
    
    /**
     * Initiate refund for a payment
     * 
     * @param paymentId payment transaction ID
     * @param amount refund amount
     * @return refund response
     */
    RefundResponse initiateRefund(String paymentId, BigDecimal amount);
    
    /**
     * Get payment gateway name
     * 
     * @return gateway name
     */
    String getGatewayName();
}
