package com.ecommerce.servlet;

import com.ecommerce.dao.UserDAO;
import com.ecommerce.model.Admin;
import com.ecommerce.model.User;
import com.ecommerce.model.UserRole;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Admin Login Servlet
 * Demonstrates HttpServlet with doGet and doPost methods
 * Handles admin authentication with session management
 */
@Component
@WebServlet(name = "AdminLoginServlet", urlPatterns = "/admin/login")
public class AdminLoginServlet extends HttpServlet {
    
    private static final Logger logger = LoggerFactory.getLogger(AdminLoginServlet.class);
    
    @Autowired
    private UserDAO userDAO;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * doGet - Display login form
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check if already logged in
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("adminUser") != null) {
            response.sendRedirect("/admin/dashboard");
            return;
        }
        
        // Display login form
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Admin Login</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; background: #f4f4f4; }");
        out.println(".container { max-width: 400px; margin: 100px auto; background: white; padding: 30px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }");
        out.println("h2 { text-align: center; color: #333; }");
        out.println("input { width: 100%; padding: 10px; margin: 10px 0; border: 1px solid #ddd; border-radius: 4px; }");
        out.println("button { width: 100%; padding: 12px; background: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer; }");
        out.println("button:hover { background: #0056b3; }");
        out.println(".error { color: red; text-align: center; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container'>");
        out.println("<h2>Admin Login</h2>");
        
        String error = request.getParameter("error");
        if (error != null) {
            out.println("<p class='error'>Invalid credentials. Please try again.</p>");
        }
        
        out.println("<form method='post' action='/admin/login'>");
        out.println("<input type='email' name='email' placeholder='Email' required>");
        out.println("<input type='password' name='password' placeholder='Password' required>");
        out.println("<button type='submit'>Login</button>");
        out.println("</form>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
    
    /**
     * doPost - Process login
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        logger.info("Admin login attempt for email: {}", email);
        
        try {
            // Find user by email
            User user = userDAO.findByEmail(email);
            
            // Validate user exists, is admin, and password matches
            if (user != null && 
                user.getRole() == UserRole.ADMIN && 
                passwordEncoder.matches(password, user.getPassword())) {
                
                // Create session
                HttpSession session = request.getSession(true);
                session.setAttribute("adminUser", user);
                session.setAttribute("userId", user.getId());
                session.setAttribute("userEmail", user.getEmail());
                session.setMaxInactiveInterval(30 * 60); // 30 minutes
                
                logger.info("Admin login successful: {}", email);
                
                // Redirect to dashboard
                response.sendRedirect("/admin/dashboard");
                
            } else {
                logger.warn("Admin login failed for email: {}", email);
                response.sendRedirect("/admin/login?error=true");
            }
            
        } catch (Exception e) {
            logger.error("Error during admin login", e);
            response.sendRedirect("/admin/login?error=true");
        }
    }
}
