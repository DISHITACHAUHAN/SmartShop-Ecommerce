-- Sample data for E-Commerce Platform

-- Insert admin user (password: admin123)
INSERT INTO users (email, password, first_name, last_name, role) VALUES
('admin@ecommerce.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Admin', 'User', 'ADMIN');

-- Insert admin details
INSERT INTO admins (user_id, department, access_level) VALUES
(1, 'Management', 'FULL');

-- Insert sample customer (password: customer123)
INSERT INTO users (email, password, first_name, last_name, role) VALUES
('customer@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'John', 'Doe', 'CUSTOMER');

-- Insert customer details
INSERT INTO customers (user_id, phone_number, shipping_address) VALUES
(2, '+1234567890', '123 Main St, City, State 12345');

-- Insert sample products
INSERT INTO products (name, description, price, stock_quantity, category, image_url) VALUES
('Laptop', 'High-performance laptop with 16GB RAM and 512GB SSD', 999.99, 50, 'Electronics', 'https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=400&h=400&fit=crop'),
('Smartphone', 'Latest smartphone with 5G connectivity', 699.99, 100, 'Electronics', 'https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=400&h=400&fit=crop'),
('Wireless Headphones', 'Noise-cancelling wireless headphones', 199.99, 75, 'Electronics', 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=400&h=400&fit=crop'),
('Running Shoes', 'Comfortable running shoes for all terrains', 89.99, 120, 'Sports', 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=400&h=400&fit=crop'),
('Backpack', 'Durable backpack with multiple compartments', 49.99, 200, 'Accessories', 'https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=400&h=400&fit=crop'),
('Smart Watch', 'Fitness tracking smart watch', 299.99, 60, 'Electronics', 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=400&h=400&fit=crop'),
('Coffee Maker', 'Programmable coffee maker with timer', 79.99, 80, 'Home', 'https://images.unsplash.com/photo-1517668808822-9ebb02f2a0e6?w=400&h=400&fit=crop'),
('Yoga Mat', 'Non-slip yoga mat with carrying strap', 29.99, 150, 'Sports', 'https://images.unsplash.com/photo-1601925260368-ae2f83cf8b7f?w=400&h=400&fit=crop'),
('Desk Lamp', 'LED desk lamp with adjustable brightness', 39.99, 90, 'Home', 'https://images.unsplash.com/photo-1507473885765-e6ed057f782c?w=400&h=400&fit=crop'),
('Water Bottle', 'Insulated stainless steel water bottle', 24.99, 250, 'Accessories', 'https://images.unsplash.com/photo-1602143407151-7111542de6e8?w=400&h=400&fit=crop'),
('Bluetooth Speaker', 'Portable Bluetooth speaker with bass boost', 59.99, 110, 'Electronics', 'https://images.unsplash.com/photo-1608043152269-423dbba4e7e1?w=400&h=400&fit=crop'),
('Gaming Mouse', 'RGB gaming mouse with programmable buttons', 49.99, 85, 'Electronics', 'https://images.unsplash.com/photo-1527864550417-7fd91fc51a46?w=400&h=400&fit=crop'),
('Notebook Set', 'Set of 3 premium notebooks', 19.99, 300, 'Stationery', 'https://images.unsplash.com/photo-1544816155-12df9643f363?w=400&h=400&fit=crop'),
('Sunglasses', 'UV protection polarized sunglasses', 89.99, 140, 'Accessories', 'https://images.unsplash.com/photo-1511499767150-a48a237f0083?w=400&h=400&fit=crop'),
('Fitness Tracker', 'Activity and sleep tracking band', 79.99, 95, 'Electronics', 'https://images.unsplash.com/photo-1575311373937-040b8e1fd5b6?w=400&h=400&fit=crop');
