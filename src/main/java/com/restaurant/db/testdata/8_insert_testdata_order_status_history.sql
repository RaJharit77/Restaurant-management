INSERT INTO Order_Status_History (order_id, status, changed_at)
VALUES (1, 'CREATED', '2025-03-01 10:00:00'),        -- ORDER-001 : CREATED
       (1, 'CONFIRMED', '2025-03-01 10:05:00'),      -- ORDER-001 : CONFIRMED
       (2, 'CONFIRMED', '2025-03-01 10:15:00'),      -- ORDER-002 : CONFIRMED
       (2, 'IN_PREPARATION', '2025-03-01 10:20:00'), -- ORDER-002 : IN_PREPARATION
       (3, 'IN_PREPARATION', '2025-03-01 10:30:00'), -- ORDER-003 : IN_PREPARATION
       (3, 'COMPLETED', '2025-03-01 10:45:00')
on conflict do nothing; -- ORDER-003 : COMPLETED