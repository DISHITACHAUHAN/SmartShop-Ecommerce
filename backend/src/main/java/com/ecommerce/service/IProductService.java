package com.ecommerce.service;

import com.ecommerce.model.Product;
import java.math.BigDecimal;
import java.util.List;

/**
 * Product Service Interface
 * Demonstrates abstraction - defines contract for product operations
 */
public interface IProductService {
    
    /**
     * Create a new product
     * @param product product to create
     * @return created product
     */
    Product createProduct(Product product);
    
    /**
     * Update existing product
     * @param id product ID
     * @param product updated product data
     * @return updated product
     */
    Product updateProduct(Long id, Product product);
    
    /**
     * Delete product by ID
     * @param id product ID
     */
    void deleteProduct(Long id);
    
    /**
     * Get product by ID
     * @param id product ID
     * @return product
     */
    Product getProductById(Long id);
    
    /**
     * Get all products
     * @return list of products
     */
    List<Product> getAllProducts();
    
    /**
     * Search products by query
     * @param query search query
     * @return list of matching products
     */
    List<Product> searchProducts(String query);
    
    /**
     * Get products by category
     * @param category category name
     * @return list of products
     */
    List<Product> getProductsByCategory(String category);
    
    /**
     * Get products by price range
     * @param minPrice minimum price
     * @param maxPrice maximum price
     * @return list of products
     */
    List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
    
    /**
     * Get all unique categories
     * @return set of categories
     */
    java.util.Set<String> getAllCategories();
}
