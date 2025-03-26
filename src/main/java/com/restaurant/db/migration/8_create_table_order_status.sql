CREATE TABLE Order_Status IF NOT EXISTS
(
    order_status_id SERIAL PRIMARY KEY,
    order_id                INT REFERENCES "Order" (order_id) ON DELETE CASCADE,
    status                  status_type NOT NULL,
    changed_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
