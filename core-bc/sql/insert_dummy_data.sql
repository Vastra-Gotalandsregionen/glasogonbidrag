
-- Insert dummy data into database.
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

    id01 bigint;
    id02 bigint;
    id03 bigint;
    id04 bigint;
    id05 bigint;
    id06 bigint;
    id07 bigint;
    id08 bigint;
    id09 bigint;
    id10 bigint;
    id11 bigint;
    id12 bigint;
    id13 bigint;
    id14 bigint;
    id15 bigint;
BEGIN
    -- Get invoice Ids from sequence.
    inv01 := nextval('invoice_id_seq'::regclass);
    inv02 := nextval('invoice_id_seq'::regclass);
    inv03 := nextval('invoice_id_seq'::regclass);

    -- Get beneficiary Ids from sequence.
    per01 := nextval('beneficiary_id_seq'::regclass);
    per02 := nextval('beneficiary_id_seq'::regclass);
    per03 := nextval('beneficiary_id_seq'::regclass);
    per04 := nextval('beneficiary_id_seq'::regclass);
    per05 := nextval('beneficiary_id_seq'::regclass);
    per06 := nextval('beneficiary_id_seq'::regclass);
    per07 := nextval('beneficiary_id_seq'::regclass);
    per08 := nextval('beneficiary_id_seq'::regclass);
    per09 := nextval('beneficiary_id_seq'::regclass);
    per10 := nextval('beneficiary_id_seq'::regclass);
    per11 := nextval('beneficiary_id_seq'::regclass);
    per12 := nextval('beneficiary_id_seq'::regclass);
    per13 := nextval('beneficiary_id_seq'::regclass);
    per14 := nextval('beneficiary_id_seq'::regclass);
    per15 := nextval('beneficiary_id_seq'::regclass);

    -- Get identification Ids from sequence.
    id01 := nextval('identification_id_seq'::regclass);
    id02 := nextval('identification_id_seq'::regclass);
    id03 := nextval('identification_id_seq'::regclass);
    id04 := nextval('identification_id_seq'::regclass);
    id05 := nextval('identification_id_seq'::regclass);
    id06 := nextval('identification_id_seq'::regclass);
    id07 := nextval('identification_id_seq'::regclass);
    id08 := nextval('identification_id_seq'::regclass);
    id09 := nextval('identification_id_seq'::regclass);
    id10 := nextval('identification_id_seq'::regclass);
    id11 := nextval('identification_id_seq'::regclass);
    id12 := nextval('identification_id_seq'::regclass);
    id13 := nextval('identification_id_seq'::regclass);
    id14 := nextval('identification_id_seq'::regclass);
    id15 := nextval('identification_id_seq'::regclass);

