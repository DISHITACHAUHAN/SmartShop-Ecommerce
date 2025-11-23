package com.ecommerce.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * CartItem model class
 * Represents an item in a user's shopping cart
 * Demonstrates encapsulation
 */
public class CartItem {
    
    // Private fields (Encapsulation)
    private Long id;
    private Long userId;
    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
    private String imageUrl;
    private LocalDateTime createdAt;

    /**
     * Default constructor
     */
    public CartItem() {
        this.createdAt = LocalDateTime.now();
        this.quantity = 1;
    }

    /**
     * Parameterized constructor
     */
    public CartItem(Long userId, Long productId, String productName, 
                   Integer quantity, BigDecimal price) {
        this();
        this.userId = userId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters and Setters
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Business methods
    
    /**
     * Calculate total price for this cart item
     * @return total price (price * quantity)
     */
    public BigDecimal getTotal() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    /**
     * Increase quantity by specified amount
     * @param amount amount to increase
     */
    public void increaseQuantity(int amount) {
        this.quantity += amount;
    }

    /**
     * Decrease quantity by specified amount
     * @param amount amount to decrease
     * @return true if quantity was decreased, false if it would go below 1
     */
    public boolean decreaseQuantity(int amount) {
        if (this.quantity - amount >= 1) {
            this.quantity -= amount;
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        return Objects.equals(id, cartItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", total=" + getTotal() +
                '}';
    }
}
