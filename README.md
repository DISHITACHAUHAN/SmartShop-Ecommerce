# E-Commerce Platform

A production-ready full-stack E-Commerce platform built with Spring Boot and React, demonstrating advanced Java concepts including OOP principles, JDBC transactions, multithreading, and servlet integration.

## 🚀 Technology Stack

### Backend
- **Spring Boot 3.2.0** - REST APIs, MVC architecture
- **Java 17** - OOP, Collections, Generics, Multithreading
- **MySQL 8.x** - Database with JDBC + JPA/Hibernate
- **JWT (jjwt 0.12.3)** - Authentication and authorization
- **Razorpay/Stripe** - Payment gateway integration
- **iText 8.0.2** - PDF invoice generation
- **HikariCP** - Connection pooling
- **BCrypt** - Password encryption
- **Maven** - Build tool

### Frontend
- **React 18** - UI library
- **Redux Toolkit** - State management
- **React Router 6** - Navigation
- **Axios** - HTTP client
- **Tailwind CSS** - Styling
- **Vite** - Build tool

## Features

### Core Features
- User authentication and authorization (JWT)
- Product catalog management (CRUD operations)
- Shopping cart functionality
- Order processing and payment integration
- Role-based access control (Customer/Admin)

### Advanced Features
- Product recommendations
- PDF invoice generation
- Admin analytics dashboard
- Product search and filtering
- Email notifications (async)
- OTP verification

### Technical Highlights
- **OOP Implementation**: Inheritance, polymorphism, abstraction, encapsulation
- **JDBC Transactions**: Manual transaction management with commit/rollback
- **Multithreading**: ExecutorService for async email sending and background processing
- **Servlet Module**: Legacy servlet support for admin operations
- **Collections & Generics**: Extensive use of Java Collections Framework
- **Thread Synchronization**: ReentrantLock and synchronized blocks

## Project Structure

```
ecommerce-platform/
├── backend/
│   ├── src/main/java/com/ecommerce/
│   │   ├── controller/      # REST controllers
│   │   ├── service/         # Business logic
│   │   ├── dao/             # Data access layer
│   │   ├── model/           # Entity models
│   │   ├── config/          # Configuration classes
│   │   ├── exception/       # Custom exceptions
│   │   ├── util/            # Utility classes
│   │   ├── servlet/         # Servlet module
│   │   ├── async/           # Async services
│   │   └── payment/         # Payment integration
│   └── src/main/resources/
│       ├── application.properties
│       ├── schema.sql
│       └── data.sql
├── frontend/
│   └── src/
│       ├── components/      # React components
│       ├── pages/           # Page components
│       ├── store/           # Redux store
│       ├── services/        # API services
│       └── utils/           # Utility functions
└── docs/
    ├── API_DOCUMENTATION.md
    └── DEPLOYMENT.md
```

## Prerequisites

- Java 17 or higher
- Node.js 18 or higher
- MySQL 8.x
- Maven 3.6+
- npm or yarn

## Setup Instructions

### Database Setup

1. Install MySQL and create database:
```sql
CREATE DATABASE ecommerce_db;
```

2. Update database credentials in `backend/src/main/resources/application.properties`:
```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Backend Setup

1. Navigate to backend directory:
```bash
cd backend
```

2. Install dependencies and build:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

### Frontend Setup

1. Navigate to frontend directory:
```bash
cd frontend
```

2. Install dependencies:
```bash
npm install
```

3. Start development server:
```bash
npm run dev
```

The frontend will start on `http://localhost:3000`

## 📡 API Endpoints

### Authentication
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login (returns JWT token)
- `GET /api/auth/profile/{userId}` - Get user profile

### Products
- `GET /api/products` - Get all products (supports filters: category, minPrice, maxPrice)
- `GET /api/products/{id}` - Get product by ID
- `GET /api/products/search?q={query}` - Search products
- `GET /api/products/categories` - Get all categories
- `POST /api/products` - Create product (Admin only)
- `PUT /api/products/{id}` - Update product (Admin only)
- `DELETE /api/products/{id}` - Delete product (Admin only)

### Cart
- `GET /api/cart/{userId}` - Get user cart
- `POST /api/cart/items` - Add item to cart
- `PUT /api/cart/items/{itemId}` - Update cart item quantity
- `DELETE /api/cart/items/{itemId}?userId={userId}` - Remove from cart
- `DELETE /api/cart/{userId}` - Clear cart

### Orders
- `POST /api/orders` - Create order from cart
- `GET /api/orders/user/{userId}` - Get user orders
- `GET /api/orders/{id}` - Get order details
- `POST /api/orders/{id}/payment` - Process payment
- `PUT /api/orders/{id}/status` - Update order status (Admin)
- `GET /api/orders` - Get all orders (Admin)

### Admin
- `GET /api/admin/analytics` - Get dashboard analytics
- `GET /api/admin/orders` - Get all orders

### Servlets (Legacy Admin Interface)
- `GET/POST /admin/login` - Admin login with session management
- `GET/POST /admin/product-upload` - Product upload form

## Testing

### Backend Tests
```bash
cd backend
mvn test
```

### Frontend Tests
```bash
cd frontend
npm test
```

## Building for Production

### Backend
```bash
cd backend
mvn clean package
java -jar target/ecommerce-platform-1.0.0.jar
```

### Frontend
```bash
cd frontend
npm run build
```

## Configuration

### Payment Gateway

Update payment gateway credentials in `application.properties`:

**Razorpay:**
```properties
razorpay.key.id=your_key_id
razorpay.key.secret=your_key_secret
```

**Stripe:**
```properties
stripe.api.key=your_api_key
```

### Email Configuration

Update email settings in `application.properties`:
```properties
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
`
