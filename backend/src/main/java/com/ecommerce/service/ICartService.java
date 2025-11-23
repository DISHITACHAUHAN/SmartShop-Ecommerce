package com.ecommerce.service;

import com.ecommerce.model.CartItem;
import java.util.List;

/**
 * Cart Service Interface
 * Demonstrates abstraction - defines contract for cart operations
 */
public interface ICartService {
    
    /**
     * Add item to cart
     * @param userId user ID
     * @param productId product ID
     * @param quantity quantity to add
     * @return cart item
     */
    CartItem addToCart(Long userId, Long productId, Integer quantity);
    
    /**
     * Update cart item quantity
     * @param userId user ID
     * @param itemId cart item ID
     * @param quantity new quantity
     * @return updated cart item
     */
    CartItem updateCartItem(Long userId, Long itemId, Integer quantity);
    
    /**
     * Remove item from cart
     * @param userId user ID
     * @param itemId cart item ID
     */
    void removeFromCart(Long userId, Long itemId);
    
    /**
     * Get user's cart
     * @param userId user ID
     * @return list of cart items
     */
    List<CartItem> getCart(Long userId);
    
    /**
     * Clear user's cart
     * @param userId user ID
     */
    void clearCart(Long userId);
    
    /**
     * Get cart total amount
     * @param userId user ID
     * @return total amount
     */
    java.math.BigDecimal getCartTotal(Long userId);
}
