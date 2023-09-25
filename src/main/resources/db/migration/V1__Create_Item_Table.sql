CREATE TABLE IF NOT EXISTS item (
    name VARCHAR(255) NOT NULL,
    price NUMERIC(5, 2),
    quantity INTEGER,
    is_sold BOOLEAN NOT NULL
);