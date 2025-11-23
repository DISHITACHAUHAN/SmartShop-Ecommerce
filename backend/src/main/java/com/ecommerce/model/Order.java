package com.ecommerce.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Order model class
 * Represents a customer order with multiple items
 * Demonstrates encapsulation and composition
 */
public class Order {
    
    // Private fields (Encapsulation)
    private Long id;
    private Long userId;
    private List<OrderItem> items;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private String paymentId;
    private PaymentStatus paymentStatus;
    private String shippingAddress;
    private LocalDateTime orderDate;

    /**
     * Default constructor
     */
    public Order() {
        this.items = new ArrayList<>();
        this.orderDate = LocalDateTime.now();
        this.status = OrderStatus.PENDING;
        this.paymentStatus = PaymentStatus.PENDING;
        this.totalAmount = BigDecimal.ZERO;
    }

    /**
     * Parameterized constructor
     */
    public Order(Long userId, String shippingAddress) {
        this();
        this.userId = userId;
        this.shippingAddress = shippingAddress;
    }

    // Getters and Setters
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
        recalculateTotalAmount();
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    // Business methods
    
    /**
     * Add an item to the order
     * @param item order item to add
     */
    public void addItem(OrderItem item) {
        if (this.items == null) {
            this.items = new ArrayList<>();
        }
        this.items.add(item);
        recalculateTotalAmount();
    }

    /**
     * Remove an item from the order
     * @param item order item to remove
     */
    public void removeItem(OrderItem item) {
        if (this.items != null) {
            this.items.remove(item);
            recalculateTotalAmount();
        }
    }

    /**
     * Recalculate total amount based on order items
     */
    private void recalculateTotalAmount() {
        if (items == null || items.isEmpty()) {
            this.totalAmount = BigDecimal.ZERO;
            return;
        }
        
        this.totalAmount = items.stream()
                .map(OrderItem::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Get total number of items in order
     * @return count of items
     */
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    /**
     * Check if order is pending
     * @return true if order status is PENDING
     */
    public boolean isPending() {
        return OrderStatus.PENDING.equals(status);
    }

    /**
     * Check if order is completed
     * @return true if order status is DELIVERED
     */
    public boolean isCompleted() {
        return OrderStatus.DELIVERED.equals(status);
    }

    /**
     * Check if payment is completed
     * @return true if payment status is COMPLETED
     */
    public boolean isPaymentCompleted() {
        return PaymentStatus.COMPLETED.equals(paymentStatus);
    }

    /**
     * Mark order as processing
     */
    public void markAsProcessing() {
        this.status = OrderStatus.PROCESSING;
    }

    /**
     * Mark order as shipped
     */
    public void markAsShipped() {
        this.status = OrderStatus.SHIPPED;
    }

    /**
     * Mark order as delivered
     */
    public void markAsDelivered() {
        this.status = OrderStatus.DELIVERED;
    }

    /**
     * Cancel the order
     */
    public void cancel() {
        this.status = OrderStatus.CANCELLED;
    }

    /**
     * Mark payment as completed
     * @param paymentId payment transaction ID
     */
    public void completePayment(String paymentId) {
        this.paymentId = paymentId;
        this.paymentStatus = PaymentStatus.COMPLETED;
    }

    /**
     * Mark payment as failed
     */
    public void failPayment() {
        this.paymentStatus = PaymentStatus.FAILED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", itemCount=" + getItemCount() +
                ", totalAmount=" + totalAmount +
                ", status=" + status +
                ", paymentStatus=" + paymentStatus +
                ", orderDate=" + orderDate +
                '}';
    }
}
