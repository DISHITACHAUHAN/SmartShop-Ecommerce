package com.ecommerce.exception;

public class InsufficientStockException extends ECommerceException {
    public InsufficientStockException(String message) {
        super(message, ErrorCode.INSUFFICIENT_STOCK);
    }
}
