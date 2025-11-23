package com.ecommerce.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Wishlist model class
 * Represents a product saved to user's wishlist
 * Demonstrates encapsulation
 */
public class Wishlist {
    
    // Private fields (Encapsulation)
    private Long id;
    private Long userId;
    private Long productId;
    private LocalDateTime addedAt;

    /**
     * Default constructor
     */
    public Wishlist() {
        this.addedAt = LocalDateTime.now();
    }

    /**
     * Parameterized constructor
     */
    public Wishlist(Long userId, Long productId) {
        this();
        this.userId = userId;
        this.productId = productId;
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

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wishlist wishlist = (Wishlist) o;
        return Objects.equals(id, wishlist.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Wishlist{" +
                "id=" + id +
                ", userId=" + userId +
                ", productId=" + productId +
                ", addedAt=" + addedAt +
                '}';
    }
}
