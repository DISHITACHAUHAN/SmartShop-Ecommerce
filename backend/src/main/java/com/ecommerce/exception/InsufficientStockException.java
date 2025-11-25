package com.ecommerce.exception;

/**
 * Exception thrown when product stock is insufficient for order
 * Extends RuntimeException for unchecked exception handling
 */
public class InsufficientStockException extends RuntimeException {
    
    private Long productId;
    private String productName;
    private int requestedQuantity;
    private int availableQuantity;
    
    /**
     * Constructor with product details
     */
    public InsufficientStockException(Long productId, int requested, int available) {
        super(String.format("Insufficient stock for product %d. Requested: %d, Available: %d",
                           productId, requested, available));
        this.productId = productId;
        this.requestedQuantity = requested;
        this.availableQuantity = available;
    }
    
    /**
     * Constructor with product name
     */
    public InsufficientStockException(String productName, int requested, int available) {
        super(String.format("Insufficient stock for %s. Requested: %d, Available: %d",
                           productName, requested, available));
        this.productName = productName;
        this.requestedQuantity = requested;
        this.availableQuantity = available;
    }
    
    /**
     * Constructor with full details
     */
    public InsufficientStockException(Long productId, String productName, 
                                     int requested, int available) {
        super(String.format("Insufficient stock for %s (ID: %d). Requested: %d, Available: %d",
                           productName, productId, requested, available));
        this.productId = productId;
        this.productName = productName;
        this.requestedQuantity = requested;
        this.availableQuantity = available;
    }
    
    public String getErrorCode() {
        return "INSUFFICIENT_STOCK";
    }
    
    public String getErrorCodeString() {
        return "INSUFFICIENT_STOCK";
    }
    
    public Long getProductId() {
        return productId;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public int getRequestedQuantity() {
        return requestedQuantity;
    }
    
    public int getAvailableQuantity() {
        return availableQuantity;
    }
    
    public int getShortfall() {
        return requestedQuantity - availableQuantity;
    }
}
