package com.ecommerce.controller;

import com.ecommerce.service.IOrderService;
import com.ecommerce.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Admin Controller
 * Handles admin operations and analytics
 */
@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {
    
    @Autowired
    private IOrderService orderService;
    
    /**
     * Get dashboard analytics
     */
    @GetMapping("/analytics")
    public ResponseEntity<Map<String, Object>> getAnalytics() {
        Map<String, Object> analytics = new HashMap<>();
        
        List<Order> allOrders = orderService.getAllOrders();
        analytics.put("totalOrders", allOrders.size());
        analytics.put("recentOrders", allOrders.stream().limit(10).toList());
        
        return ResponseEntity.ok(analytics);
    }
    
    /**
     * Get all orders (admin view)
     */
    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }
}
