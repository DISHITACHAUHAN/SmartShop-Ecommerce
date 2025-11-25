package com.ecommerce.service;

import com.ecommerce.model.Product;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Product Catalog Service
 * Demonstrates advanced Collections and Generics usage
 * 
 * Uses List, Map, Set, and Stream API
 */
public class ProductCatalog {
    
    // List of all products
    private List<Product> products;
    
    // Map for quick lookup by ID
    private Map<Long, Product> productMap;
    
    // Map of products by category
    private Map<String, List<Product>> productsByCategory;
    
    // Set of unique categories (sorted)
    private Set<String> categories;
    
    /**
     * Constructor initializes all collections
     */
    public ProductCatalog() {
        this.products = new ArrayList<>();
        this.productMap = new HashMap<>();
        this.productsByCategory = new TreeMap<>(); // Sorted by category name
        this.categories = new TreeSet<>(); // Sorted set
    }
    
    /**
     * Add product to catalog
     * Demonstrates multiple collection operations
     * 
     * @param product product to add
     */
    public void addProduct(Product product) {
        if (product == null || product.getId() == null) {
            throw new IllegalArgumentException("Invalid product");
        }
        
        // Add to list
        products.add(product);
        
        // Add to map for quick lookup
        productMap.put(product.getId(), product);
        
        // Add to category map
        String category = product.getCategory();
        productsByCategory.computeIfAbsent(category, k -> new ArrayList<>())
                         .add(product);
        
        // Add category to set
        categories.add(category);
        
        System.out.println("Added product: " + product.getName() + " to catalog");
    }
    
    /**
     * Add multiple products
     * Demonstrates Collection parameter
     * 
     * @param productList list of products to add
     */
    public void addProducts(Collection<Product> productList) {
        productList.forEach(this::addProduct);
    }
    
    /**
     * Get product by ID
     * Demonstrates Map lookup
     * 
     * @param productId product ID
     * @return product or null
     */
    public Product getProductById(Long productId) {
        return productMap.get(productId);
    }
    
    /**
     * Get all products
     * 
     * @return list of all products
     */
    public List<Product> getAllProducts() {
        return new ArrayList<>(products); // Defensive copy
    }
    
    /**
     * Get products by category
     * Demonstrates Map with List values
     * 
     * @param category category name
     * @return list of products in category
     */
    public List<Product> getProductsByCategory(String category) {
        return productsByCategory.getOrDefault(category, new ArrayList<>());
    }
    
    /**
     * Get all categories
     * Demonstrates Set usage
     * 
     * @return set of categories (sorted)
     */
    public Set<String> getAllCategories() {
        return new TreeSet<>(categories); // Return sorted copy
    }
    
    /**
     * Search products by name
     * Demonstrates Stream API filtering
     * 
     * @param query search query
     * @return list of matching products
     */
    public List<Product> searchByName(String query) {
        String lowerQuery = query.toLowerCase();
        return products.stream()
                      .filter(p -> p.getName().toLowerCase().contains(lowerQuery))
                      .collect(Collectors.toList());
    }
    
    /**
     * Search products by name or description
     * 
     * @param query search query
     * @return list of matching products
     */
    public List<Product> search(String query) {
        String lowerQuery = query.toLowerCase();
        return products.stream()
                      .filter(p -> p.getName().toLowerCase().contains(lowerQuery) ||
                                  (p.getDescription() != null && 
                                   p.getDescription().toLowerCase().contains(lowerQuery)))
                      .collect(Collectors.toList());
    }
    
