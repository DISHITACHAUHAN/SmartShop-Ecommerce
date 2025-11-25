package com.ecommerce.service.impl;

import com.ecommerce.dao.CartDAO;
import com.ecommerce.dao.ProductDAO;
import com.ecommerce.exception.InsufficientStockException;
import com.ecommerce.exception.ProductNotFoundException;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Product;
import com.ecommerce.service.ICartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Cart Service Implementation
 * Demonstrates thread-safe operations using ConcurrentHashMap
 */
@Service
public class CartServiceImpl implements ICartService {
    
    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);
    
    @Autowired
    private CartDAO cartDAO;
    
    @Autowired
    private ProductDAO productDAO;
    
    // Thread-safe in-memory cache for cart operations
    private final ConcurrentHashMap<Long, List<CartItem>> cartCache = new ConcurrentHashMap<>();
    
    @Override
    public CartItem addToCart(Long userId, Long productId, Integer quantity) {
        try {
            // Validate product exists and has sufficient stock
            Product product = productDAO.findById(productId);
            if (product == null) {
                throw new ProductNotFoundException("Product not found with ID: " + productId);
            }
            
            if (!product.hasSufficientStock(quantity)) {
                throw new InsufficientStockException(
                    product.getName(), quantity, product.getStockQuantity());
            }
            
            // Create cart item
            CartItem cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(productId);
            cartItem.setProductName(product.getName());
            cartItem.setQuantity(quantity);
            cartItem.setPrice(product.getPrice());
            cartItem.setImageUrl(product.getImageUrl());
            
            // Save to database
            CartItem saved = cartDAO.save(cartItem);
            
            // Invalidate cache for this user
            cartCache.remove(userId);
            
            logger.info("Item added to cart for user {}: product {}", userId, productId);
            return saved;
            
        } catch (SQLException e) {
            logger.error("Error adding to cart", e);
            throw new RuntimeException("Failed to add to cart: " + e.getMessage());
        }
    }
    
    @Override
    public CartItem updateCartItem(Long userId, Long itemId, Integer quantity) {
        try {
            CartItem cartItem = cartDAO.findById(itemId);
            if (cartItem == null || !cartItem.getUserId().equals(userId)) {
                throw new IllegalArgumentException("Cart item not found");
            }
            
            // Validate stock
            Product product = productDAO.findById(cartItem.getProductId());
            if (product != null && !product.hasSufficientStock(quantity)) {
                throw new InsufficientStockException(
                    product.getName(), quantity, product.getStockQuantity());
            }
            
            cartItem.setQuantity(quantity);
            CartItem updated = cartDAO.update(cartItem);
            
            // Invalidate cache
            cartCache.remove(userId);
            
            logger.info("Cart item updated for user {}: item {}", userId, itemId);
            return updated;
            
        } catch (SQLException e) {
            logger.error("Error updating cart item", e);
            throw new RuntimeException("Failed to update cart: " + e.getMessage());
        }
    }
    
    @Override
    public void removeFromCart(Long userId, Long itemId) {
        try {
            cartDAO.delete(itemId);
            cartCache.remove(userId);
            logger.info("Item removed from cart for user {}: item {}", userId, itemId);
        } catch (SQLException e) {
            logger.error("Error removing from cart", e);
            throw new RuntimeException("Failed to remove from cart: " + e.getMessage());
        }
    }
    
    @Override
    public List<CartItem> getCart(Long userId) {
        // Check cache first (thread-safe)
        return cartCache.computeIfAbsent(userId, id -> {
            try {
                List<CartItem> items = cartDAO.findByUserId(id);
                logger.info("Cart fetched for user {}: {} items", id, items.size());
                return items;
            } catch (SQLException e) {
                logger.error("Error fetching cart", e);
                throw new RuntimeException("Failed to fetch cart: " + e.getMessage());
            }
        });
    }
    
    @Override
    public void clearCart(Long userId) {
        try {
            cartDAO.deleteByUserId(userId);
            cartCache.remove(userId);
            logger.info("Cart cleared for user {}", userId);
        } catch (SQLException e) {
            logger.error("Error clearing cart", e);
            throw new RuntimeException("Failed to clear cart: " + e.getMessage());
        }
    }
    
    @Override
    public BigDecimal getCartTotal(Long userId) {
        List<CartItem> items = getCart(userId);
        return items.stream()
                .map(CartItem::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
