INSERT INTO vgr_glasogonbidrag_supplier (name, user_id, group_id, company_id, create_date, modified_date) VALUES ('Synsam Alfa', 20159, 20195, 20155, '2015-01-01 08:00:00.000', '2015-01-01 08:00:00.000');
INSERT INTO vgr_glasogonbidrag_supplier (name, user_id, group_id, company_id, create_date, modified_date) VALUES ('Synsam Beta', 20159, 20195, 20155, '2015-01-01 08:00:00.000', '2015-01-01 08:00:00.000');
INSERT INTO vgr_glasogonbidrag_supplier (name, user_id, group_id, company_id, create_date, modified_date) VALUES ('Synsam Gamma', 20159, 20195, 20155, '2015-01-01 08:00:00.000', '2015-01-01 08:00:00.000');
INSERT INTO vgr_glasogonbidrag_supplier (name, user_id, group_id, company_id, create_date, modified_date) VALUES ('Synsam Delta', 20159, 20195, 20155, '2015-01-01 08:00:00.000', '2015-01-01 08:00:00.000');
INSERT INTO vgr_glasogonbidrag_supplier (name, user_id, group_id, company_id, create_date, modified_date) VALUES ('Specsavers Alfa', 20159, 20195, 20155, '2015-01-01 08:00:00.000', '2015-01-01 08:00:00.000');
INSERT INTO vgr_glasogonbidrag_supplier (name, user_id, group_id, company_id, create_date, modified_date) VALUES ('Specsavers Beta', 20159, 20195, 20155, '2015-01-01 08:00:00.000', '2015-01-01 08:00:00.000');
INSERT INTO vgr_glasogonbidrag_supplier (name, user_id, group_id, company_id, create_date, modified_date) VALUES ('Specsavers Gamma', 20159, 20195, 20155, '2015-01-01 08:00:00.000', '2015-01-01 08:00:00.000');
INSERT INTO vgr_glasogonbidrag_supplier (name, user_id, group_id, company_id, create_date, modified_date) VALUES ('Specsavers Delta', 20159, 20195, 20155, '2015-01-01 08:00:00.000', '2015-01-01 08:00:00.000');
INSERT INTO vgr_glasogonbidrag_supplier (name, user_id, group_id, company_id, create_date, modified_date) VALUES ('Wasaoptik', 20159, 20195, 20155, '2015-01-01 08:00:00.000', '2015-01-01 08:00:00.000');
INSERT INTO vgr_glasogonbidrag_supplier (name, user_id, group_id, company_id, create_date, modified_date) VALUES ('Direktoptik', 20159, 20195, 20155, '2015-01-01 08:00:00.000', '2015-01-01 08:00:00.000');
INSERT INTO vgr_glasogonbidrag_supplier (name, user_id, group_id, company_id, create_date, modified_date) VALUES ('Hendéns', 20159, 20195, 20155, '2015-01-01 08:00:00.000', '2015-01-01 08:00:00.000');

INSERT INTO vgr_glasogonbidrag_identification (id, identity_type) VALUES (100, 'p');
INSERT INTO vgr_glasogonbidrag_personal_identification (id, number) VALUES (100, '19990101-1111');
INSERT INTO vgr_glasogonbidrag_identification (id, identity_type) VALUES (200, 'p');
INSERT INTO vgr_glasogonbidrag_personal_identification (id, number) VALUES (200, '20101010-2010');
INSERT INTO vgr_glasogonbidrag_identification (id, identity_type) VALUES (300, 'p');
INSERT INTO vgr_glasogonbidrag_personal_identification (id, number) VALUES (300, '19801212-1212');
INSERT INTO vgr_glasogonbidrag_identification (id, identity_type) VALUES (400, 'p');
INSERT INTO vgr_glasogonbidrag_personal_identification (id, number) VALUES (400, '19990202-2222');
INSERT INTO vgr_glasogonbidrag_identification (id, identity_type) VALUES (500, 'p');
INSERT INTO vgr_glasogonbidrag_personal_identification (id, number) VALUES (500, '19990202-2222');