    /**
     * Get products in price range
     * Demonstrates Stream API with multiple filters
     * 
     * @param minPrice minimum price
     * @param maxPrice maximum price
     * @return list of products in range
     */
    public List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return products.stream()
                      .filter(p -> p.getPrice().compareTo(minPrice) >= 0 &&
                                  p.getPrice().compareTo(maxPrice) <= 0)
                      .collect(Collectors.toList());
    }
    
    /**
     * Get products in stock
     * 
     * @return list of available products
     */
    public List<Product> getInStockProducts() {
        return products.stream()
                      .filter(p -> p.getStockQuantity() != null && p.getStockQuantity() > 0)
                      .collect(Collectors.toList());
    }
    
    /**
     * Get out of stock products
     * 
     * @return list of unavailable products
     */
    public List<Product> getOutOfStockProducts() {
        return products.stream()
                      .filter(p -> p.getStockQuantity() == null || p.getStockQuantity() == 0)
                      .collect(Collectors.toList());
    }
    
    /**
     * Get products sorted by price (ascending)
     * Demonstrates Comparator
     * 
     * @return sorted list
     */
    public List<Product> getProductsSortedByPrice() {
        return products.stream()
                      .sorted(Comparator.comparing(Product::getPrice))
                      .collect(Collectors.toList());
    }
    
    /**
     * Get products sorted by price (descending)
     * 
     * @return sorted list
     */
    public List<Product> getProductsSortedByPriceDesc() {
        return products.stream()
                      .sorted(Comparator.comparing(Product::getPrice).reversed())
                      .collect(Collectors.toList());
    }
    
    /**
     * Get products sorted by name
     * 
     * @return sorted list
     */
    public List<Product> getProductsSortedByName() {
        return products.stream()
                      .sorted(Comparator.comparing(Product::getName))
                      .collect(Collectors.toList());
    }
    
    /**
     * Get top N expensive products
     * Demonstrates limit() with Stream API
     * 
     * @param n number of products
     * @return list of top N products
     */
    public List<Product> getTopNExpensiveProducts(int n) {
        return products.stream()
                      .sorted(Comparator.comparing(Product::getPrice).reversed())
                      .limit(n)
                      .collect(Collectors.toList());
    }
    
    /**
     * Get top N cheapest products
     * 
     * @param n number of products
     * @return list of top N products
     */
    public List<Product> getTopNCheapestProducts(int n) {
        return products.stream()
                      .sorted(Comparator.comparing(Product::getPrice))
                      .limit(n)
                      .collect(Collectors.toList());
    }
    
    /**
     * Get products by multiple categories
     * Demonstrates filtering with Set
     * 
     * @param categorySet set of categories
     * @return list of products
     */
    public List<Product> getProductsByCategories(Set<String> categorySet) {
        return products.stream()
                      .filter(p -> categorySet.contains(p.getCategory()))
                      .collect(Collectors.toList());
    }
    
    /**
     * Get product count by category
     * Demonstrates grouping with Stream API
     * 
     * @return map of category to count
     */
    public Map<String, Long> getProductCountByCategory() {
        return products.stream()
                      .collect(Collectors.groupingBy(
                          Product::getCategory,
                          Collectors.counting()
                      ));
    }
    
    /**
     * Get average price by category
     * 
     * @return map of category to average price
     */
    public Map<String, Double> getAveragePriceByCategory() {
        return products.stream()
                      .collect(Collectors.groupingBy(
                          Product::getCategory,
                          Collectors.averagingDouble(p -> p.getPrice().doubleValue())
                      ));
    }
    
    /**
     * Get catalog statistics
     * 
     * @return map with statistics
     */
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalProducts", products.size());
        stats.put("totalCategories", categories.size());
        stats.put("inStock", getInStockProducts().size());
        stats.put("outOfStock", getOutOfStockProducts().size());
        stats.put("productsByCategory", getProductCountByCategory());
        return stats;
    }
    
    /**
     * Remove product from catalog
     * 
     * @param productId product ID
     * @return true if removed
     */
    public boolean removeProduct(Long productId) {
        Product product = productMap.remove(productId);
        if (product != null) {
            products.remove(product);
            List<Product> categoryList = productsByCategory.get(product.getCategory());
            if (categoryList != null) {
                categoryList.remove(product);
            }
            System.out.println("Removed product: " + product.getName());
            return true;
        }
        return false;
    }
    
    /**
     * Clear catalog
     */
    public void clear() {
        products.clear();
        productMap.clear();
        productsByCategory.clear();
        categories.clear();
        System.out.println("Catalog cleared");
    }
    
    /**
     * Get catalog size
     * 
     * @return number of products
     */
    public int size() {
        return products.size();
    }
    
    @Override
    public String toString() {
        return "ProductCatalog{" +
                "products=" + products.size() +
                ", categories=" + categories.size() +
                '}';
    }
}
