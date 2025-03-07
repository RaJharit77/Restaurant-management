CREATE TYPE unit_type AS ENUM ('G', 'L', 'U');
CREATE TYPE movement_type AS ENUM ('ENTRY', 'EXIT');

CREATE TABLE Ingredient (
    ingredient_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    unit_price DOUBLE PRECISION NOT NULL,
    unit unit_type NOT NULL,
    update_datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Dish (
    dish_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL
);

CREATE TABLE Dish_Ingredient (
    dish_id INT REFERENCES Dish(dish_id),
    ingredient_id INT REFERENCES Ingredient(ingredient_id),
    quantity DECIMAL(10, 2) NOT NULL,
    unit unit_type NOT NULL,
    PRIMARY KEY (dish_id, ingredient_id)
);

CREATE TABLE Price_History (
    price_history_id SERIAL PRIMARY KEY,
    ingredient_id INT REFERENCES Ingredient(ingredient_id),
    price DOUBLE PRECISION NOT NULL,
    date TIMESTAMP NOT NULL
);

CREATE TABLE Stock_Movement (
    movement_id SERIAL PRIMARY KEY,
    ingredient_id INT REFERENCES Ingredient(ingredient_id),
    movement_type movement_type NOT NULL,
    quantity DECIMAL(10, 2) NOT NULL,
    unit unit_type NOT NULL,
    movement_date TIMESTAMP NOT NULL
);