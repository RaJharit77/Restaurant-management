CREATE TABLE "Order" IF NOT EXISTS
(
    order_id   SERIAL PRIMARY KEY,
    reference  VARCHAR(255) UNIQUE NOT NULL,
    created_at TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    status     status_type DEFAULT 'CREATED'
);