INSERT INTO Dish_Order (order_id, dish_id, quantity, status)
VALUES
    (1, 1, 2, 'CREATED'),   -- ORDER-001 : 2 Hot Dogs
    (1, 2, 1, 'CREATED'),   -- ORDER-001 : 1 Cheeseburger
    (2, 3, 1, 'CONFIRMED'), -- ORDER-002 : 1 Hamburger
    (2, 4, 1, 'CONFIRMED'), -- ORDER-002 : 1 Salade
    (3, 1, 3, 'IN_PREPARATION'); -- ORDER-003 : 3 Hot Dogs