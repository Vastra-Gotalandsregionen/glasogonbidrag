
-- Insert data into the dummy data into the database.
DO $$
DECLARE
    inv01 bigint;
    inv02 bigint;
    inv03 bigint;
    per01 bigint;
    per02 bigint;
    per03 bigint;
    per04 bigint;
    per05 bigint;
    per06 bigint;
    per07 bigint;
    per08 bigint;
    per09 bigint;
    per10 bigint;
    per11 bigint;
    per12 bigint;
    per13 bigint;
    per14 bigint;
    per15 bigint;
BEGIN
    inv01 := nextval('vgr_glasogonbidrag_invoice_id_seq'::regclass);
    inv02 := nextval('vgr_glasogonbidrag_invoice_id_seq'::regclass);
    inv03 := nextval('vgr_glasogonbidrag_invoice_id_seq'::regclass);
    per01 := nextval('vgr_glasogonbidrag_beneficiary_id_seq'::regclass);
    per02 := nextval('vgr_glasogonbidrag_beneficiary_id_seq'::regclass);
    per03 := nextval('vgr_glasogonbidrag_beneficiary_id_seq'::regclass);
    per04 := nextval('vgr_glasogonbidrag_beneficiary_id_seq'::regclass);
    per05 := nextval('vgr_glasogonbidrag_beneficiary_id_seq'::regclass);
    per06 := nextval('vgr_glasogonbidrag_beneficiary_id_seq'::regclass);
    per07 := nextval('vgr_glasogonbidrag_beneficiary_id_seq'::regclass);
    per08 := nextval('vgr_glasogonbidrag_beneficiary_id_seq'::regclass);
    per09 := nextval('vgr_glasogonbidrag_beneficiary_id_seq'::regclass);
    per10 := nextval('vgr_glasogonbidrag_beneficiary_id_seq'::regclass);
    per11 := nextval('vgr_glasogonbidrag_beneficiary_id_seq'::regclass);
    per12 := nextval('vgr_glasogonbidrag_beneficiary_id_seq'::regclass);
    per13 := nextval('vgr_glasogonbidrag_beneficiary_id_seq'::regclass);
    per14 := nextval('vgr_glasogonbidrag_beneficiary_id_seq'::regclass);
    per15 := nextval('vgr_glasogonbidrag_beneficiary_id_seq'::regclass);

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

    -- Add invoices.
    INSERT INTO vgr_glasogonbidrag_invoice
        (id, user_id, group_id, company_id,
         amount,
         vat,
         invoice_date,
         invoice_number,
         verification_number,
         supplier_name)
    VALUES
      (inv01, 20159, 20195, 20155, 100000, 25000, '2015-11-22', '10001', 'E510382', 'Direktoptik'),
      (inv02, 20159, 20195, 20155, 350000, 87500, '2016-02-16', '10002', 'E510396', 'Synsam Gamma'),
      (inv03, 20159, 20195, 20155, 250000, 62500, '2016-02-17', '10003', 'E510422', 'Wasaoptik');

    -- Add grant adjustment to invoice E510422
    INSERT INTO vgr_glasogonbidrag_grant_adjustment
        (amount, invoice)
    VALUES (50000, inv03);

    INSERT INTO vgr_glasogonbidrag_beneficiary
        (id, first_name, last_name)
    VALUES
        (per01, 'Karin', 'Magnusson'),
        (per02, 'Njord', 'Solberg'),
        (per03, 'Kettil', 'Persson'),
        (per04, 'Agnes', 'Vinter'),
        (per05, 'Teresia', 'Normansson'),
        (per06, 'Niklas', 'Kjellsson'),
        (per07, 'Thorborg', 'Danielsson'),
        (per08, 'Moa', 'Lindholm'),
        (per09, 'Åsa', 'Björk'),
        (per10, 'David', 'Grahn'),
        (per11, 'Charlotte', 'Bergström'),
        (per12, 'Helene', 'Johnsson'),
        (per13, 'Vilhelmina', 'Claesson'),
        (per14, 'Gustav', 'Borg'),
        (per15, 'Sibylla', 'Robertsson');

    -- Insert grants.
    INSERT INTO vgr_glasogonbidrag_grant
        (delivery_date,
         prescription_date,
         amount,
         vat,
         receiver,
         invoice_id)
    VALUES
        ('2015-11-21', '2016-01-26', 40000, 10000, per01, inv01),
        ('2015-12-30', '2016-01-26', 30000, 7500, per02, inv01),
        ('2015-10-17', '2016-01-26', 30000, 7500, per03, inv01),

        ('2016-02-03', '2016-01-26', 30000, 7500, per03, inv02),
        ('2016-01-20', '2016-01-26', 60000, 15000, per04, inv02),
        ('2016-01-23', '2016-01-26', 60000, 15000, per05, inv02),
        ('2016-02-04', '2016-01-26', 50000, 12500, per06, inv02),
        ('2016-01-30', '2016-01-26', 60000, 15000, per07, inv02),
        ('2016-01-17', '2016-01-26', 60000, 15000, per08, inv02),
        ('2016-01-21', '2016-01-26', 30000, 7500, per09, inv02),
        ('2016-02-03', '2016-01-26', 40000, 10000, per10, inv02),
        ('2016-01-12', '2016-01-26', 10000, 2500, per11, inv02),

        ('2016-02-28', '2016-01-26', 20000, 5000, per03, inv03),
        ('2016-03-16', '2016-01-26', 20000, 5000, per11, inv03),
        ('2016-02-13', '2016-01-26', 60000, 15000, per12, inv03),
        ('2016-02-17', '2016-01-26', 60000, 15000, per13, inv03),
        ('2016-03-21', '2016-01-26', 30000, 7500, per14, inv03),
        ('2016-04-01', '2016-01-26', 20000, 5000, per15, inv03);
END $$;
