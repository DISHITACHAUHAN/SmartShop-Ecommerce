package com.ecommerce.service.impl;

import com.ecommerce.dao.UserDAO;
import com.ecommerce.dto.*;
import com.ecommerce.exception.UserNotFoundException;
import com.ecommerce.model.Customer;
import com.ecommerce.model.User;
import com.ecommerce.model.UserRole;
import com.ecommerce.service.IUserService;
import com.ecommerce.util.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

/**
 * User Service Implementation
 * Demonstrates polymorphism - implements IUserService interface
 * Handles user authentication and registration with JWT
 */
@Service
public class UserServiceImpl implements IUserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    
    @Autowired
    private UserDAO userDAO;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    
    @Override
    public UserDTO register(RegisterRequest request) {
        try {
            // Check if email already exists
            if (userDAO.existsByEmail(request.getEmail())) {
                throw new IllegalArgumentException("Email already exists");
            }
            
            // Create new customer
            Customer customer = new Customer();
            customer.setEmail(request.getEmail());
            customer.setPassword(passwordEncoder.encode(request.getPassword()));
            customer.setFirstName(request.getFirstName());
            customer.setLastName(request.getLastName());
            customer.setPhoneNumber(request.getPhoneNumber());
            customer.setShippingAddress(request.getShippingAddress());
            customer.setRole(UserRole.CUSTOMER);
            
            // Save to database
            User savedUser = userDAO.save(customer);
            
            logger.info("User registered successfully: {}", savedUser.getEmail());
            
            return convertToDTO(savedUser);
            
        } catch (SQLException e) {
            logger.error("Error registering user", e);
            throw new RuntimeException("Registration failed: " + e.getMessage());
        }
    }
    
    @Override
    public LoginResponse login(LoginRequest request) {
        try {
            // Find user by email
            User user = userDAO.findByEmail(request.getEmail());
            
            if (user == null) {
                throw new UserNotFoundException("Invalid email or password");
            }
            
            // Verify password
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new UserNotFoundException("Invalid email or password");
            }
            
            // Generate JWT token
            String token = jwtTokenProvider.generateToken(
                user.getEmail(),
                user.getRole().name()
            );
            
            logger.info("User logged in successfully: {}", user.getEmail());
            
            return new LoginResponse(token, convertToDTO(user));
            
        } catch (SQLException e) {
            logger.error("Error during login", e);
            throw new RuntimeException("Login failed: " + e.getMessage());
        }
    }
    
    @Override
    public UserDTO getUserProfile(Long userId) {
        try {
            User user = userDAO.findById(userId);
            
            if (user == null) {
                throw new UserNotFoundException("User not found with ID: " + userId);
            }
            
            return convertToDTO(user);
            
        } catch (SQLException e) {
            logger.error("Error fetching user profile", e);
            throw new RuntimeException("Failed to fetch user profile: " + e.getMessage());
        }
    }
    
    @Override
    public UserDTO updateProfile(Long userId, RegisterRequest request) {
        try {
            User user = userDAO.findById(userId);
            
            if (user == null) {
                throw new UserNotFoundException("User not found with ID: " + userId);
            }
            
            // Update user fields
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setEmail(request.getEmail());
            
            if (user instanceof Customer) {
                Customer customer = (Customer) user;
                customer.setPhoneNumber(request.getPhoneNumber());
                customer.setShippingAddress(request.getShippingAddress());
            }
            
            // Save updates
            User updatedUser = userDAO.update(user);
            
            logger.info("User profile updated: {}", userId);
            
            return convertToDTO(updatedUser);
            
        } catch (SQLException e) {
            logger.error("Error updating user profile", e);
            throw new RuntimeException("Failed to update profile: " + e.getMessage());
        }
    }
    
    @Override
    public boolean emailExists(String email) {
        try {
            return userDAO.existsByEmail(email);
        } catch (SQLException e) {
            logger.error("Error checking email existence", e);
            return false;
        }
    }
    
    /**
     * Convert User entity to UserDTO
     * Demonstrates encapsulation - hides password and internal details
     */
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setRole(user.getRole());
        dto.setCreatedAt(user.getCreatedAt());
        
        // Add customer-specific fields if applicable
        if (user instanceof Customer) {
            Customer customer = (Customer) user;
            dto.setPhoneNumber(customer.getPhoneNumber());
            dto.setShippingAddress(customer.getShippingAddress());
        }
        
        return dto;
    }
}
