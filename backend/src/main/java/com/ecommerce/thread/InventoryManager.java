package com.ecommerce.thread;

import com.ecommerce.exception.InsufficientStockException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Inventory Manager with Thread Safety
 * Demonstrates Synchronization and thread-safe operations
 * 
 * Uses synchronized methods, ReentrantLock, and ConcurrentHashMap
 */
public class InventoryManager {
    
    // Thread-safe map for stock quantities
    // Demonstrates ConcurrentHashMap
    private ConcurrentHashMap<Long, Integer> stockMap;
    
    // Lock for critical sections
    // Demonstrates ReentrantLock
    private ReentrantLock lock;
    
    // Read-Write lock for optimized concurrent access
    // Demonstrates ReadWriteLock
    private ReadWriteLock rwLock;
    
    // Statistics
    private int totalReductions = 0;
    private int totalIncreases = 0;
    
    /**
     * Constructor initializes thread-safe collections
     */
    public InventoryManager() {
        this.stockMap = new ConcurrentHashMap<>();
        this.lock = new ReentrantLock();
        this.rwLock = new ReentrantReadWriteLock();
    }
    
    /**
     * Initialize stock for a product
     * 
     * @param productId product ID
     * @param quantity initial quantity
     */
    public void initializeStock(Long productId, int quantity) {
        stockMap.put(productId, quantity);
        System.out.println("Initialized stock for product " + productId + ": " + quantity);
    }
    
    /**
     * Reduce stock (synchronized method)
     * Demonstrates synchronized keyword for thread safety
     * 
     * @param productId product ID
     * @param quantity quantity to reduce
     * @return true if stock reduced successfully
     * @throws InsufficientStockException if not enough stock
     */
    public synchronized boolean reduceStock(Long productId, int quantity) 
            throws InsufficientStockException {
        
        Integer currentStock = stockMap.get(productId);
        
        if (currentStock == null) {
            throw new InsufficientStockException(productId, quantity, 0);
        }
        
        if (currentStock < quantity) {
            throw new InsufficientStockException(productId, quantity, currentStock);
        }
        
        // Reduce stock
        int newStock = currentStock - quantity;
        stockMap.put(productId, newStock);
        totalReductions++;
        
        System.out.println("[SYNC] Stock reduced for product " + productId + 
                          ": " + currentStock + " -> " + newStock + 
                          " (Thread: " + Thread.currentThread().getName() + ")");
        
        return true;
    }
    
    /**
     * Increase stock (using ReentrantLock)
     * Demonstrates explicit locking
     * 
     * @param productId product ID
     * @param quantity quantity to add
     * @return true if stock increased
     */
    public boolean increaseStock(Long productId, int quantity) {
        lock.lock(); // Acquire lock
        try {
            Integer currentStock = stockMap.getOrDefault(productId, 0);
            int newStock = currentStock + quantity;
            stockMap.put(productId, newStock);
            totalIncreases++;
            
            System.out.println("[LOCK] Stock increased for product " + productId + 
                              ": " + currentStock + " -> " + newStock +
                              " (Thread: " + Thread.currentThread().getName() + ")");
            
            return true;
        } finally {
            lock.unlock(); // Always release lock
        }
    }
    
    /**
     * Get current stock (using ReadLock)
     * Demonstrates read-write lock for optimized concurrent reads
     * 
     * @param productId product ID
     * @return current stock quantity
     */
    public int getStock(Long productId) {
        rwLock.readLock().lock(); // Multiple threads can read simultaneously
        try {
            return stockMap.getOrDefault(productId, 0);
        } finally {
            rwLock.readLock().unlock();
        }
    }
    
    /**
     * Update stock (using WriteLock)
     * Demonstrates write lock for exclusive access
     * 
     * @param productId product ID
     * @param newQuantity new quantity
     */
    public void updateStock(Long productId, int newQuantity) {
        rwLock.writeLock().lock(); // Exclusive access for writing
        try {
            Integer oldStock = stockMap.put(productId, newQuantity);
            System.out.println("[RWLOCK] Stock updated for product " + productId + 
                              ": " + oldStock + " -> " + newQuantity +
                              " (Thread: " + Thread.currentThread().getName() + ")");
        } finally {
            rwLock.writeLock().unlock();
        }
    }
    
    /**
     * Check if product is in stock
     * 
     * @param productId product ID
     * @param quantity required quantity
     * @return true if sufficient stock available
     */
    public boolean isInStock(Long productId, int quantity) {
        int currentStock = getStock(productId);
        return currentStock >= quantity;
    }
    
    /**
     * Reserve stock for order (atomic operation)
     * Demonstrates atomic operations with synchronization
     * 
     * @param productId product ID
     * @param quantity quantity to reserve
     * @return true if reserved successfully
     */
    public synchronized boolean reserveStock(Long productId, int quantity) {
        try {
            return reduceStock(productId, quantity);
        } catch (InsufficientStockException e) {
            System.err.println("Cannot reserve stock: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Release reserved stock (rollback)
     * 
     * @param productId product ID
     * @param quantity quantity to release
     */
    public void releaseStock(Long productId, int quantity) {
        increaseStock(productId, quantity);
        System.out.println("Released reserved stock for product " + productId + ": " + quantity);
    }
    
    /**
     * Get total stock across all products
     * Demonstrates ConcurrentHashMap operations
     * 
     * @return total stock
     */
    public int getTotalStock() {
        return stockMap.values().stream()
                      .mapToInt(Integer::intValue)
                      .sum();
    }
    
    /**
     * Get statistics
     * 
     * @return statistics map
     */
    public synchronized java.util.Map<String, Object> getStatistics() {
        java.util.Map<String, Object> stats = new java.util.HashMap<>();
        stats.put("totalProducts", stockMap.size());
        stats.put("totalStock", getTotalStock());
        stats.put("totalReductions", totalReductions);
        stats.put("totalIncreases", totalIncreases);
        return stats;
    }
    
    /**
     * Clear all stock
     */
    public void clear() {
        lock.lock();
        try {
            stockMap.clear();
            totalReductions = 0;
            totalIncreases = 0;
            System.out.println("Inventory cleared");
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public String toString() {
        return "InventoryManager{" +
                "products=" + stockMap.size() +
                ", totalStock=" + getTotalStock() +
                ", reductions=" + totalReductions +
                ", increases=" + totalIncreases +
                '}';
    }
}
