package com.ecommerce.servlet;

import com.ecommerce.dao.impl.UserDAOImpl;
import com.ecommerce.model.Customer;
import com.ecommerce.model.UserRole;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Register Servlet
 * Demonstrates complete servlet lifecycle and form handling
 * Handles user registration with validation
 */
@Component
@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {
    
    @Autowired
    private UserDAOImpl userDAO;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * init() - Called once when servlet is loaded
     */
    @Override
    public void init() throws ServletException {
        super.init();
        System.out.println("RegisterServlet initialized");
    }
    
    /**
     * doGet() - Display registration form
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if already logged in
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }
        
        // Forward to registration page
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/register.jsp");
        dispatcher.forward(request, response);
    }
    
    /**
     * doPost() - Process registration form
     * Demonstrates form handling and validation
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get form parameters
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String phoneNumber = request.getParameter("phoneNumber");
        String shippingAddress = request.getParameter("shippingAddress");
        
        // Validate input
        StringBuilder errors = new StringBuilder();
        
        if (email == null || email.trim().isEmpty()) {
            errors.append("Email is required. ");
        } else if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            errors.append("Invalid email format. ");
        }
        
        if (password == null || password.length() < 6) {
            errors.append("Password must be at least 6 characters. ");
        }
        
        if (!password.equals(confirmPassword)) {
            errors.append("Passwords do not match. ");
        }
        
        if (firstName == null || firstName.trim().isEmpty()) {
            errors.append("First name is required. ");
        }
        
        if (lastName == null || lastName.trim().isEmpty()) {
            errors.append("Last name is required. ");
        }
        
        // If validation fails, return to form with errors
        if (errors.length() > 0) {
            request.setAttribute("error", errors.toString());
            request.setAttribute("email", email);
            request.setAttribute("firstName", firstName);
            request.setAttribute("lastName", lastName);
            request.setAttribute("phoneNumber", phoneNumber);
            request.setAttribute("shippingAddress", shippingAddress);
            
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/register.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        try {
            // Check if email already exists
            if (userDAO.findByEmail(email) != null) {
                request.setAttribute("error", "Email already registered. Please login.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/register.jsp");
                dispatcher.forward(request, response);
                return;
            }
            
            // Create customer object
            Customer customer = new Customer();
            customer.setEmail(email);
            customer.setPassword(passwordEncoder.encode(password));
            customer.setFirstName(firstName);
            customer.setLastName(lastName);
            customer.setPhoneNumber(phoneNumber);
            customer.setShippingAddress(shippingAddress);
            customer.setRole(UserRole.CUSTOMER);
            
            // Save to database
            userDAO.save(customer);
            
            System.out.println("New user registered: " + email);
            
            // Redirect to login page with success message
            response.sendRedirect(request.getContextPath() + "/login?registered=true");
            
        } catch (SQLException e) {
            System.err.println("Registration failed: " + e.getMessage());
            request.setAttribute("error", "Registration failed. Please try again.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/register.jsp");
            dispatcher.forward(request, response);
        }
    }
    
    /**
     * destroy() - Called when servlet is unloaded
     */
    @Override
    public void destroy() {
        System.out.println("RegisterServlet destroyed");
        super.destroy();
    }
}
