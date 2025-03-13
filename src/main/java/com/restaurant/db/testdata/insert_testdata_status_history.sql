INSERT INTO Status_History (dish_order_id, status, changed_at)
VALUES
    (1, 'CREATED', '2025-03-01 10:00:00'),    -- Dish_Order 1 : CREATED
    (1, 'CONFIRMED', '2025-03-01 10:05:00'),  -- Dish_Order 1 : CONFIRMED
    (2, 'CREATED', '2025-03-01 10:00:00'),    -- Dish_Order 2 : CREATED
    (2, 'IN_PREPARATION', '2025-03-01 10:10:00'), -- Dish_Order 2 : IN_PREPARATION
    (3, 'CONFIRMED', '2025-03-01 10:15:00'),   -- Dish_Order 3 : CONFIRMED
    (3, 'COMPLETED', '2025-03-01 10:30:00');   -- Dish_Order 3 : COMPLETED