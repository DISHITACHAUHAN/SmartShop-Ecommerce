package com.ecommerce.exception;

/**
 * Exception thrown when a database transaction fails
 * Demonstrates custom exception hierarchy
 */
public class TransactionException extends ECommerceException {
    
    public TransactionException(String message) {
        super(message, ErrorCode.TRANSACTION_FAILED);
    }
    
    public TransactionException(String message, Throwable cause) {
        super(message, cause, ErrorCode.TRANSACTION_FAILED);
    }
}
