package com.ecommerce.model;

/**
 * Admin class extending User
 * Demonstrates OOP Inheritance - inherits all properties and methods from User
 * 
 * Represents an administrator user with elevated privileges
 */
public class Admin extends User {
    
    // Admin-specific fields (Encapsulation)
    private String department;
    private AccessLevel accessLevel;

    /**
     * Default constructor
     */
    public Admin() {
        super();
        setRole(UserRole.ADMIN);
        this.accessLevel = AccessLevel.LIMITED;
    }

    /**
     * Parameterized constructor
     */
    public Admin(String email, String password, String firstName, String lastName) {
        super(email, password, firstName, lastName, UserRole.ADMIN);
        this.accessLevel = AccessLevel.LIMITED;
    }

    /**
     * Full constructor with admin-specific fields
     */
    public Admin(String email, String password, String firstName, String lastName,
                String department, AccessLevel accessLevel) {
        this(email, password, firstName, lastName);
        this.department = department;
        this.accessLevel = accessLevel;
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

    /**
     * Implementation of abstract method from User class
     * Demonstrates polymorphism - Admin-specific implementation
     * 
     * @return admin type display string
     */
    @Override
    public String getUserTypeDisplay() {
        return "Administrator: " + getFullName() + 
               " [" + department + " - " + accessLevel.getDisplayName() + "]";
    }

    /**
     * Implementation of abstract method from User class
     * Validates admin-specific data
     * 
     * @return true if admin data is valid
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
        // Admin-specific validation
        if (department == null || department.trim().isEmpty()) {
            return false;
        }
        if (accessLevel == null) {
            return false;
        }
        return true;
    }

    /**
     * Check if admin has full access
     * @return true if admin has full access level
     */
    public boolean hasFullAccess() {
        return AccessLevel.FULL.equals(accessLevel);
    }

    /**
     * Grant full access to admin
     */
    public void grantFullAccess() {
        this.accessLevel = AccessLevel.FULL;
    }

    /**
     * Restrict admin to limited access
     */
    public void restrictAccess() {
        this.accessLevel = AccessLevel.LIMITED;
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
