package com.ecommerce.exception;

/**
 * Enum representing error codes for exceptions
 */
public enum ErrorCode {
    // User errors
    USER_NOT_FOUND("USER_001", "User not found"),
    USER_ALREADY_EXISTS("USER_002", "User already exists"),
    INVALID_CREDENTIALS("USER_003", "Invalid credentials"),
    UNAUTHORIZED("USER_004", "Unauthorized access"),
    
    // Product errors
    PRODUCT_NOT_FOUND("PROD_001", "Product not found"),
    INSUFFICIENT_STOCK("PROD_002", "Insufficient stock"),
    
    // Order errors
    ORDER_NOT_FOUND("ORD_001", "Order not found"),
    ORDER_CREATION_FAILED("ORD_002", "Order creation failed"),
    
    // Payment errors
    PAYMENT_FAILED("PAY_001", "Payment processing failed"),
    PAYMENT_VERIFICATION_FAILED("PAY_002", "Payment verification failed"),
    
    // Database errors
    TRANSACTION_FAILED("DB_001", "Database transaction failed"),
    DATABASE_ERROR("DB_002", "Database operation failed"),
    
    // General errors
    VALIDATION_ERROR("GEN_001", "Validation error"),
    INTERNAL_ERROR("GEN_002", "Internal server error");
    
    private final String code;
    private final String message;
    
    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getMessage() {
        return message;
    }
}
