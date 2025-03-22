INSERT INTO "Order" (reference, created_at, status)
VALUES ('ORDER-001', '2025-03-01 10:00:00', 'CREATED'),
       ('ORDER-002', '2025-03-01 10:15:00', 'CONFIRMED'),
       ('ORDER-003', '2025-03-01 10:30:00', 'IN_PREPARATION')
on conflict do nothing;