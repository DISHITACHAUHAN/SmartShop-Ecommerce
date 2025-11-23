package com.ecommerce.controller;

import com.ecommerce.dto.*;
import com.ecommerce.service.IUserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication Controller
 * Handles user registration and login
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    @Autowired
    private IUserService userService;
    
    /**
     * Register a new user
     */
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody RegisterRequest request) {
        logger.info("Registration request for email: {}", request.getEmail());
        UserDTO user = userService.register(request);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
    
    /**
     * Login user and return JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        logger.info("Login request for email: {}", request.getEmail());
        LoginResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get current user profile
     */
    @GetMapping("/profile/{userId}")
    public ResponseEntity<UserDTO> getProfile(@PathVariable Long userId) {
        UserDTO user = userService.getUserProfile(userId);
        return ResponseEntity.ok(user);
    }
}
