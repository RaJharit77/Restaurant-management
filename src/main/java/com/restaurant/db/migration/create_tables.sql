CREATE TYPE unit_type AS ENUM ('G', 'L', 'U');

CREATE TABLE Ingredient (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    unit unit_type NOT NULL,
    update_datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Dish (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL
);

CREATE TABLE Dish_Ingredient (
    dish_id INT REFERENCES Dish(id),
    ingredient_id INT REFERENCES Ingredient(id),
    quantity DECIMAL(10, 2) NOT NULL,
    unit unit_type NOT NULL,
    PRIMARY KEY (dish_id, ingredient_id)
);