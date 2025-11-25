package com.ecommerce.model;

/**
 * Admin class extending User
 * Demonstrates Inheritance and Polymorphism
 * 
 * Represents an administrator user with additional admin-specific properties
 */
public class Admin extends User {
    
    private String department;
    private AccessLevel accessLevel;
    
    /**
     * Default constructor
     */
    public Admin() {
        super();
        setRole(UserRole.ADMIN);
        this.accessLevel = AccessLevel.FULL;
    }
    
    /**
     * Parameterized constructor
     */
    public Admin(String email, String password, String firstName, String lastName) {
        super(email, password, firstName, lastName, UserRole.ADMIN);
        this.accessLevel = AccessLevel.FULL;
    }
    
    /**
     * Full constructor
     */
    public Admin(String email, String password, String firstName, String lastName,
                String department, AccessLevel accessLevel) {
        this(email, password, firstName, lastName);
        this.department = department;
        this.accessLevel = accessLevel;
    }
    
    // Implementing abstract method (Polymorphism)
    @Override
    public String getUserTypeDisplay() {
        return "Admin: " + getFullName() + " [" + department + "] - " + accessLevel;
    }
    
    // Implementing abstract method
    @Override
    public boolean validateUserData() {
        // Admin-specific validation
        if (getEmail() == null || getEmail().trim().isEmpty()) {
            return false;
        }
        if (getFirstName() == null || getFirstName().trim().isEmpty()) {
            return false;
        }
        if (getLastName() == null || getLastName().trim().isEmpty()) {
            return false;
        }
        if (accessLevel == null) {
            return false;
        }
        return true;
    }
    
    /**
     * Check if admin has permission for an action
     * @param permission the permission to check
     * @return true if admin has permission
     */
    public boolean hasPermission(String permission) {
        if (accessLevel == AccessLevel.FULL) {
            return true; // Full access to everything
        } else if (accessLevel == AccessLevel.LIMITED) {
            // Limited access - cannot delete users or modify critical settings
            return !permission.equals("DELETE_USER") && 
                   !permission.equals("MODIFY_SYSTEM_SETTINGS");
        } else {
            // Read-only access
            return permission.startsWith("VIEW_") || permission.startsWith("READ_");
        }
    }
    
    /**
     * Check if admin can manage products
     * @return true if admin has product management permission
     */
    public boolean canManageProducts() {
        return accessLevel == AccessLevel.FULL || accessLevel == AccessLevel.LIMITED;
    }
    
    /**
     * Check if admin can manage users
     * @return true if admin has user management permission
     */
    public boolean canManageUsers() {
        return accessLevel == AccessLevel.FULL;
    }
    
    /**
     * Check if admin can view reports
     * @return true if admin has report viewing permission
     */
    public boolean canViewReports() {
        return true; // All admins can view reports
    }
    
    // Getters and Setters
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public AccessLevel getAccessLevel() {
        return accessLevel;
    }
    
    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }
    
    @Override
    public String toString() {
        return "Admin{" +
                "id=" + getId() +
                ", email='" + getEmail() + '\'' +
                ", name='" + getFullName() + '\'' +
                ", department='" + department + '\'' +
                ", accessLevel=" + accessLevel +
                '}';
    }
}
