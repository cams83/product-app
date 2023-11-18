CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(255) NOT NULL,
    cost DECIMAL(19, 2) NOT NULL,
    price DECIMAL(19, 2) NOT NULL
);