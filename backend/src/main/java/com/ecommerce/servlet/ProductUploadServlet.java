package com.ecommerce.servlet;

import com.ecommerce.dao.ProductDAO;
import com.ecommerce.model.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

/**
 * Product Upload Servlet
 * Demonstrates HttpServlet with form handling and session validation
 * Handles product creation through servlet interface
 */
@Component
@WebServlet(name = "ProductUploadServlet", urlPatterns = "/admin/product-upload")
@MultipartConfig
public class ProductUploadServlet extends HttpServlet {
    
    private static final Logger logger = LoggerFactory.getLogger(ProductUploadServlet.class);
    
    @Autowired
    private ProductDAO productDAO;
    
    /**
     * doGet - Display product upload form
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check admin session
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("adminUser") == null) {
            response.sendRedirect("/admin/login");
            return;
        }
        
        // Display product upload form
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Upload Product</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; background: #f4f4f4; }");
        out.println(".container { max-width: 600px; margin: 50px auto; background: white; padding: 30px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }");
        out.println("h2 { text-align: center; color: #333; }");
        out.println("label { display: block; margin-top: 15px; font-weight: bold; }");
        out.println("input, textarea, select { width: 100%; padding: 10px; margin: 5px 0; border: 1px solid #ddd; border-radius: 4px; }");
        out.println("button { width: 100%; padding: 12px; margin-top: 20px; background: #28a745; color: white; border: none; border-radius: 4px; cursor: pointer; }");
        out.println("button:hover { background: #218838; }");
        out.println(".success { color: green; text-align: center; }");
        out.println(".error { color: red; text-align: center; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container'>");
        out.println("<h2>Upload New Product</h2>");
        
        String success = request.getParameter("success");
        String error = request.getParameter("error");
        
        if (success != null) {
            out.println("<p class='success'>Product uploaded successfully!</p>");
        }
        if (error != null) {
            out.println("<p class='error'>Error uploading product. Please try again.</p>");
        }
        
        out.println("<form method='post' action='/admin/product-upload' enctype='multipart/form-data'>");
        out.println("<label>Product Name:</label>");
        out.println("<input type='text' name='name' required>");
        
        out.println("<label>Description:</label>");
        out.println("<textarea name='description' rows='4' required></textarea>");
        
        out.println("<label>Price:</label>");
        out.println("<input type='number' name='price' step='0.01' required>");
        
        out.println("<label>Stock Quantity:</label>");
        out.println("<input type='number' name='stockQuantity' required>");
        
        out.println("<label>Category:</label>");
        out.println("<select name='category' required>");
        out.println("<option value='Electronics'>Electronics</option>");
        out.println("<option value='Sports'>Sports</option>");
        out.println("<option value='Home'>Home</option>");
        out.println("<option value='Accessories'>Accessories</option>");
        out.println("<option value='Stationery'>Stationery</option>");
        out.println("</select>");
        
        out.println("<label>Image URL:</label>");
        out.println("<input type='text' name='imageUrl'>");
        
        out.println("<button type='submit'>Upload Product</button>");
        out.println("</form>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
    
    /**
     * doPost - Process product upload
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Validate session
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("adminUser") == null) {
            response.sendRedirect("/admin/login");
            return;
        }
        
        try {
            // Parse form data
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            BigDecimal price = new BigDecimal(request.getParameter("price"));
            Integer stockQuantity = Integer.parseInt(request.getParameter("stockQuantity"));
            String category = request.getParameter("category");
            String imageUrl = request.getParameter("imageUrl");
            
            // Create product
            Product product = new Product();
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setStockQuantity(stockQuantity);
            product.setCategory(category);
            product.setImageUrl(imageUrl != null && !imageUrl.isEmpty() ? 
                imageUrl : "https://via.placeholder.com/300x300?text=" + name);
            
            // Save product using DAO
            productDAO.save(product);
            
            logger.info("Product uploaded successfully: {}", name);
            
            // Redirect with success message
            response.sendRedirect("/admin/product-upload?success=true");
            
        } catch (Exception e) {
            logger.error("Error uploading product", e);
            response.sendRedirect("/admin/product-upload?error=true");
        }
    }
}
