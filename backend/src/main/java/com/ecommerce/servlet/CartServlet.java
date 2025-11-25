package com.ecommerce.servlet;

import com.ecommerce.dao.impl.ProductDAOImpl;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Product;
import com.ecommerce.service.ShoppingCart;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Cart Servlet
 * Demonstrates session management and cart operations
 * Uses HttpSession to maintain shopping cart state
 */
@Component
@WebServlet(name = "CartServlet", urlPatterns = {"/cart"})
public class CartServlet extends HttpServlet {
    
    @Autowired
    private ProductDAOImpl productDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        System.out.println("CartServlet initialized");
    }
    
    /**
     * doGet() - View cart or handle cart actions
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if (action == null || action.equals("view")) {
            viewCart(request, response);
        } else if (action.equals("remove")) {
            removeFromCart(request, response);
        } else if (action.equals("clear")) {
            clearCart(request, response);
        } else {
            viewCart(request, response);
        }
    }
    
    /**
     * doPost() - Add to cart or update quantities
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if (action == null || action.equals("add")) {
            addToCart(request, response);
        } else if (action.equals("update")) {
            updateCart(request, response);
        }
    }
    
    /**
     * Add product to cart
     * Demonstrates session attribute manipulation
     */
    private void addToCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            Long productId = Long.parseLong(request.getParameter("productId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            
            if (quantity <= 0) {
                response.sendRedirect(request.getContextPath() + "/products?error=invalid_quantity");
                return;
            }
            
            // Get product from database
            Product product = productDAO.findById(productId);
            
            if (product == null) {
                response.sendRedirect(request.getContextPath() + "/products?error=product_not_found");
                return;
            }
            
            // Check stock availability
            if (product.getStockQuantity() < quantity) {
                response.sendRedirect(request.getContextPath() + 
                    "/products?error=insufficient_stock&id=" + productId);
                return;
            }
            
            // Get or create session
            HttpSession session = request.getSession(true);
            
            // Get cart from session (using Map for demonstration)
            @SuppressWarnings("unchecked")
            Map<Long, CartItem> cart = (Map<Long, CartItem>) session.getAttribute("cart");
            
            if (cart == null) {
                cart = new HashMap<>();
            }
            
            // Add or update item in cart
            if (cart.containsKey(productId)) {
                CartItem existingItem = cart.get(productId);
                int newQuantity = existingItem.getQuantity() + quantity;
                
                // Check if new quantity exceeds stock
                if (newQuantity > product.getStockQuantity()) {
                    response.sendRedirect(request.getContextPath() + 
                        "/products?error=exceeds_stock&id=" + productId);
                    return;
                }
                
                existingItem.setQuantity(newQuantity);
                System.out.println("Updated cart item: " + product.getName() + " x " + newQuantity);
            } else {
                CartItem newItem = new CartItem(
                    productId,
                    product.getName(),
                    product.getPrice(),
                    quantity,
                    product.getImageUrl()
                );
                cart.put(productId, newItem);
                System.out.println("Added to cart: " + product.getName() + " x " + quantity);
            }
            
            // Store cart in session
            session.setAttribute("cart", cart);
            
            // Redirect to cart page
            response.sendRedirect(request.getContextPath() + "/cart?added=true");
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/products?error=invalid_input");
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }
    
    /**
     * View cart contents
     * Demonstrates reading session attributes
     */
    private void viewCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            @SuppressWarnings("unchecked")
            Map<Long, CartItem> cart = (Map<Long, CartItem>) session.getAttribute("cart");
            
            if (cart != null && !cart.isEmpty()) {
                request.setAttribute("cart", cart.values());
                
                // Calculate total
                java.math.BigDecimal total = cart.values().stream()
                    .map(CartItem::getTotal)
                    .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
                
                request.setAttribute("cartTotal", total);
                request.setAttribute("itemCount", cart.size());
            }
        }
        
        // Forward to cart page
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/cart.jsp");
        dispatcher.forward(request, response);
    }
    
    /**
     * Remove item from cart
     */
    private void removeFromCart(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        try {
            Long productId = Long.parseLong(request.getParameter("productId"));
            
            HttpSession session = request.getSession(false);
            if (session != null) {
                @SuppressWarnings("unchecked")
                Map<Long, CartItem> cart = (Map<Long, CartItem>) session.getAttribute("cart");
                
                if (cart != null) {
                    CartItem removed = cart.remove(productId);
                    if (removed != null) {
                        System.out.println("Removed from cart: " + removed.getProductName());
                    }
                    session.setAttribute("cart", cart);
                }
            }
            
            response.sendRedirect(request.getContextPath() + "/cart?removed=true");
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/cart?error=invalid_input");
        }
    }
    
    /**
     * Update cart quantities
     */
    private void updateCart(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        HttpSession session = request.getSession(false);
        if (session != null) {
            @SuppressWarnings("unchecked")
            Map<Long, CartItem> cart = (Map<Long, CartItem>) session.getAttribute("cart");
            
            if (cart != null) {
                // Update quantities for all items
                for (Long productId : cart.keySet()) {
                    String qtyParam = request.getParameter("qty_" + productId);
                    if (qtyParam != null) {
                        try {
                            int newQty = Integer.parseInt(qtyParam);
                            if (newQty > 0) {
                                cart.get(productId).setQuantity(newQty);
                            } else {
                                cart.remove(productId);
                            }
                        } catch (NumberFormatException e) {
                            // Skip invalid quantities
                        }
                    }
                }
                session.setAttribute("cart", cart);
            }
        }
        
        response.sendRedirect(request.getContextPath() + "/cart?updated=true");
    }
    
    /**
     * Clear entire cart
     * Demonstrates session attribute removal
     */
    private void clearCart(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute("cart");
            System.out.println("Cart cleared for session: " + session.getId());
        }
        
        response.sendRedirect(request.getContextPath() + "/cart?cleared=true");
    }
    
    @Override
    public void destroy() {
        System.out.println("CartServlet destroyed");
        super.destroy();
    }
}
