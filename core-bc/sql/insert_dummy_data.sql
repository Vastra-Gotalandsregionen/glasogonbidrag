
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
    inv01 := nextval('vgr_glasogonbidrag_invoice_id_seq'::regclass);
    inv02 := nextval('vgr_glasogonbidrag_invoice_id_seq'::regclass);
    inv03 := nextval('vgr_glasogonbidrag_invoice_id_seq'::regclass);

    -- Get beneficiary Ids from sequence.
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

    -- Get identification Ids from sequence.
    id01 := nextval('vgr_glasogonbidrag_identification_id_seq'::regclass);
    id02 := nextval('vgr_glasogonbidrag_identification_id_seq'::regclass);
    id03 := nextval('vgr_glasogonbidrag_identification_id_seq'::regclass);
    id04 := nextval('vgr_glasogonbidrag_identification_id_seq'::regclass);
    id05 := nextval('vgr_glasogonbidrag_identification_id_seq'::regclass);
    id06 := nextval('vgr_glasogonbidrag_identification_id_seq'::regclass);
    id07 := nextval('vgr_glasogonbidrag_identification_id_seq'::regclass);
    id08 := nextval('vgr_glasogonbidrag_identification_id_seq'::regclass);
    id09 := nextval('vgr_glasogonbidrag_identification_id_seq'::regclass);
    id10 := nextval('vgr_glasogonbidrag_identification_id_seq'::regclass);
    id11 := nextval('vgr_glasogonbidrag_identification_id_seq'::regclass);
    id12 := nextval('vgr_glasogonbidrag_identification_id_seq'::regclass);
    id13 := nextval('vgr_glasogonbidrag_identification_id_seq'::regclass);
    id14 := nextval('vgr_glasogonbidrag_identification_id_seq'::regclass);
    id15 := nextval('vgr_glasogonbidrag_identification_id_seq'::regclass);

    -- Insert suppliers.
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

    -- Insert invoices.
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

    -- Insert grant adjustment to invoice with verification number E510422
    INSERT INTO vgr_glasogonbidrag_grant_adjustment
        (amount, invoice)
    VALUES
        (50000, inv03);

    -- Insert identification number ids.
    INSERT INTO vgr_glasogonbidrag_identification
        (id, identity_type)
    VALUES
        (id01, 'p'), (id02, 'p'), (id03, 'p'), (id04, 'l'), (id05, 'p'),
        (id06, 'p'), (id07, 'p'), (id08, 'p'), (id09, 'p'), (id10, 'p'),
        (id11, 'p'), (id12, 'p'), (id13, 'p'), (id14, 'p'), (id15, 'p');

    -- Insert swedish personal identification numbers.
    INSERT INTO vgr_glasogonbidrag_personal_identification
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
    INSERT INTO vgr_glasogonbidrag_lma_identification
        (id, number)
    VALUES
        (id04, '50-008920/4');

    -- Insert beneficiaries.
    INSERT INTO vgr_glasogonbidrag_beneficiary
        (id, identification_id, first_name, last_name)
    VALUES
        (per01, id01, 'Karin', 'Magnusson'),
        (per02, id02, 'Njord', 'Solberg'),
        (per03, id03, 'Kettil', 'Persson'),
        (per04, id04, 'Boguslaw', 'Stedry'),
        (per05, id05, 'Teresia', 'Normansson'),
        (per06, id06, 'Niklas', 'Kjellsson'),
        (per07, id07, 'Thorborg', 'Danielsson'),
        (per08, id08, 'Moa', 'Lindholm'),
        (per09, id09, 'Åsa', 'Björk'),
        (per10, id10, 'David', 'Grahn'),
        (per11, id11, 'Charlotte', 'Bergström'),
        (per12, id12, 'Helene', 'Johnsson'),
        (per13, id13, 'Vilhelmina', 'Claesson'),
        (per14, id14, 'Gustav', 'Borg'),
        (per15, id15, 'Sibylla', 'Robertsson');

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
