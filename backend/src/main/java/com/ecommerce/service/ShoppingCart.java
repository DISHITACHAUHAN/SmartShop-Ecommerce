package com.ecommerce.service;

import com.ecommerce.model.CartItem;
import com.ecommerce.model.Product;
import java.math.BigDecimal;
import java.util.*;

/**
 * Shopping Cart Service
 * Demonstrates Collections and Generics usage
 * 
 * Uses Map<Long, CartItem> for efficient cart management
 */
public class ShoppingCart {
    
    // Map to store cart items (productId -> CartItem)
    // Demonstrates Map collection and Generics
    private Map<Long, CartItem> items;
    
    // List to maintain insertion order
    // Demonstrates List collection
    private List<CartItem> itemsList;
    
    // Set to track unique product IDs
    // Demonstrates Set collection
    private Set<Long> productIds;
    
    /**
     * Constructor initializes all collections
     */
    public ShoppingCart() {
        this.items = new HashMap<>();
        this.itemsList = new ArrayList<>();
        this.productIds = new HashSet<>();
    }
    
    /**
     * Add product to cart
     * Demonstrates Map operations and generics
     * 
     * @param product product to add
     * @param quantity quantity to add
     */
    public void addItem(Product product, int quantity) {
        if (product == null || quantity <= 0) {
            throw new IllegalArgumentException("Invalid product or quantity");
        }
        
        Long productId = product.getId();
        
        if (items.containsKey(productId)) {
            // Update existing item
            CartItem existingItem = items.get(productId);
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            System.out.println("Updated cart: " + product.getName() + " quantity to " + existingItem.getQuantity());
        } else {
            // Add new item
            CartItem newItem = new CartItem(
                productId,
                product.getName(),
                product.getPrice(),
                quantity,
                product.getImageUrl()
            );
            items.put(productId, newItem);
            itemsList.add(newItem);
            productIds.add(productId);
            System.out.println("Added to cart: " + product.getName() + " x " + quantity);
        }
    }
    
    /**
     * Remove item from cart
     * 
     * @param productId product ID to remove
     * @return true if item was removed
     */
    public boolean removeItem(Long productId) {
        CartItem removed = items.remove(productId);
        if (removed != null) {
            itemsList.remove(removed);
            productIds.remove(productId);
            System.out.println("Removed from cart: " + removed.getProductName());
            return true;
        }
        return false;
    }
    
    /**
     * Update item quantity
     * 
     * @param productId product ID
     * @param newQuantity new quantity
     */
    public void updateQuantity(Long productId, int newQuantity) {
        if (newQuantity <= 0) {
            removeItem(productId);
            return;
        }
        
        CartItem item = items.get(productId);
        if (item != null) {
            item.setQuantity(newQuantity);
            System.out.println("Updated quantity for " + item.getProductName() + " to " + newQuantity);
        }
    }
    
    /**
     * Get all items as List
     * Demonstrates returning generic collection
     * 
     * @return list of cart items
     */
    public List<CartItem> getItems() {
        return new ArrayList<>(itemsList); // Return defensive copy
    }
    
    /**
     * Get item by product ID
     * 
     * @param productId product ID
     * @return cart item or null
     */
    public CartItem getItem(Long productId) {
        return items.get(productId);
    }
    
    /**
     * Check if cart contains product
     * Demonstrates Set usage
     * 
     * @param productId product ID
     * @return true if product is in cart
     */
    public boolean containsProduct(Long productId) {
        return productIds.contains(productId);
    }
    
    /**
     * Get total number of items (sum of quantities)
     * Demonstrates Stream API with collections
     * 
     * @return total item count
     */
    public int getTotalItems() {
        return items.values().stream()
                    .mapToInt(CartItem::getQuantity)
                    .sum();
    }
    
    /**
     * Calculate cart total amount
     * Demonstrates Stream API and functional programming
     * 
     * @return total cart value
     */
    public BigDecimal getTotal() {
        return items.values().stream()
                    .map(CartItem::getTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    /**
     * Get cart size (number of unique products)
     * 
     * @return number of unique products
     */
    public int size() {
        return items.size();
    }
    
    /**
     * Check if cart is empty
     * 
     * @return true if cart has no items
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }
    
    /**
     * Clear all items from cart
     */
    public void clear() {
        items.clear();
        itemsList.clear();
        productIds.clear();
        System.out.println("Cart cleared");
    }
    
    /**
     * Get items sorted by name
     * Demonstrates Comparator and sorting
     * 
     * @return sorted list of items
     */
    public List<CartItem> getItemsSortedByName() {
        List<CartItem> sorted = new ArrayList<>(itemsList);
        sorted.sort(Comparator.comparing(CartItem::getProductName));
        return sorted;
    }
    
    /**
     * Get items sorted by price
     * 
     * @return sorted list of items
     */
    public List<CartItem> getItemsSortedByPrice() {
        List<CartItem> sorted = new ArrayList<>(itemsList);
        sorted.sort(Comparator.comparing(CartItem::getPrice));
        return sorted;
    }
    
    /**
     * Get cart summary
     * 
     * @return map with cart statistics
     */
    public Map<String, Object> getSummary() {
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalItems", getTotalItems());
        summary.put("uniqueProducts", size());
        summary.put("totalAmount", getTotal());
        summary.put("isEmpty", isEmpty());
        return summary;
    }
    
    @Override
    public String toString() {
        return "ShoppingCart{" +
                "items=" + size() +
                ", totalQuantity=" + getTotalItems() +
                ", total=" + getTotal() +
                '}';
    }
}
