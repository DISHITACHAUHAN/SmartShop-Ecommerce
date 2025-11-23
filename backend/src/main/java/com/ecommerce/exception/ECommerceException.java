package com.ecommerce.exception;

/**
 * Base exception class for all E-Commerce application exceptions
 * Demonstrates custom exception hierarchy and encapsulation
 */
public class ECommerceException extends RuntimeException {
    
    private final ErrorCode errorCode;
    
    public ECommerceException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public ECommerceException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    public ErrorCode getErrorCode() {
        return errorCode;
    }
    
    public String getErrorCodeString() {
        return errorCode.getCode();
    }
}
