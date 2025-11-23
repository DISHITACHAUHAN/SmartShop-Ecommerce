# Implementation Plan

- [x] 1. Set up project structure and configuration





  - Create Maven project for Spring Boot backend with proper package structure (controller, service, dao, model, config, exception, util, servlet, async, payment)
  - Create React project with Vite for frontend with folder structure (components, pages, store, services, utils)
  - Configure pom.xml with all required dependencies (Spring Boot, MySQL, JWT, Razorpay/Stripe, iText PDF, JUnit)
  - Configure package.json with React, Redux Toolkit, React Router, Axios, Tailwind CSS



  - Create application.properties with database, JWT, and payment gateway configuration
  - _Requirements: 11.1, 11.4, 12.5_

- [x] 2. Create database schema and connection setup



  - Write schema.sql with all table definitions (users, customers, admins, products, cart_items, orders, order_items, wishlist)
  - Implement JDBC connection pooling configuration in DatabaseConfig class
  - Create data.sql with sample data for testing (products, admin user)
  - Test database connectivity and verify tables are created correctly
  - _Requirements: 3.1, 3.4, 8.1_






- [ ] 3. Implement OOP model layer with inheritance hierarchy
  - Create abstract User base class with common fields (id, email, password, firstName, lastName, role, createdAt)
  - Implement Customer class extending User with shippingAddress and phoneNumber


  - Implement Admin class extending User with department and accessLevel
  - Create Product, Order, OrderItem, CartItem, and Wishlist model classes with proper encapsulation
  - Implement enums for UserRole, OrderStatus, PaymentStatus, AccessLevel
  - _Requirements: 6.1, 6.5, 3.1_



- [ ] 4. Build generic DAO layer with JDBC and transaction management
- [ ] 4.1 Create generic BaseDAO interface and AbstractDAO implementation
  - Define BaseDAO<T> interface with generic CRUD methods (save, findById, findAll, update, delete)







  - Implement AbstractDAO<T> with common JDBC operations using PreparedStatement
  - Add helper methods for connection management and resource cleanup
  - _Requirements: 2.5, 3.2, 3.4, 6.3_



- [ ] 4.2 Implement TransactionManager for commit/rollback operations
  - Create TransactionManager class with executeInTransaction method
  - Implement connection auto-commit disable, commit, and rollback logic
  - Add proper exception handling and resource cleanup in finally block

  - _Requirements: 8.2, 8.3, 8.4_


- [ ] 4.3 Create specific DAO implementations
  - Implement UserDAO extending AbstractDAO with authentication queries (findByEmail)
  - Implement ProductDAO with inventory operations and search queries

  - Implement OrderDAO with transaction support and date range queries

  - Implement CartDAO with user-specific queries
  - Implement WishlistDAO for wishlist management
  - _Requirements: 3.2, 3.3, 6.2_


- [-] 5. Implement service layer with interfaces and polymorphism

- [ ] 5.1 Create service interfaces demonstrating abstraction
  - Define IUserService interface with register, login, getUserProfile, updateProfile methods


  - Define IProductService interface with CRUD and search methods
  - Define ICartService interface with cart management methods
  - Define IOrderService interface with order processing and payment methods
  - _Requirements: 6.2, 6.3_

- [x] 5.2 Implement UserServiceImpl with authentication logic





  - Implement user registration with password encryption using BCryptPasswordEncoder
  - Implement login with JWT token generation
  - Add user profile retrieval and update methods
  - Use UserDAO for database operations with transaction management
  - _Requirements: 1.1, 1.2, 3.1_



- [ ] 5.3 Implement ProductServiceImpl with Collections usage
  - Implement product CRUD operations using ProductDAO
  - Use List<Product> for managing product collections

  - Use Set<String> for unique category extraction
  - Implement search and filter functionality with Java Streams
  - _Requirements: 2.1, 2.2, 3.2, 10.2_



- [ ] 5.4 Implement CartServiceImpl with thread-safe operations
  - Use ConcurrentHashMap<Long, List<CartItem>> for in-memory cart management
  - Implement add, update, remove, and clear cart operations
  - Add inventory validation before adding items to cart
  - Use CartDAO for persistence with transaction support
  - _Requirements: 3.1, 3.2, 3.3, 7.4_




