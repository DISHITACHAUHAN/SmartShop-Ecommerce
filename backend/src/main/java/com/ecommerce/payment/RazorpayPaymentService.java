package com.ecommerce.payment;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Razorpay Payment Service Implementation
 * Demonstrates polymorphism - implements PaymentGateway interface
 */
@Service("razorpayPaymentService")
public class RazorpayPaymentService implements PaymentGateway {
    
    private static final Logger logger = LoggerFactory.getLogger(RazorpayPaymentService.class);
    
    @Value("${razorpay.key.id:test_key}")
    private String keyId;
    
    @Value("${razorpay.key.secret:test_secret}")
    private String keySecret;
    
    private RazorpayClient razorpayClient;
    
    @Override
    public PaymentResponse createPaymentOrder(BigDecimal amount, String currency, Map<String, String> metadata) {
        try {
            logger.info("Creating Razorpay payment order for amount: {} {}", amount, currency);
            
            // Initialize client if not already done
            if (razorpayClient == null) {
                razorpayClient = new RazorpayClient(keyId, keySecret);
            }
            
            // Create order request
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amount.multiply(BigDecimal.valueOf(100)).intValue()); // Amount in paise
            orderRequest.put("currency", currency);
            orderRequest.put("receipt", metadata.getOrDefault("orderId", "order_" + System.currentTimeMillis()));
            
            // Create order
            com.razorpay.Order order = razorpayClient.orders.create(orderRequest);
            
            PaymentResponse response = new PaymentResponse(true, "Payment order created successfully");
            response.setOrderId(order.get("id"));
            response.setAmount(amount);
            response.setCurrency(currency);
            response.setStatus("created");
            
            logger.info("Razorpay order created: {}", (Object) order.get("id"));
            return response;
            
        } catch (RazorpayException e) {
            logger.error("Error creating Razorpay order", e);
            return new PaymentResponse(false, "Failed to create payment order: " + e.getMessage());
        }
    }
    
    @Override
    public PaymentResponse verifyPayment(String paymentId, String signature) {
        try {
            logger.info("Verifying Razorpay payment: {}", paymentId);
            
            // In production, verify signature using Razorpay utility
            // For now, simulate verification
            PaymentResponse response = new PaymentResponse(true, "Payment verified successfully");
            response.setPaymentId(paymentId);
            response.setStatus("verified");
            
            logger.info("Razorpay payment verified: {}", (Object) paymentId);
            return response;
            
        } catch (Exception e) {
            logger.error("Error verifying Razorpay payment", e);
            return new PaymentResponse(false, "Payment verification failed: " + e.getMessage());
        }
    }
    
    @Override
    public RefundResponse initiateRefund(String paymentId, BigDecimal amount) {
        try {
            logger.info("Initiating Razorpay refund for payment: {}", paymentId);
            
            if (razorpayClient == null) {
                razorpayClient = new RazorpayClient(keyId, keySecret);
            }
            
            JSONObject refundRequest = new JSONObject();
            refundRequest.put("amount", amount.multiply(BigDecimal.valueOf(100)).intValue());
            
            // Create refund
            com.razorpay.Refund refund = razorpayClient.payments.refund(paymentId, refundRequest);
            
            RefundResponse response = new RefundResponse(true, "Refund initiated successfully");
            response.setRefundId(refund.get("id"));
            response.setPaymentId(paymentId);
            response.setAmount(amount);
            response.setStatus("processed");
            
            logger.info("Razorpay refund created: {}", (Object) refund.get("id"));
            return response;
            
        } catch (RazorpayException e) {
            logger.error("Error initiating Razorpay refund", e);
            return new RefundResponse(false, "Refund failed: " + e.getMessage());
        }
    }
    
    @Override
    public String getGatewayName() {
        return "Razorpay";
    }
}
