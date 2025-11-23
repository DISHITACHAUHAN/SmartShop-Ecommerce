package com.ecommerce.service;

import com.ecommerce.dto.*;

/**
 * User Service Interface
 * Demonstrates abstraction - defines contract for user operations
 */
public interface IUserService {
    
    /**
     * Register a new user
     * @param request registration details
     * @return created user DTO
     */
    UserDTO register(RegisterRequest request);
    
    /**
     * Authenticate user and generate JWT token
     * @param request login credentials
     * @return login response with token and user info
     */
    LoginResponse login(LoginRequest request);
    
    /**
     * Get user profile by ID
     * @param userId user ID
     * @return user DTO
     */
    UserDTO getUserProfile(Long userId);
    
    /**
     * Update user profile
     * @param userId user ID
     * @param request update details
     * @return updated user DTO
     */
    UserDTO updateProfile(Long userId, RegisterRequest request);
    
    /**
     * Check if email exists
     * @param email email to check
     * @return true if exists
     */
    boolean emailExists(String email);
}
