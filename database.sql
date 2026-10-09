-- Final Exam: JSP/Servlet + MySQL Product Management
-- Chay file nay trong MySQL Workbench truoc khi truy cap trang web.
CREATE DATABASE IF NOT EXISTS product_management_db
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE product_management_db;

CREATE TABLE IF NOT EXISTS products (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    price DECIMAL(12,2) NOT NULL,
    quantity INT NOT NULL,
    CONSTRAINT chk_products_price_nonnegative CHECK (price >= 0),
    CONSTRAINT chk_products_quantity_nonnegative CHECK (quantity >= 0)
) ENGINE=InnoDB;

-- Du lieu mau, chay nhieu lan khong tao trung theo ten san pham mau.
INSERT INTO products (name, price, quantity)
SELECT 'Laptop Dell', 15990000.00, 12
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name='Laptop Dell');
INSERT INTO products (name, price, quantity)
SELECT 'Chuột không dây', 250000.00, 40
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name='Chuột không dây');
INSERT INTO products (name, price, quantity)
SELECT 'Bàn phím cơ', 890000.00, 25
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name='Bàn phím cơ');

-- SELECT id, name, price, quantity FROM products ORDER BY id DESC;
