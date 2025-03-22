CREATE TABLE Ingredient IF NOT EXIST
(
    ingredient_id   SERIAL PRIMARY KEY,
    name            VARCHAR(255)     NOT NULL,
    unit_price      DOUBLE PRECISION NOT NULL,
    unit            unit_type        NOT NULL,
    update_datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);