- [ ] 5.5 Implement OrderServiceImpl with transaction management
  - Implement createOrder method with multi-step transaction (create order, update inventory, clear cart)
  - Add payment processing integration with PaymentGateway interface
  - Implement order retrieval and status update methods
  - Use TransactionManager for commit/rollback on success/failure


  - _Requirements: 4.1, 4.2, 4.3, 4.4, 8.2, 8.3, 8.4_

- [ ] 6. Create custom exception hierarchy
  - Create base ECommerceException class with errorCode field
  - Implement specific exceptions: UserNotFoundException, ProductNotFoundException, InsufficientStockException
  - Implement PaymentFailedException, TransactionException, UnauthorizedException
  - Create GlobalExceptionHandler with @RestControllerAdvice for centralized error handling
  - Map exceptions to appropriate HTTP status codes
  - _Requirements: 6.4, 11.5_






- [ ] 7. Implement multithreading components with ExecutorService
- [ ] 7.1 Create AsyncEmailService with thread pool
  - Initialize ExecutorService with fixed thread pool (5 threads)
  - Implement sendOrderConfirmationAsync method using executorService.submit()

  - Implement sendBulkPromotionalEmails for batch email sending
  - Add proper error handling and logging in async tasks
  - _Requirements: 7.1, 7.5, 4.4_

- [ ] 7.2 Implement OrderProcessorService for background processing
  - Create ScheduledExecutorService for periodic order processing
  - Implement processOrdersInBackground with synchronized block for inventory updates

  - Add thread-safe order status updates
  - _Requirements: 7.2, 7.4_

- [ ] 7.3 Create InventoryUpdateService with synchronization
  - Use ConcurrentHashMap<Long, AtomicInteger> for thread-safe stock cache
  - Implement ReentrantLock for inventory update synchronization
  - Create updateStockAsync method using CompletableFuture

  - Ensure proper lock acquisition and release in try-finally block
  - _Requirements: 7.3, 7.4, 7.5_

- [ ] 8. Build payment gateway integration with polymorphism
  - Create PaymentGateway interface with createPaymentOrder, verifyPayment, initiateRefund methods




  - Implement RazorpayPaymentService with Razorpay SDK integration

  - Implement StripePaymentService with Stripe SDK integration
  - Add payment configuration in application.properties
  - Test payment flow in test mode


  - _Requirements: 4.2, 6.2, 6.3_

- [ ] 9. Implement JWT authentication and security configuration
- [ ] 9.1 Create JwtTokenProvider utility class
  - Implement generateToken method with user details and expiration

  - Implement validateToken method for token verification
  - Implement getUsernameFromToken for extracting user information
  - Configure JWT secret and expiration in application.properties
  - _Requirements: 1.2, 1.3_





- [ ] 9.2 Configure Spring Security with JWT filter
  - Create SecurityConfig class with @EnableWebSecurity
  - Configure SecurityFilterChain with public and protected endpoints
  - Implement JwtAuthenticationFilter extending OncePerRequestFilter
  - Add filter before UsernamePasswordAuthenticationFilter

  - Configure CORS and disable CSRF for stateless API
  - _Requirements: 1.3, 1.4, 1.5_

- [ ] 10. Create REST controllers with proper annotations
- [ ] 10.1 Implement AuthController for authentication
  - Create POST /api/auth/register endpoint with @Valid RegisterRequest

  - Create POST /api/auth/login endpoint returning JWT token
  - Add proper error handling and validation
  - _Requirements: 1.1, 1.2, 11.5_

- [ ] 10.2 Implement ProductController with CRUD operations
  - Create GET /api/products endpoint with filtering support

  - Create GET /api/products/{id} endpoint for product details
  - Create POST /api/products endpoint with @PreAuthorize("hasRole('ADMIN')")
  - Create PUT /api/products/{id} and DELETE /api/products/{id} for admin
  - Create GET /api/products/search endpoint with query parameter
  - _Requirements: 2.1, 2.2, 2.3, 10.2_


- [ ] 10.3 Implement CartController for cart management
  - Create GET /api/cart endpoint to retrieve user cart
  - Create POST /api/cart/items endpoint to add items
  - Create PUT /api/cart/items/{id} endpoint to update quantity


  - Create DELETE /api/cart/items/{id} endpoint to remove items

  - Use @AuthenticationPrincipal to get current user
  - _Requirements: 3.1, 3.2, 3.3_

