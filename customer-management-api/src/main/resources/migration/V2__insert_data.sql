-- =============================================
-- DML - Datos de prueba
-- =============================================

INSERT INTO customers (identification_type, identification_number, names, surnames, email, date_of_birth, created_at)
VALUES
    ('CC', '1010101010', 'Carlos',  'Ramírez',  'carlos.ramirez@email.com',  '1990-05-15', NOW()),
    ('CC', '2020202020', 'Lucía',   'Gómez',    'lucia.gomez@email.com',     '1985-08-22', NOW()),
    ('CE', '3030303030', 'Andrés',  'Martínez', 'andres.martinez@email.com', '1992-11-30', NOW());

INSERT INTO accounts (account_type, account_number, account_state, balance, gmf_exempt, customer_id, created_at)
VALUES
    ('AHORROS',   4600000001, 'ACTIVA',    1500000.00, FALSE, (SELECT id FROM customers WHERE identification_number = '1010101010'), NOW()),
    ('CORRIENTE', 4600000002, 'ACTIVA',    3200000.00, TRUE,  (SELECT id FROM customers WHERE identification_number = '2020202020'), NOW()),
    ('AHORROS',   4600000003, 'INACTIVA',       0.00, FALSE, (SELECT id FROM customers WHERE identification_number = '3030303030'), NOW());

INSERT INTO transactions (transaction_type, amount, source_account_id, destination_account_id, transaction_date)
VALUES
    ('CONSIGNACION',  500000.00, NULL,
     (SELECT id FROM accounts WHERE account_number = 4600000001), NOW()),
    ('RETIRO',        200000.00,
     (SELECT id FROM accounts WHERE account_number = 4600000002), NULL, NOW()),
    ('TRANSFERENCIA', 300000.00,
     (SELECT id FROM accounts WHERE account_number = 4600000002),
     (SELECT id FROM accounts WHERE account_number = 4600000001), NOW());