-- Temporary moved to initial data.
--    -- Insert suppliers.
--    INSERT INTO supplier
--        (name, user_id, group_id, company_id, create_date, modified_date)
--    VALUES
--        ('Synsam Alfa', 20159, 20195, 20155,
--         '2015-01-01 08:00:00.000', '2015-01-01 08:00:00.000'),
--        ('Synsam Beta', 20159, 20195, 20155,
--         '2015-01-01 08:00:00.000', '2015-01-01 08:00:00.000'),
--        ('Synsam Gamma', 20159, 20195, 20155,
--         '2015-01-01 08:00:00.000', '2015-01-01 08:00:00.000'),
--        ('Synsam Delta', 20159, 20195, 20155,
--         '2015-01-01 08:00:00.000', '2015-01-01 08:00:00.000'),
--        ('Specsavers Alfa', 20159, 20195, 20155,
--         '2015-01-01 08:00:00.000', '2015-01-01 08:00:00.000'),
--        ('Specsavers Beta', 20159, 20195, 20155,
--         '2015-01-01 08:00:00.000', '2015-01-01 08:00:00.000'),
--        ('Specsavers Gamma', 20159, 20195, 20155,
--         '2015-01-01 08:00:00.000', '2015-01-01 08:00:00.000'),
--        ('Specsavers Delta', 20159, 20195, 20155,
--         '2015-01-01 08:00:00.000', '2015-01-01 08:00:00.000'),
--        ('Wasaoptik', 20159, 20195, 20155,
--         '2015-01-01 08:00:00.000', '2015-01-01 08:00:00.000'),
--        ('Direktoptik', 20159, 20195, 20155,
--         '2015-01-01 08:00:00.000', '2015-01-01 08:00:00.000'),
--        ('Hendéns', 20159, 20195, 20155,
--         '2015-01-01 08:00:00.000', '2015-01-01 08:00:00.000');

    -- Insert invoices.
    INSERT INTO invoice
        (id,
         user_id, group_id, company_id,
         create_date, modified_date,
         amount,
         vat,
         invoice_date,
         invoice_number,
         verification_number,
         supplier_name)
    VALUES
      (inv01, 20159, 20195, 20155,
       '2016-05-02 08:58:00.000', '2016-05-02 09:18:00.000',
       100000, 25000, '2015-11-22', '10001', 'E510382', 'Direktoptik'),

      (inv02, 20159, 20195, 20155, NOW(), NOW(),
       350000, 87500, '2016-02-16', '10002', 'E510396', 'Synsam Gamma'),
      (inv03, 20159, 20195, 20155, NOW(), NOW(),
       250000, 62500, '2016-02-17', '10003', 'E510422', 'Wasaoptik');

    -- Insert grant adjustment to invoice with verification number E510422
    INSERT INTO grant_adjustment
        (amount,
         user_id, group_id, company_id,
         create_date, modified_date,
         invoice)
    VALUES
        (50000, 20159, 20195, 20155, NOW(), NOW(), inv03);

    -- Insert identification number ids.
    INSERT INTO identification
        (id, identity_type)
    VALUES
        (id01, 'p'), (id02, 'p'), (id03, 'p'), (id04, 'l'), (id05, 'p'),
        (id06, 'p'), (id07, 'p'), (id08, 'p'), (id09, 'p'), (id10, 'p'),
        (id11, 'p'), (id12, 'p'), (id13, 'p'), (id14, 'p'), (id15, 'p');

    -- Insert swedish personal identification numbers.
    INSERT INTO personal_identification
        (id, number)
    VALUES
        (id01, '95362648-6608'),
        (id02, '11996977-9927'),
        (id03, '17174547-8048'),
        (id05, '06135518-6209'),
        (id06, '56278208-2178'),
        (id07, '90962100-8895'),
        (id08, '63248426-1541'),
        (id09, '75301755-6468'),
        (id10, '34645311-2459'),
        (id11, '29612756-7024'),
        (id12, '08118623-3066'),
        (id13, '90402945-5218'),
        (id14, '20619206-6497'),
        (id15, '19551549-5904');

    -- Insert migrations LMA identification number.
    INSERT INTO lma_identification
        (id, number)
    VALUES
        (id04, '50-008920/4');

    -- Insert beneficiaries.
    INSERT INTO beneficiary
        (id, identification_id, create_date, modified_date, first_name, last_name)
    VALUES
        (per01, id01, '2016-01-01 08:00:00.000', '2016-01-01 08:00:00.000',
         'Karin', 'Magnusson'),
        (per02, id02, '2016-01-01 08:00:00.000', '2016-01-01 08:00:00.000',
         'Njord', 'Solberg'),
        (per03, id03, '2016-01-01 08:00:00.000', '2016-01-01 08:00:00.000',
         'Kettil', 'Persson'),
        (per04, id04, '2016-01-01 08:00:00.000', '2016-01-01 08:00:00.000',
         'Boguslaw', 'Stedry'),
        (per05, id05, '2016-01-01 08:00:00.000', '2016-01-01 08:00:00.000',
         'Teresia', 'Normansson'),
        (per06, id06, '2016-01-01 08:00:00.000', '2016-01-01 08:00:00.000',
         'Niklas', 'Kjellsson'),
        (per07, id07, '2016-01-01 08:00:00.000', '2016-01-01 08:00:00.000',
         'Thorborg', 'Danielsson'),
        (per08, id08, '2016-01-01 08:00:00.000', '2016-01-01 08:00:00.000',
         'Moa', 'Lindholm'),
        (per09, id09, '2016-01-01 08:00:00.000', '2016-01-01 08:00:00.000',
         'Åsa', 'Björk'),
        (per10, id10, '2016-01-01 08:00:00.000', '2016-01-01 08:00:00.000',
         'David', 'Grahn'),
        (per11, id11, '2016-01-01 08:00:00.000', '2016-01-01 08:00:00.000',
         'Charlotte', 'Bergström'),
        (per12, id12, '2016-01-01 08:00:00.000', '2016-01-01 08:00:00.000',
         'Helene', 'Johnsson'),
        (per13, id13, '2016-01-01 08:00:00.000', '2016-01-01 08:00:00.000',
         'Vilhelmina', 'Claesson'),
        (per14, id14, '2016-01-01 08:00:00.000', '2016-01-01 08:00:00.000',
         'Gustav', 'Borg'),
        (per15, id15, '2016-01-01 08:00:00.000', '2016-01-01 08:00:00.000',
         'Sibylla', 'Robertsson');

    -- Insert grants.
    INSERT INTO grant
        (user_id,
         group_id,
         company_id,
         create_date,
         modified_date,
         delivery_date,
         prescription_date,
         amount,
         vat,
         receiver,
         invoice_id)
    VALUES
        (20159, 20195, 20155,
         '2016-05-02 08:58:00.000', '2016-05-02 08:58:00.000',
         '2015-11-21', '2016-01-26', 40000, 10000, per01, inv01),
        (20159, 20195, 20155,
         '2016-05-02 09:03:00.000', '2016-05-02 09:03:00.000',
         '2015-12-30', '2016-01-26', 30000, 7500, per02, inv01),
        (20159, 20195, 20155,
         '2016-05-02 09:18:00.000', '2016-05-02 09:18:00.000',
         '2015-10-17', '2016-01-26', 30000, 7500, per03, inv01),

        (20159, 20195, 20155, NOW(), NOW(),
         '2016-02-03', '2016-01-26', 30000, 7500, per03, inv02),
        (20159, 20195, 20155, NOW(), NOW(),
         '2016-01-20', '2016-01-26', 60000, 15000, per04, inv02),
        (20159, 20195, 20155, NOW(), NOW(),
         '2016-01-23', '2016-01-26', 60000, 15000, per05, inv02),
        (20159, 20195, 20155, NOW(), NOW(),
         '2016-02-04', '2016-01-26', 50000, 12500, per06, inv02),
        (20159, 20195, 20155, NOW(), NOW(),
         '2016-01-30', '2016-01-26', 60000, 15000, per07, inv02),
        (20159, 20195, 20155, NOW(), NOW(),
         '2016-01-17', '2016-01-26', 60000, 15000, per08, inv02),
        (20159, 20195, 20155, NOW(), NOW(),
         '2016-01-21', '2016-01-26', 30000, 7500, per09, inv02),
        (20159, 20195, 20155, NOW(), NOW(),
         '2016-02-03', '2016-01-26', 40000, 10000, per10, inv02),
        (20159, 20195, 20155, NOW(), NOW(),
         '2016-01-12', '2016-01-26', 10000, 2500, per11, inv02),

        (20159, 20195, 20155, NOW(), NOW(),
         '2016-02-28', '2016-01-26', 20000, 5000, per03, inv03),
        (20159, 20195, 20155, NOW(), NOW(),
         '2016-03-16', '2016-01-26', 20000, 5000, per11, inv03),
        (20159, 20195, 20155, NOW(), NOW(),
         '2016-02-13', '2016-01-26', 60000, 15000, per12, inv03),
        (20159, 20195, 20155, NOW(), NOW(),
         '2016-02-17', '2016-01-26', 60000, 15000, per13, inv03),
        (20159, 20195, 20155, NOW(), NOW(),
         '2016-03-21', '2016-01-26', 30000, 7500, per14, inv03),
        (20159, 20195, 20155, NOW(), NOW(),
         '2016-04-01', '2016-01-26', 20000, 5000, per15, inv03);
END $$;