- [ ] 10.4 Implement OrderController for order processing
  - Create POST /api/orders endpoint for order creation
  - Create GET /api/orders endpoint for user order history
  - Create GET /api/orders/{id} endpoint for order details

  - Create POST /api/orders/{id}/payment endpoint for payment processing
  - _Requirements: 4.1, 4.2, 4.4_

- [ ] 10.5 Implement AdminController for analytics
  - Create GET /api/admin/analytics endpoint with date range parameters
  - Create GET /api/admin/orders endpoint for all orders
  - Add @PreAuthorize("hasRole('ADMIN')") to all methods
  - _Requirements: 10.4_


- [ ] 11. Build servlet module for admin operations
- [ ] 11.1 Create AdminLoginServlet with session management
  - Implement doGet method to display login form
  - Implement doPost method to authenticate admin and create HttpSession
  - Store admin user in session with setAttribute
  - Redirect to admin dashboard on success

  - _Requirements: 5.1, 5.2, 5.3, 5.4_

- [ ] 11.2 Create ProductUploadServlet with form handling
  - Implement doGet method to display product upload form with session validation
  - Implement doPost method to parse form data and handle file upload
  - Use ProductDAO to persist product with JDBC transaction
  - Redirect with success/error message using request attributes
  - _Requirements: 5.1, 5.2, 5.3, 5.4, 5.5_


- [ ] 11.3 Configure servlet registration in Spring Boot
  - Create ServletRegistrationBean for AdminLoginServlet
  - Create ServletRegistrationBean for ProductUploadServlet
  - Map servlets to /admin/login and /admin/product-upload URLs
  - _Requirements: 5.1_

- [x] 12. Implement advanced features

- [ ] 12.1 Create RecommendationService for product suggestions
  - Implement collaborative filtering based on user order history
  - Use Set<String> to extract unique categories from user purchases
  - Filter and sort products using Java Streams and Comparator
  - Return top 10 recommended products
  - _Requirements: 10.1_

- [x] 12.2 Implement InvoiceService for PDF generation

  - Add iText dependency to pom.xml
  - Create generateInvoicePDF method using Document and PdfWriter
  - Add order details, customer information, and items table
  - Return PDF as byte array for download
  - _Requirements: 10.3_

- [x] 12.3 Create AnalyticsService for admin dashboard

  - Implement getDashboardMetrics with date range parameters
  - Calculate total sales, order count, and revenue trends
  - Use Map<LocalDate, BigDecimal> for daily revenue tracking
  - Query top-selling products from OrderDAO
  - _Requirements: 10.4_


- [ ] 12.4 Implement ProductSearchService with dynamic queries
  - Build dynamic SQL queries based on SearchCriteria
  - Add text search with LIKE operator for name and description
  - Add filters for category, price range, and sorting
  - Use PreparedStatement with parameterized queries
  - _Requirements: 10.2_

- [ ] 12.5 Create VerificationService for email OTP
  - Use ConcurrentHashMap to store OTP codes with email as key
  - Implement sendVerificationOTP with 6-digit random OTP generation
  - Use CompletableFuture.delayedExecutor for OTP expiration (10 minutes)
  - Implement verifyOTP to validate and remove OTP from store
  - _Requirements: 10.5_

- [ ] 13. Build React frontend with Redux Toolkit
- [x] 13.1 Set up Redux store and slices




  - Create store.js with configureStore
  - Implement authSlice with login, logout, and token management
  - Implement productSlice with fetchProducts async thunk
  - Implement cartSlice with add, update, remove actions
  - Implement orderSlice for order history

  - _Requirements: 9.2, 9.3_

- [ ] 13.2 Create API service layer with Axios
  - Configure Axios instance with base URL and interceptors
  - Add request interceptor to attach JWT token from localStorage
  - Implement authService with register and login methods
  - Implement productService with getAllProducts, getProductById, searchProducts

  - Implement cartService with cart CRUD operations
  - Implement orderService with createOrder and getOrders
  - _Requirements: 9.4_

- [ ] 13.3 Build authentication components
  - Create LoginForm component with email and password fields
  - Create SignupForm component with validation

  - Implement ProtectedRoute component for route guarding
  - Create LoginPage and SignupPage with form integration
  - Dispatch Redux actions on form submission
  - _Requirements: 1.1, 1.2, 9.4_

