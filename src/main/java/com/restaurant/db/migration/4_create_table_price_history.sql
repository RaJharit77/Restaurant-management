CREATE TABLE Price_History IF NOT EXISTS
(
    price_history_id SERIAL PRIMARY KEY,
    ingredient_id    INT REFERENCES Ingredient (ingredient_id),
    price            DOUBLE PRECISION NOT NULL,
    date             TIMESTAMP        NOT NULL
);