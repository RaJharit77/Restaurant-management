CREATE TABLE Dish IF NOT EXISTS
(
    dish_id    SERIAL PRIMARY KEY,
    name       VARCHAR(255)   NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL
);