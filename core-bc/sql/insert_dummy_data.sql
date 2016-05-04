
--- Insert supplies.
INSERT INTO vgr_glasogonbidrag_supplier
VALUES ('Synsam Alfa'),
       ('Synsam Beta'),
       ('Synsam Gamma'),
       ('Synsam Delta'),
       ('Specsavers Alfa'),
       ('Specsavers Beta'),
       ('Specsavers Gamma'),
       ('Specsavers Delta'),
       ('Wasaoptik'),
       ('Direktoptik'),
       ('Hendéns');

--- Insert invoices.
INSERT INTO vgr_glasogonbidrag_invoice
    (user_id, group_id, company_id, amount, vat, invoice_date, invoice_number,
     verification_number, supplier_name)
VALUES (1, 1, 20202, 10000, 100,
        '2016-02-16', '10001', 'E510382', 'Direktoptik');