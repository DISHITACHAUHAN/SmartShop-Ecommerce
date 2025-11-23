package com.ecommerce.exception;

public class UserNotFoundException extends ECommerceException {
    public UserNotFoundException(String message) {
        super(message, ErrorCode.USER_NOT_FOUND);
    }
}
