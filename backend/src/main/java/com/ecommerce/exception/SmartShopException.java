package com.ecommerce.exception;

/**
 * Base exception class for SmartShop application
 * All custom exceptions extend this class
 * Demonstrates Exception Handling and Inheritance
 */
public class SmartShopException extends Exception {
    
    private String errorCode;
    private String userMessage;
    
    /**
     * Constructor with message
     */
    public SmartShopException(String message) {
        super(message);
        this.userMessage = message;
    }
    
    /**
     * Constructor with message and cause
     */
    public SmartShopException(String message, Throwable cause) {
        super(message, cause);
        this.userMessage = message;
    }
    
    /**
     * Constructor with error code and message
     */
    public SmartShopException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.userMessage = message;
    }
    
    /**
     * Constructor with error code, message, and cause
     */
    public SmartShopException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.userMessage = message;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public String getErrorCodeString() {
        return errorCode != null ? errorCode : "UNKNOWN_ERROR";
    }
    
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    
    public String getUserMessage() {
        return userMessage;
    }
    
    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }
    
    @Override
    public String toString() {
        return "SmartShopException{" +
                "errorCode='" + errorCode + '\'' +
                ", message='" + getMessage() + '\'' +
                '}';
    }
}
