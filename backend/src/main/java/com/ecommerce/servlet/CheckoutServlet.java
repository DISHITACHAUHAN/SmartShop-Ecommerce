package com.ecommerce.servlet;

import com.ecommerce.model.CartItem;
import com.ecommerce.model.Order;
import com.ecommerce.model.User;
import com.ecommerce.payment.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Checkout Servlet
 * Demonstrates complete checkout flow with payment processing
 * Uses session management and RequestDispatcher
 */
@Component
@WebServlet(name = "CheckoutServlet", urlPatterns = {"/checkout"})
public class CheckoutServlet extends HttpServlet {
    
    @Override
    public void init() throws ServletException {
        super.init();
        System.out.println("CheckoutServlet initialized");
    }
    
    /**
     * doGet() - Display checkout page
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=checkout");
            return;
        }
        
        // Check if cart is empty
        @SuppressWarnings("unchecked")
        Map<Long, CartItem> cart = (Map<Long, CartItem>) session.getAttribute("cart");
        
        if (cart == null || cart.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart?error=empty");
            return;
        }
        
        // Calculate total
        BigDecimal total = cart.values().stream()
            .map(CartItem::getTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        request.setAttribute("cartTotal", total);
        request.setAttribute("cartItems", cart.values());
        
        // Forward to checkout page
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/checkout.jsp");
        dispatcher.forward(request, response);
    }
    
    /**
     * doPost() - Process checkout and payment
     * Demonstrates payment processing with polymorphism
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Get cart from session
        @SuppressWarnings("unchecked")
        Map<Long, CartItem> cart = (Map<Long, CartItem>) session.getAttribute("cart");
        
        if (cart == null || cart.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart?error=empty");
            return;
        }
        
        // Get payment details
        String paymentMethod = request.getParameter("paymentMethod");
        String shippingAddress = request.getParameter("shippingAddress");
        
        // Calculate total
        BigDecimal total = cart.values().stream()
            .map(CartItem::getTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Create payment strategy based on method (Polymorphism)
        PaymentStrategy paymentStrategy = createPaymentStrategy(request, paymentMethod);
        
        if (paymentStrategy == null) {
            request.setAttribute("error", "Invalid payment method");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/checkout.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        // Process payment using PaymentProcessor
        PaymentProcessor processor = new PaymentProcessor();
        boolean paymentSuccess = processor.processPayment(paymentStrategy, total);
        
        if (paymentSuccess) {
            // Create order
            User user = (User) session.getAttribute("user");
            Order order = new Order();
            order.setUserId(user.getId());
            order.setTotalAmount(total);
            order.setShippingAddress(shippingAddress);
            order.setStatus(com.ecommerce.model.OrderStatus.PENDING);
            
            // Store transaction ID in session
            session.setAttribute("lastTransactionId", paymentStrategy.getTransactionId());
            session.setAttribute("lastOrder", order);
            
            // Clear cart after successful order
            session.removeAttribute("cart");
            
            System.out.println("Order placed successfully for user: " + user.getEmail());
            
            // Redirect to order confirmation
            response.sendRedirect(request.getContextPath() + "/order-confirmation");
            
        } else {
            // Payment failed
            request.setAttribute("error", "Payment failed: " + paymentStrategy.getStatusMessage());
            request.setAttribute("cartTotal", total);
            request.setAttribute("cartItems", cart.values());
            
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/checkout.jsp");
            dispatcher.forward(request, response);
        }
    }
    
    /**
     * Create payment strategy based on payment method
     * Demonstrates Factory pattern and polymorphism
     */
    private PaymentStrategy createPaymentStrategy(HttpServletRequest request, String paymentMethod) {
        if (paymentMethod == null) {
            return null;
        }
        
        switch (paymentMethod.toUpperCase()) {
            case "CARD":
                String cardNumber = request.getParameter("cardNumber");
                String cardHolder = request.getParameter("cardHolder");
                String expiryDate = request.getParameter("expiryDate");
                String cvv = request.getParameter("cvv");
                return new CardPayment(cardNumber, cardHolder, expiryDate, cvv);
                
            case "UPI":
                String upiId = request.getParameter("upiId");
                String upiPin = request.getParameter("upiPin");
                return new UPIPayment(upiId, upiPin);
                
            case "COD":
                String address = request.getParameter("shippingAddress");
                String phone = request.getParameter("phone");
                return new CODPayment(address, phone);
                
            default:
                return null;
        }
    }
    
    @Override
    public void destroy() {
        System.out.println("CheckoutServlet destroyed");
        super.destroy();
    }
}
