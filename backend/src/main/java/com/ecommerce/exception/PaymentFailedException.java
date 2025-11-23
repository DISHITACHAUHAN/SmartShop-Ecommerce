package com.ecommerce.exception;

public class PaymentFailedException extends ECommerceException {
    public PaymentFailedException(String message) {
        super(message, ErrorCode.PAYMENT_FAILED);
    }
    
    public PaymentFailedException(String message, Throwable cause) {
        super(message, cause, ErrorCode.PAYMENT_FAILED);
    }
}
