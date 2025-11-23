package com.ecommerce.exception;

public class UnauthorizedException extends ECommerceException {
    public UnauthorizedException(String message) {
        super(message, ErrorCode.UNAUTHORIZED);
    }
}
