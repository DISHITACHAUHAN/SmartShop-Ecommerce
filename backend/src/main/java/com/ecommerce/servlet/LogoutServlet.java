package com.ecommerce.servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Logout Servlet
 * Demonstrates session invalidation
 * Handles user logout and session cleanup
 */
@Component
@WebServlet(name = "LogoutServlet", urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {
    
    @Override
    public void init() throws ServletException {
        super.init();
        System.out.println("LogoutServlet initialized");
    }
    
    /**
     * doGet() - Handle logout
     * Demonstrates session invalidation
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get existing session (don't create new one)
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            // Get user info before invalidating
            String userEmail = (String) session.getAttribute("userEmail");
            String userId = String.valueOf(session.getAttribute("userId"));
            
            // Log logout
            System.out.println("User logging out: " + userEmail + " (ID: " + userId + ")");
            
            // Invalidate session (removes all attributes)
            session.invalidate();
            
            System.out.println("Session invalidated for user: " + userEmail);
        }
        
        // Redirect to login page with logout message
        response.sendRedirect(request.getContextPath() + "/login?logout=true");
    }
    
    /**
     * doPost() - Also handle POST requests for logout
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
    
    @Override
    public void destroy() {
        System.out.println("LogoutServlet destroyed");
        super.destroy();
    }
}