INSERT INTO vgr_glasogonbidrag_beneficiary  (id, identification_id, create_date, modified_date, first_name, last_name) VALUES (110, 100, '2016-01-01 08:00:00.000', '2016-01-01 08:00:00.000', 'Karin', 'Bergqvist');
INSERT INTO vgr_glasogonbidrag_beneficiary  (id, identification_id, create_date, modified_date, first_name, last_name) VALUES (210, 200, '2016-01-01 08:00:00.000', '2016-01-01 08:00:00.000', 'Kim', 'Grahn');
INSERT INTO vgr_glasogonbidrag_beneficiary  (id, identification_id, create_date, modified_date, first_name, last_name) VALUES (310, 300, '2016-01-01 08:00:00.000', '2016-01-01 08:00:00.000', 'David', 'Alm');
INSERT INTO vgr_glasogonbidrag_beneficiary  (id, identification_id, create_date, modified_date, first_name, last_name) VALUES (410, 400, '2016-01-01 08:00:00.000', '2016-01-01 08:00:00.000', 'Saga', 'Larsson');
INSERT INTO vgr_glasogonbidrag_beneficiary  (id, identification_id, create_date, modified_date, first_name, last_name) VALUES (510, 500, '2016-01-01 08:00:00.000', '2016-01-01 08:00:00.000', 'Elias', 'Jonsson');

INSERT INTO vgr_glasogonbidrag_invoice (id, user_id, group_id, company_id, create_date, modified_date, amount, vat, invoice_date, invoice_number, verification_number, supplier_name, status) VALUES (1000, 20159, 20195, 20155, '2016-05-02 08:58:00.000', '2016-05-02 09:18:00.000', 100000, 25000, '2015-11-22', '10001', 'E510382', 'Direktoptik', 'PAID');
INSERT INTO vgr_glasogonbidrag_invoice (id, user_id, group_id, company_id, create_date, modified_date, amount, vat, invoice_date, invoice_number, verification_number, supplier_name, status) VALUES (2000, 20159, 20195, 20155, '2016-05-03 08:58:00.000', '2016-05-02 09:18:00.000', 100000, 25000, '2015-11-22', '10124', 'E542343', 'Synsam Delta', 'PAID');
INSERT INTO vgr_glasogonbidrag_invoice (id, user_id, group_id, company_id, create_date, modified_date, amount, vat, invoice_date, invoice_number, verification_number, supplier_name, status) VALUES (3000, 20159, 20195, 20155, '2016-05-03 08:58:00.000', '2016-05-02 09:18:00.000', 100000, 25000, '2015-11-22', '10124', 'E552181', 'Hendéns', 'CANCELED');
INSERT INTO vgr_glasogonbidrag_invoice (id, user_id, group_id, company_id, create_date, modified_date, amount, vat, invoice_date, invoice_number, verification_number, supplier_name, status) VALUES (4000, 20159, 20195, 20155, '2016-05-03 08:58:00.000', '2016-05-02 09:18:00.000', 100000, 25000, '2015-11-22', '10124', 'E584399', 'Synsam Delta', 'CANCELED');

INSERT INTO vgr_glasogonbidrag_grant (user_id, group_id, company_id, create_date, modified_date, delivery_date, prescription_date, amount, vat, receiver, invoice_id) VALUES (20159, 20195, 20155, '2016-05-02 08:58:00.000', '2016-05-02 08:58:00.000', '2015-11-21', '2016-01-26', 50000, 12500, 110, 1000);
INSERT INTO vgr_glasogonbidrag_grant (user_id, group_id, company_id, create_date, modified_date, delivery_date, prescription_date, amount, vat, receiver, invoice_id) VALUES (20159, 20195, 20155, '2016-05-02 08:58:00.000', '2016-05-02 08:58:00.000', '2015-11-21', '2016-01-26', 50000, 12500, 210, 1000);
INSERT INTO vgr_glasogonbidrag_grant (user_id, group_id, company_id, create_date, modified_date, delivery_date, prescription_date, amount, vat, receiver, invoice_id) VALUES (20159, 20195, 20155, '2016-05-02 08:58:00.000', '2016-05-02 08:58:00.000', '2015-11-21', '2016-01-26', 50000, 12500, 110, 2000);
INSERT INTO vgr_glasogonbidrag_grant (user_id, group_id, company_id, create_date, modified_date, delivery_date, prescription_date, amount, vat, receiver, invoice_id) VALUES (20159, 20195, 20155, '2016-05-02 08:58:00.000', '2016-05-02 08:58:00.000', '2015-11-21', '2016-01-26', 50000, 12500, 310, 2000);
INSERT INTO vgr_glasogonbidrag_grant (user_id, group_id, company_id, create_date, modified_date, delivery_date, prescription_date, amount, vat, receiver, invoice_id) VALUES (20159, 20195, 20155, '2016-05-02 08:58:00.000', '2016-05-02 08:58:00.000', '2015-11-21', '2016-01-26', 50000, 12500, 410, 3000);
INSERT INTO vgr_glasogonbidrag_grant (user_id, group_id, company_id, create_date, modified_date, delivery_date, prescription_date, amount, vat, receiver, invoice_id) VALUES (20159, 20195, 20155, '2016-05-02 08:58:00.000', '2016-05-02 08:58:00.000', '2015-11-21', '2016-01-26', 50000, 12500, 510, 4000);

