package com.ecommerce.model;

/**
 * Enum representing user roles in the system
 * Demonstrates encapsulation of role types
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
