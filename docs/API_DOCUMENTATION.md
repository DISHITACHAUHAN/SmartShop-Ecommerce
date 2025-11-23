# API Documentation

## Base URL
```
http://localhost:8080/api
```

## Authentication

All protected endpoints require a JWT token in the Authorization header:
```
Authorization: Bearer <your_jwt_token>
```

---

## Auth Endpoints

### Register User
**POST** `/auth/register`

**Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "password": "password123",
  "phoneNumber": "+1234567890",
  "shippingAddress": "123 Main St, City, State 12345"
}
```

**Response:** `201 Created`
```json
{
  "id": 1,
  "email": "john@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "role": "CUSTOMER",
  "phoneNumber": "+1234567890",
  "shippingAddress": "123 Main St, City, State 12345",
  "createdAt": "2024-01-01T10:00:00"
}
```

### Login
**POST** `/auth/login`

**Request Body:**
```json
{
  "email": "john@example.com",
  "password": "password123"
}
```

**Response:** `200 OK`
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 1,
    "email": "john@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "role": "CUSTOMER"
  }
}
```

---

## Product Endpoints

### Get All Products
**GET** `/products`

**Query Parameters:**
- `category` (optional) - Filter by category
- `minPrice` (optional) - Minimum price
- `maxPrice` (optional) - Maximum price

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "name": "Laptop",
    "description": "High-performance laptop",
    "price": 999.99,
    "stockQuantity": 50,
    "category": "Electronics",
    "imageUrl": "https://example.com/laptop.jpg",
    "createdAt": "2024-01-01T10:00:00"
  }
]
```

### Search Products
**GET** `/products/search?q=laptop`

### Get Product by ID
**GET** `/products/{id}`

### Create Product (Admin)
**POST** `/products`

**Request Body:**
```json
{
  "name": "Smartphone",
  "description": "Latest smartphone",
  "price": 699.99,
  "stockQuantity": 100,
  "category": "Electronics",
  "imageUrl": "https://example.com/phone.jpg"
}
```

---

## Cart Endpoints

### Get Cart
**GET** `/cart/{userId}`

**Response:** `200 OK`
```json
{
  "items": [
    {
      "id": 1,
      "userId": 1,
      "productId": 1,
      "productName": "Laptop",
      "quantity": 2,
      "price": 999.99,
      "imageUrl": "https://example.com/laptop.jpg"
    }
  ],
  "totalAmount": 1999.98
}
```

### Add to Cart
**POST** `/cart/items`

**Request Body:**
```json
{
  "userId": 1,
  "productId": 1,
  "quantity": 2
}
```

### Update Cart Item
**PUT** `/cart/items/{itemId}`

**Request Body:**
```json
{
  "userId": 1,
  "quantity": 3
}
```

### Remove from Cart
**DELETE** `/cart/items/{itemId}?userId={userId}`

---

## Order Endpoints

### Create Order
**POST** `/orders`

**Request Body:**
```json
{
  "userId": 1,
  "shippingAddress": "123 Main St, City, State 12345"
}
```

**Response:** `201 Created`
```json
{
  "id": 1,
  "userId": 1,
  "items": [
    {
      "id": 1,
      "productId": 1,
      "productName": "Laptop",
      "quantity": 2,
      "price": 999.99
    }
  ],
  "totalAmount": 1999.98,
  "status": "PENDING",
  "paymentStatus": "PENDING",
  "shippingAddress": "123 Main St, City, State 12345",
  "orderDate": "2024-01-01T10:00:00"
}
```

### Process Payment
**POST** `/orders/{id}/payment`

**Request Body:**
```json
{
  "paymentId": "pay_abc123xyz"
}
```

### Get User Orders
**GET** `/orders/user/{userId}`

---

## Error Responses

All errors follow this format:

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Product not found with ID: 999",
  "errorCode": "PROD_001",
  "timestamp": "2024-01-01T10:00:00"
}
```

### Common Error Codes
- `USER_001` - User not found
- `USER_002` - User already exists
- `USER_003` - Invalid credentials
- `PROD_001` - Product not found
- `PROD_002` - Insufficient stock
- `ORD_001` - Order not found
- `PAY_001` - Payment failed
- `DB_001` - Transaction failed

---

## Testing with cURL

### Register
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"firstName":"John","lastName":"Doe","email":"john@example.com","password":"password123"}'
```

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"john@example.com","password":"password123"}'
```

### Get Products
```bash
curl http://localhost:8080/api/products
```

### Add to Cart (with JWT)
```bash
curl -X POST http://localhost:8080/api/cart/items \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{"userId":1,"productId":1,"quantity":2}'
```
