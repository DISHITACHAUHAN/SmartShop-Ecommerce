package com.ecommerce.async;

import com.ecommerce.dao.ProductDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Inventory Update Service with thread synchronization
 * Demonstrates ReentrantLock, ConcurrentHashMap, AtomicInteger, and CompletableFuture
 */
@Service
public class InventoryUpdateService {
    
    private static final Logger logger = LoggerFactory.getLogger(InventoryUpdateService.class);
    
    @Autowired
    private ProductDAO productDAO;
    
    // Thread-safe cache for stock quantities using ConcurrentHashMap and AtomicInteger
    private final ConcurrentHashMap<Long, AtomicInteger> stockCache = new ConcurrentHashMap<>();
    
    // ReentrantLock for fine-grained synchronization
    private final ReentrantLock inventoryLock = new ReentrantLock();
    
    @PostConstruct
    public void init() {
        logger.info("InventoryUpdateService initialized with thread-safe stock cache");
    }
    
    /**
     * Update stock asynchronously using CompletableFuture
     * Demonstrates multithreading with CompletableFuture and ReentrantLock
     * 
     * @param productId product ID
     * @param quantity quantity to add (positive) or remove (negative)
     */
    public void updateStockAsync(Long productId, int quantity) {
        CompletableFuture.runAsync(() -> {
            inventoryLock.lock();
            try {
                logger.debug("Updating stock for product {}: quantity change = {}", 
                           productId, quantity);
                
                // Get or create atomic counter for this product
                AtomicInteger stock = stockCache.computeIfAbsent(
                    productId, 
                    k -> new AtomicInteger(getCurrentStock(productId))
                );
                
                // Update stock atomically
                int newStock = stock.addAndGet(quantity);
                
                // Persist to database
                persistToDatabase(productId, newStock);
                
                logger.info("Stock updated for product {}: new quantity = {}", 
                          productId, newStock);
                
            } catch (Exception e) {
                logger.error("Error updating stock for product: " + productId, e);
            } finally {
                inventoryLock.unlock();
            }
        });
    }
    
    /**
     * Batch update stock for multiple products
     * Demonstrates parallel processing with CompletableFuture
     * 
     * @param updates map of product ID to quantity change
     */
    public void batchUpdateStockAsync(java.util.Map<Long, Integer> updates) {
        logger.info("Starting batch stock update for {} products", updates.size());
        
        // Create CompletableFuture for each update
        CompletableFuture<?>[] futures = updates.entrySet().stream()
            .map(entry -> CompletableFuture.runAsync(() -> 
                updateStockSync(entry.getKey(), entry.getValue())
            ))
            .toArray(CompletableFuture[]::new);
        
        // Wait for all updates to complete
        CompletableFuture.allOf(futures).thenRun(() -> 
            logger.info("Batch stock update completed for {} products", updates.size())
        );
    }
    
    /**
     * Synchronous stock update with lock
     * Demonstrates ReentrantLock usage
     * 
     * @param productId product ID
     * @param quantity quantity change
     */
    public void updateStockSync(Long productId, int quantity) {
        inventoryLock.lock();
        try {
            AtomicInteger stock = stockCache.computeIfAbsent(
                productId,
                k -> new AtomicInteger(getCurrentStock(productId))
            );
            
            int newStock = stock.addAndGet(quantity);
            persistToDatabase(productId, newStock);
            
            logger.debug("Sync stock update for product {}: new quantity = {}", 
                       productId, newStock);
            
        } catch (Exception e) {
            logger.error("Error in sync stock update", e);
        } finally {
            inventoryLock.unlock();
        }
    }
    
    /**
     * Get current stock from cache or database
     * 
     * @param productId product ID
     * @return current stock quantity
     */
    public int getCurrentStock(Long productId) {
        // Check cache first
        AtomicInteger cachedStock = stockCache.get(productId);
        if (cachedStock != null) {
            return cachedStock.get();
        }
        
        // Fetch from database
        try {
            var product = productDAO.findById(productId);
            return product != null ? product.getStockQuantity() : 0;
        } catch (Exception e) {
            logger.error("Error fetching stock from database", e);
            return 0;
        }
    }
    
    /**
     * Persist stock quantity to database
     * 
     * @param productId product ID
     * @param quantity new quantity
     */
    private void persistToDatabase(Long productId, int quantity) {
        try {
            productDAO.updateStock(productId, quantity);
        } catch (Exception e) {
            logger.error("Error persisting stock to database", e);
            throw new RuntimeException("Failed to persist stock: " + e.getMessage());
        }
    }
    
    /**
     * Clear stock cache for a product
     * 
     * @param productId product ID
     */
    public void clearCache(Long productId) {
        stockCache.remove(productId);
        logger.debug("Cache cleared for product: {}", productId);
    }
    
    /**
     * Clear entire stock cache
     */
    public void clearAllCache() {
        stockCache.clear();
        logger.info("Entire stock cache cleared");
    }
    
    /**
     * Get cache statistics
     * 
     * @return statistics map
     */
    public java.util.Map<String, Object> getCacheStatistics() {
        java.util.Map<String, Object> stats = new java.util.HashMap<>();
        stats.put("cacheSize", stockCache.size());
        stats.put("lockHeldByCurrentThread", inventoryLock.isHeldByCurrentThread());
        stats.put("lockQueueLength", inventoryLock.getQueueLength());
        return stats;
    }
    
    /**
     * Check if stock is available with thread safety
     * Demonstrates tryLock with timeout
     * 
     * @param productId product ID
     * @param requiredQuantity required quantity
     * @return true if stock is available
     */
    public boolean isStockAvailable(Long productId, int requiredQuantity) {
        try {
            // Try to acquire lock with timeout
            if (inventoryLock.tryLock(1, java.util.concurrent.TimeUnit.SECONDS)) {
                try {
                    int currentStock = getCurrentStock(productId);
                    return currentStock >= requiredQuantity;
                } finally {
                    inventoryLock.unlock();
                }
            } else {
                logger.warn("Could not acquire lock to check stock for product: {}", productId);
                return false;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Interrupted while checking stock", e);
            return false;
        }
    }
}
