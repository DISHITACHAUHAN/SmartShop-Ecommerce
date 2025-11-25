package com.ecommerce.model;

/**
 * Customer class extending User
 * Demonstrates Inheritance and Polymorphism
 * 
 * Represents a customer user with additional customer-specific properties
 */
public class Customer extends User {
    
    private String phoneNumber;
    private String shippingAddress;
    private String billingAddress;
    
    /**
     * Default constructor
     */
    public Customer() {
        super();
        setRole(UserRole.CUSTOMER);
    }
    
    /**
     * Parameterized constructor
     */
    public Customer(String email, String password, String firstName, String lastName) {
        super(email, password, firstName, lastName, UserRole.CUSTOMER);
    }
    
    /**
     * Full constructor
     */
    public Customer(String email, String password, String firstName, String lastName,
                   String phoneNumber, String shippingAddress) {
        this(email, password, firstName, lastName);
        this.phoneNumber = phoneNumber;
        this.shippingAddress = shippingAddress;
        this.billingAddress = shippingAddress; // Default billing same as shipping
    }
    
    // Implementing abstract method (Polymorphism)
    @Override
    public String getUserTypeDisplay() {
        return "Customer: " + getFullName() + " (" + getEmail() + ")";
    }
    
    // Implementing abstract method
    @Override
    public boolean validateUserData() {
        // Customer-specific validation
        if (getEmail() == null || getEmail().trim().isEmpty()) {
            return false;
        }
        if (getFirstName() == null || getFirstName().trim().isEmpty()) {
            return false;
        }
        if (getLastName() == null || getLastName().trim().isEmpty()) {
            return false;
        }
        // Phone and address are optional for basic validation
        return true;
    }
    
    /**
     * Check if customer has complete profile
     * @return true if all required fields are filled
     */
    public boolean hasCompleteProfile() {
        return validateUserData() && 
               phoneNumber != null && !phoneNumber.trim().isEmpty() &&
               shippingAddress != null && !shippingAddress.trim().isEmpty();
    }
    
    /**
     * Check if customer can place order
     * @return true if customer has shipping address
     */
    public boolean canPlaceOrder() {
        return shippingAddress != null && !shippingAddress.trim().isEmpty();
    }
    
    // Getters and Setters
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getShippingAddress() {
        return shippingAddress;
    }
    
    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
    
    public String getBillingAddress() {
        return billingAddress;
    }
    
    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }
    
    @Override
    public String toString() {
        return "Customer{" +
                "id=" + getId() +
                ", email='" + getEmail() + '\'' +
                ", name='" + getFullName() + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", shippingAddress='" + shippingAddress + '\'' +
                '}';
    }
}
