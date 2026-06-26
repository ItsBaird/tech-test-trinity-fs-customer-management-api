-- =============================================
-- DML - Datos de prueba
-- =============================================

INSERT INTO customers (identification_type, identification_number, names, surnames, email, date_of_birth, created_at)
VALUES
    ('CC', '1029880567', 'Carlos',  'Ramírez',  'carlos.ramirez@email.com',  '1990-05-15', NOW()),
    ('CC', '36501569', 'Lucía',   'Gómez',    'lucia.gomez@email.com',     '1985-08-22', NOW()),
    ('CE', '119876789', 'Andrés',  'Martínez', 'andres.martinez@email.com', '2006-11-30', NOW());

INSERT INTO accounts (account_type, account_number, account_state, balance, gmf_exempt, customer_id, created_at)
VALUES
    ('AHORROS',   5300000001, 'ACTIVA',    1500000.00, FALSE, (SELECT id FROM customers WHERE identification_number = '1029880567'), NOW()),
    ('CORRIENTE', 3300000001, 'ACTIVA',    3200000.00, TRUE,  (SELECT id FROM customers WHERE identification_number = '36501569'), NOW()),
    ('AHORROS',   5300000002, 'INACTIVA',       0.00, FALSE, (SELECT id FROM customers WHERE identification_number = '119876789'), NOW());

INSERT INTO transactions (transaction_type, amount, source_account_id, destination_account_id, transaction_date)
VALUES
    ('CONSIGNACION',  500000.00, NULL,
     (SELECT id FROM accounts WHERE account_number = 5300000001), NOW()),
    ('RETIRO',        200000.00,
     (SELECT id FROM accounts WHERE account_number = 3300000001), NULL, NOW()),
    ('TRANSFERENCIA', 300000.00,
     (SELECT id FROM accounts WHERE account_number = 3300000001),
     (SELECT id FROM accounts WHERE account_number = 5300000001), NOW());