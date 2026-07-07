-- Schema for `products` table
-- Run in PostgreSQL: psql -U postgres -d enterprise -f db/schema-products.sql

DROP TABLE IF EXISTS products;

CREATE TABLE products (
  id BIGSERIAL PRIMARY KEY,
  name TEXT NOT NULL,
  description TEXT,
  price NUMERIC(12,2) NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

-- Sample data
INSERT INTO products (name, description, price) VALUES
('Lapicero', 'Lapicero azul', 1.50),
('Cuaderno', 'Cuaderno A4', 3.20),
('Boligrafo', 'Boligrafo negro', 2.00);
