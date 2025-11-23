package com.ecommerce.service.impl;

import com.ecommerce.dao.ProductDAO;
import com.ecommerce.exception.ProductNotFoundException;
import com.ecommerce.model.Product;
import com.ecommerce.service.IProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Product Service Implementation
 * Demonstrates Collections usage (List, Set, Map) and Java Streams
 */
@Service
public class ProductServiceImpl implements IProductService {
    
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    
    @Autowired
    private ProductDAO productDAO;
    
    @Override
    public Product createProduct(Product product) {
        try {
            Product savedProduct = productDAO.save(product);
            logger.info("Product created: {}", savedProduct.getId());
            return savedProduct;
        } catch (SQLException e) {
            logger.error("Error creating product", e);
            throw new RuntimeException("Failed to create product: " + e.getMessage());
        }
    }
    
    @Override
    public Product updateProduct(Long id, Product product) {
        try {
            Product existing = productDAO.findById(id);
            if (existing == null) {
                throw new ProductNotFoundException("Product not found with ID: " + id);
            }
            
            product.setId(id);
            Product updated = productDAO.update(product);
            logger.info("Product updated: {}", id);
            return updated;
        } catch (SQLException e) {
            logger.error("Error updating product", e);
            throw new RuntimeException("Failed to update product: " + e.getMessage());
        }
    }
    
    @Override
    public void deleteProduct(Long id) {
        try {
            boolean deleted = productDAO.delete(id);
            if (!deleted) {
                throw new ProductNotFoundException("Product not found with ID: " + id);
            }
            logger.info("Product deleted: {}", id);
        } catch (SQLException e) {
            logger.error("Error deleting product", e);
            throw new RuntimeException("Failed to delete product: " + e.getMessage());
        }
    }
    
    @Override
    public Product getProductById(Long id) {
        try {
            Product product = productDAO.findById(id);
            if (product == null) {
                throw new ProductNotFoundException("Product not found with ID: " + id);
            }
            return product;
        } catch (SQLException e) {
            logger.error("Error fetching product", e);
            throw new RuntimeException("Failed to fetch product: " + e.getMessage());
        }
    }
    
    @Override
    public List<Product> getAllProducts() {
        try {
            // Demonstrates List usage
            List<Product> products = productDAO.findAll();
            logger.info("Fetched {} products", products.size());
            return products;
        } catch (SQLException e) {
            logger.error("Error fetching products", e);
            throw new RuntimeException("Failed to fetch products: " + e.getMessage());
        }
    }
    
    @Override
    public List<Product> searchProducts(String query) {
        try {
            if (query == null || query.trim().isEmpty()) {
                return getAllProducts();
            }
            
            // Demonstrates List and Stream usage
            List<Product> products = productDAO.searchProducts(query);
            
            // Sort by relevance using Streams
            return products.stream()
                    .sorted(Comparator.comparing(Product::getName))
                    .collect(Collectors.toList());
                    
        } catch (SQLException e) {
            logger.error("Error searching products", e);
            throw new RuntimeException("Failed to search products: " + e.getMessage());
        }
    }
    
    @Override
    public List<Product> getProductsByCategory(String category) {
        try {
            List<Product> products = productDAO.findByCategory(category);
            logger.info("Fetched {} products in category: {}", products.size(), category);
            return products;
        } catch (SQLException e) {
            logger.error("Error fetching products by category", e);
            throw new RuntimeException("Failed to fetch products: " + e.getMessage());
        }
    }
    
    @Override
    public List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        try {
            List<Product> products = productDAO.findByPriceRange(minPrice, maxPrice);
            
            // Demonstrates Stream filtering and sorting
            return products.stream()
                    .filter(p -> p.getPrice().compareTo(minPrice) >= 0 
                              && p.getPrice().compareTo(maxPrice) <= 0)
                    .sorted(Comparator.comparing(Product::getPrice))
                    .collect(Collectors.toList());
                    
        } catch (SQLException e) {
            logger.error("Error fetching products by price range", e);
            throw new RuntimeException("Failed to fetch products: " + e.getMessage());
        }
    }
    
    @Override
    public Set<String> getAllCategories() {
        try {
            // Demonstrates Set usage for unique values
            Set<String> categories = productDAO.getAllCategories();
            logger.info("Fetched {} unique categories", categories.size());
            return categories;
        } catch (SQLException e) {
            logger.error("Error fetching categories", e);
            throw new RuntimeException("Failed to fetch categories: " + e.getMessage());
        }
    }
    
    /**
     * Filter products by multiple criteria using Streams
     * Demonstrates advanced Collections and Stream usage
     */
    public List<Product> filterProducts(String category, BigDecimal minPrice, 
                                       BigDecimal maxPrice, Boolean inStockOnly) {
        try {
            List<Product> products = productDAO.findAll();
            
            // Use Stream API for filtering
            return products.stream()
                    .filter(p -> category == null || category.equals(p.getCategory()))
                    .filter(p -> minPrice == null || p.getPrice().compareTo(minPrice) >= 0)
                    .filter(p -> maxPrice == null || p.getPrice().compareTo(maxPrice) <= 0)
                    .filter(p -> !Boolean.TRUE.equals(inStockOnly) || p.isInStock())
                    .collect(Collectors.toList());
                    
        } catch (SQLException e) {
            logger.error("Error filtering products", e);
            throw new RuntimeException("Failed to filter products: " + e.getMessage());
        }
    }
    
    /**
     * Group products by category using Map
     * Demonstrates Map usage with Collectors.groupingBy
     */
    public Map<String, List<Product>> groupProductsByCategory() {
        try {
            List<Product> products = productDAO.findAll();
            
            // Demonstrates Map usage with Stream grouping
            return products.stream()
                    .collect(Collectors.groupingBy(Product::getCategory));
                    
        } catch (SQLException e) {
            logger.error("Error grouping products", e);
            throw new RuntimeException("Failed to group products: " + e.getMessage());
        }
    }
    
    /**
     * Get product statistics using Collections
     * Demonstrates advanced Stream operations
     */
    public Map<String, Object> getProductStatistics() {
        try {
            List<Product> products = productDAO.findAll();
            Map<String, Object> stats = new HashMap<>();
            
            // Total products
            stats.put("totalProducts", products.size());
            
            // Average price using Stream
            OptionalDouble avgPrice = products.stream()
                    .mapToDouble(p -> p.getPrice().doubleValue())
                    .average();
            stats.put("averagePrice", avgPrice.orElse(0.0));
            
            // Products in stock
            long inStock = products.stream()
                    .filter(Product::isInStock)
                    .count();
            stats.put("productsInStock", inStock);
            
            // Total inventory value
            BigDecimal totalValue = products.stream()
                    .map(p -> p.getPrice().multiply(BigDecimal.valueOf(p.getStockQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            stats.put("totalInventoryValue", totalValue);
            
            return stats;
            
        } catch (SQLException e) {
            logger.error("Error calculating statistics", e);
            throw new RuntimeException("Failed to calculate statistics: " + e.getMessage());
        }
    }
}
