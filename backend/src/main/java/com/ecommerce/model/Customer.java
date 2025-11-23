package com.ecommerce.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Customer class extending User
 * Demonstrates OOP Inheritance - inherits all properties and methods from User
 * 
 * Represents a customer user with additional customer-specific fields
 */
public class Customer extends User {
    
    // Customer-specific fields (Encapsulation)
    private String phoneNumber;
    private String shippingAddress;
    private List<Order> orders;

    /**
     * Default constructor
     */
    public Customer() {
        super();
        this.orders = new ArrayList<>();
        setRole(UserRole.CUSTOMER);
    }

    /**
     * Parameterized constructor
     */
    public Customer(String email, String password, String firstName, String lastName) {
        super(email, password, firstName, lastName, UserRole.CUSTOMER);
        this.orders = new ArrayList<>();
    }

    /**
     * Full constructor with customer-specific fields
     */
    public Customer(String email, String password, String firstName, String lastName, 
                   String phoneNumber, String shippingAddress) {
        this(email, password, firstName, lastName);
        this.phoneNumber = phoneNumber;
        this.shippingAddress = shippingAddress;
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

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    /**
     * Add an order to customer's order history
     * @param order the order to add
     */
    public void addOrder(Order order) {
        if (this.orders == null) {
            this.orders = new ArrayList<>();
        }
        this.orders.add(order);
    }

    /**
     * Implementation of abstract method from User class
     * Demonstrates polymorphism - Customer-specific implementation
     * 
     * @return customer type display string
     */
    @Override
    public String getUserTypeDisplay() {
        return "Customer: " + getFullName() + " (" + getEmail() + ")";
    }

    /**
     * Implementation of abstract method from User class
     * Validates customer-specific data
     * 
     * @return true if customer data is valid
     */
    @Override
    public boolean validateUserData() {
        // Basic validation
        if (getEmail() == null || getEmail().trim().isEmpty()) {
            return false;
        }
        if (getFirstName() == null || getFirstName().trim().isEmpty()) {
            return false;
        }
        if (getLastName() == null || getLastName().trim().isEmpty()) {
            return false;
        }
        // Customer-specific validation
        if (phoneNumber != null && !phoneNumber.matches("^\\+?[0-9]{10,15}$")) {
            return false;
        }
        return true;
    }

    /**
     * Check if customer has any orders
     * @return true if customer has placed orders
     */
    public boolean hasOrders() {
        return orders != null && !orders.isEmpty();
    }

    /**
     * Get total number of orders
     * @return count of orders
     */
    public int getOrderCount() {
        return orders != null ? orders.size() : 0;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + getId() +
                ", email='" + getEmail() + '\'' +
                ", name='" + getFullName() + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", orderCount=" + getOrderCount() +
                '}';
    }
}
