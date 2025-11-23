package com.ecommerce.payment;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.RefundCreateParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Stripe Payment Service Implementation
 * Demonstrates polymorphism - implements PaymentGateway interface
 */
@Service("stripePaymentService")
public class StripePaymentService implements PaymentGateway {
    
    private static final Logger logger = LoggerFactory.getLogger(StripePaymentService.class);
    
    @Value("${stripe.api.key:test_key}")
    private String apiKey;
    
    @PostConstruct
    public void init() {
        Stripe.apiKey = apiKey;
        logger.info("Stripe Payment Service initialized");
    }
    
    @Override
    public PaymentResponse createPaymentOrder(BigDecimal amount, String currency, Map<String, String> metadata) {
        try {
            logger.info("Creating Stripe payment intent for amount: {} {}", amount, currency);
            
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amount.multiply(BigDecimal.valueOf(100)).longValue()) // Amount in cents
                .setCurrency(currency.toLowerCase())
                .putMetadata("orderId", metadata.getOrDefault("orderId", ""))
                .setAutomaticPaymentMethods(
                    PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                        .setEnabled(true)
                        .build()
                )
                .build();
            
            PaymentIntent intent = PaymentIntent.create(params);
            
            PaymentResponse response = new PaymentResponse(true, "Payment intent created successfully");
            response.setPaymentId(intent.getId());
            response.setAmount(amount);
            response.setCurrency(currency);
            response.setStatus(intent.getStatus());
            
            logger.info("Stripe payment intent created: {}", intent.getId());
            return response;
            
        } catch (StripeException e) {
            logger.error("Error creating Stripe payment intent", e);
            return new PaymentResponse(false, "Failed to create payment: " + e.getMessage());
        }
    }
    
    @Override
    public PaymentResponse verifyPayment(String paymentId, String signature) {
        try {
            logger.info("Verifying Stripe payment: {}", paymentId);
            
            PaymentIntent intent = PaymentIntent.retrieve(paymentId);
            
            PaymentResponse response = new PaymentResponse(true, "Payment verified successfully");
            response.setPaymentId(paymentId);
            response.setStatus(intent.getStatus());
            response.setSuccess("succeeded".equals(intent.getStatus()));
            
            logger.info("Stripe payment verified: {} - status: {}", paymentId, intent.getStatus());
            return response;
            
        } catch (StripeException e) {
            logger.error("Error verifying Stripe payment", e);
            return new PaymentResponse(false, "Payment verification failed: " + e.getMessage());
        }
    }
    
    @Override
    public RefundResponse initiateRefund(String paymentId, BigDecimal amount) {
        try {
            logger.info("Initiating Stripe refund for payment: {}", paymentId);
            
            RefundCreateParams params = RefundCreateParams.builder()
                .setPaymentIntent(paymentId)
                .setAmount(amount.multiply(BigDecimal.valueOf(100)).longValue())
                .build();
            
            Refund refund = Refund.create(params);
            
            RefundResponse response = new RefundResponse(true, "Refund initiated successfully");
            response.setRefundId(refund.getId());
            response.setPaymentId(paymentId);
            response.setAmount(amount);
            response.setStatus(refund.getStatus());
            
            logger.info("Stripe refund created: {}", refund.getId());
            return response;
            
        } catch (StripeException e) {
            logger.error("Error initiating Stripe refund", e);
            return new RefundResponse(false, "Refund failed: " + e.getMessage());
        }
    }
    
    @Override
    public String getGatewayName() {
        return "Stripe";
    }
}
