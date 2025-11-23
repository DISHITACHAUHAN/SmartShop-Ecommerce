package com.ecommerce.exception;

public class ProductNotFoundException extends ECommerceException {
    public ProductNotFoundException(String message) {
        super(message, ErrorCode.PRODUCT_NOT_FOUND);
    }
}
