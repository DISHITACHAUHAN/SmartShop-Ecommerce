package com.ecommerce.controller;

import com.ecommerce.model.CartItem;
import com.ecommerce.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Cart Controller
 * Handles shopping cart operations
 */
@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {
    
    @Autowired
    private ICartService cartService;
    
    @GetMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> getCart(@PathVariable Long userId) {
        List<CartItem> items = cartService.getCart(userId);
        BigDecimal total = cartService.getCartTotal(userId);
        
        Map<String, Object> response = Map.of(
            "items", items,
            "totalAmount", total
        );
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/items")
    public ResponseEntity<CartItem> addToCart(@RequestBody Map<String, Object> request) {
        Long userId = Long.valueOf(request.get("userId").toString());
        Long productId = Long.valueOf(request.get("productId").toString());
        Integer quantity = Integer.valueOf(request.get("quantity").toString());
        
        CartItem item = cartService.addToCart(userId, productId, quantity);
        return ResponseEntity.ok(item);
    }
    
    @PutMapping("/items/{itemId}")
    public ResponseEntity<CartItem> updateCartItem(
            @PathVariable Long itemId,
            @RequestBody Map<String, Object> request) {
        Long userId = Long.valueOf(request.get("userId").toString());
        Integer quantity = Integer.valueOf(request.get("quantity").toString());
        
        CartItem item = cartService.updateCartItem(userId, itemId, quantity);
        return ResponseEntity.ok(item);
    }
    
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> removeFromCart(
            @PathVariable Long itemId,
            @RequestParam Long userId) {
        cartService.removeFromCart(userId, itemId);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}
