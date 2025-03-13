CREATE TYPE unit_type AS ENUM ('G', 'L', 'U');
CREATE TYPE movement_type AS ENUM ('ENTRY', 'EXIT');
CREATE TYPE order_status AS ENUM ('CREATED', 'CONFIRMED', 'IN_PREPARATION', 'COMPLETED', 'SERVED');
CREATE TYPE dish_status AS ENUM ('CREATED', 'CONFIRMED', 'IN_PREPARATION', 'COMPLETED', 'SERVED');

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

CREATE TABLE "Order" (
    order_id SERIAL PRIMARY KEY,
    reference VARCHAR(255) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status order_status DEFAULT 'CREATED'
);

CREATE TABLE Dish_Order (
    dish_order_id SERIAL PRIMARY KEY,
    order_id INT REFERENCES "Order"(order_id) ON DELETE CASCADE,
    dish_id INT REFERENCES Dish(dish_id) ON DELETE CASCADE,
    quantity INT NOT NULL,
    status dish_status DEFAULT 'CREATED'
);

CREATE TABLE Status_History (
    status_history_id SERIAL PRIMARY KEY,
    dish_order_id INT REFERENCES Dish_Order(dish_order_id) ON DELETE CASCADE,
    status dish_status NOT NULL,
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Order_Status_History (
    order_status_history_id SERIAL PRIMARY KEY,
    order_id INT REFERENCES "Order"(order_id) ON DELETE CASCADE,
    status order_status NOT NULL,
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
