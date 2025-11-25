package com.ecommerce.exception;

/**
 * Exception thrown when product validation fails or product not found
 * Extends SmartShopException
 */
public class InvalidProductException extends SmartShopException {
    
    private Long productId;
    
    /**
     * Constructor with message
     */
    public InvalidProductException(String message) {
        super("INVALID_PRODUCT", message);
    }
    
    /**
     * Constructor with product ID
     */
    public InvalidProductException(Long productId) {
        super("INVALID_PRODUCT", "Product with ID " + productId + " not found or invalid");
        this.productId = productId;
    }
    
    /**
     * Constructor with product ID and custom message
     */
    public InvalidProductException(Long productId, String message) {
        super("INVALID_PRODUCT", message);
        this.productId = productId;
    }
    
    public Long getProductId() {
        return productId;
    }
}