- [ ] 13.4 Implement product browsing components
  - Create ProductCard component with image, name, price, and add-to-cart button

  - Create ProductList component with grid layout
  - Create ProductFilter component for category and price filtering
  - Create SearchBar component with debounced search
  - Create ProductDetails component with full product information
  - Create ProductListPage and ProductDetailsPage
  - _Requirements: 2.1, 10.2, 9.5_


- [ ] 13.5 Build cart and checkout components
  - Create CartItem component with quantity controls and remove button
  - Create CartSummary component with total calculation
  - Create CartPage with list of cart items
  - Create CheckoutForm with shipping address input
  - Create OrderSummary component for review before payment
  - Create CheckoutPage integrating all checkout components
  - _Requirements: 3.1, 3.2, 3.3, 4.1_

- [ ] 13.6 Implement payment and order components
  - Create PaymentForm component with Razorpay/Stripe integration
  - Create PaymentSuccess component for confirmation
  - Create PaymentPage with payment gateway initialization
  - Create OrderList component displaying order history
  - Create OrderDetails component with order items and status
  - Create OrdersPage for viewing past orders
  - _Requirements: 4.2, 4.4_

- [ ] 13.7 Create common UI components
  - Create Header component with logo and navigation
  - Create Navbar component with links and user menu
  - Create Footer component with copyright and links
  - Create LoadingSpinner component for async operations
  - Create ErrorAlert component for displaying errors
  - _Requirements: 9.4, 9.5_

- [ ] 13.8 Implement routing with React Router
  - Configure BrowserRouter in App.jsx
  - Define routes for all pages (Home, Login, Signup, Products, ProductDetails, Cart, Checkout, Payment, Orders)
  - Wrap protected routes with ProtectedRoute component
  - Add navigation between pages
  - _Requirements: 9.3_

- [ ] 13.9 Style components with Tailwind CSS
  - Configure Tailwind CSS in vite.config.js
  - Apply responsive design classes to all components
  - Implement mobile-first design approach
  - Add hover effects and animations
  - Ensure consistent color scheme and typography
  - _Requirements: 9.4, 9.5_

- [ ] 14. Create documentation and deployment files
- [ ] 14.1 Write comprehensive README files
  - Create main README.md with project overview and setup instructions
  - Create backend/README.md with backend-specific instructions
  - Create frontend/README.md with frontend-specific instructions
  - Include prerequisites, installation steps, and running commands
  - _Requirements: 12.1, 12.5_

- [ ] 14.2 Create API documentation
  - Write API_DOCUMENTATION.md with all endpoint descriptions
  - Document request/response formats for each endpoint
  - Include authentication requirements and headers
  - Add example requests and responses
  - Document error codes and messages
  - _Requirements: 12.3, 12.4_

- [ ] 14.3 Create Postman collection
  - Export Postman collection with all API endpoints
  - Organize requests by feature (Auth, Products, Cart, Orders, Admin)
  - Add environment variables for base URL and token
  - Include sample request bodies
  - Save as postman_collection.json
  - _Requirements: 12.3_

- [ ] 14.4 Write deployment guide
  - Create DEPLOYMENT.md with production deployment steps
  - Document MySQL database setup and configuration
  - Include environment variable configuration
  - Add instructions for building and deploying backend (JAR file)
  - Add instructions for building and deploying frontend (static files)
  - _Requirements: 12.1_

- [ ] 14.5 Write unit and integration tests
  - Create unit tests for service layer with mocked DAOs
  - Create integration tests for controllers using MockMvc
  - Test DAO layer with in-memory H2 database
  - Test exception handling scenarios
  - Create frontend component tests with React Testing Library
  - Test Redux reducers and actions
  - _Requirements: 11.4_

- [ ] 15. Final integration and testing
  - Build backend with Maven (mvn clean install)
  - Build frontend with npm (npm run build)
  - Test complete user flow: register → login → browse products → add to cart → checkout → payment
  - Test admin flow: login via servlet → upload product → view analytics
  - Verify multithreading: check email sending in background
  - Verify transaction management: test rollback on payment failure
  - Test all API endpoints with Postman collection
  - Verify error handling and exception responses
  - Test responsive design on mobile and desktop
  - _Requirements: 11.4, 12.5_
