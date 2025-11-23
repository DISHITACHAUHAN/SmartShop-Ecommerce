package com.ecommerce.exception;

public class OrderNotFoundException extends ECommerceException {
    public OrderNotFoundException(String message) {
        super(message, ErrorCode.ORDER_NOT_FOUND);
    }
}
