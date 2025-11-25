package com.ecommerce.model;

/**
 * User Role Enum
 * Defines different user types in the system
 */
public enum UserRole {
    CUSTOMER("Customer"),
    ADMIN("Administrator");
    
    private final String displayName;
    
    UserRole(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
