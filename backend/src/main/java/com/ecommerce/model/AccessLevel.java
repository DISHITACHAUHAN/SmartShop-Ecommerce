package com.ecommerce.model;

/**
 * Enum representing admin access levels
 */
public enum AccessLevel {
    FULL("Full Access"),
    LIMITED("Limited Access");

    private final String displayName;

    AccessLevel(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
