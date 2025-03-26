CREATE TABLE Stock_Movement IF NOT EXISTS
(
    movement_id   SERIAL PRIMARY KEY,
    ingredient_id INT REFERENCES Ingredient (ingredient_id),
    movement_type movement_type  NOT NULL,
    quantity      DECIMAL(10, 2) NOT NULL,
    unit          unit_type      NOT NULL,
    movement_date TIMESTAMP      NOT NULL
);