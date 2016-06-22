package se.vgregion.glasogonbidrag.model;

import se.vgregion.glasogonbidrag.parser.ParseOutputData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;

/**
 * This file will import parsed data from the ExcelDataParser and convert
 * the ImportDocument and ImportGrant into Beneficiaries, Invoices and Grants
 *
 * Since ImportDocument contains a list of ImportGrants, and the grants are
 * the storage of invoice's verification number, the data set needs to be
 * reversed, so that an Invoice have a list of Grants and an Beneficiary have
 * a list of Grants.
 *
 * @author Martin Lind - Monator Technologies AB
 */
public class ImportDataLibrary {

    private static final String DUMMY_VERIFICATION_NUMBER_PREFIX = "DM";
    private static final String DUMMY_VERIFICATION_NUMBER;

    static {
        DUMMY_VERIFICATION_NUMBER = generateRandomVerificationNumber();
    }

    public static String dummyVerificationNumber() {
        return DUMMY_VERIFICATION_NUMBER;
    }

    private static String generateRandomVerificationNumber() {
        Random rand = new Random();
        int a = rand.nextInt();

        return String.format("%s%d",
                DUMMY_VERIFICATION_NUMBER_PREFIX,
                Math.abs(a) % 100000000);
    }

    private InvoiceStorage invoices;
    private BeneficiaryStorage beneficiaries;
    private GrantStorage grants;

    private String verificationNumber;

    private List<ImportError> errors = new ArrayList<>();

    public ImportDataLibrary() {
        invoices = new InvoiceStorage();
        beneficiaries = new BeneficiaryStorage();
        grants = new GrantStorage();
    }

    public ImportDataLibrary(String verificationNumber) {
        this.verificationNumber = verificationNumber;
    }

    public List<ImportError> getErrors() {
        return errors;
    }

    public void catalog(Map<String, ParseOutputData> data) {
        for (String s : data.keySet()) {
            catalog(data.get(s));
        }

        System.out.println("Done!");
    }

    private void catalog(ParseOutputData data) {
        Stack<ImportDocument> docs = data.getDocuments();
        Stack<ImportError> errs = data.getErrors();

        if (!errs.isEmpty()) {
            catalogErrors(errs);
            return;
        }

        while (!docs.isEmpty()) {
            ImportDocument doc = docs.pop();

            String id = doc.getId();
            String name = doc.getName();

            beneficiaries.add(id, name);

            add(id, doc.getGrants());
        }
    }

    private void catalogErrors(Stack<ImportError> errors) {
        while (!errors.isEmpty()) {
            ImportError error = errors.pop();

            this.errors.add(error);
        }
    }


    private void add(String id, List<ImportGrant> gs) {
        for (ImportGrant grant : gs) {
            String g = data(grant);
            Long grantId = grants.add(g);

            String ver = grant.getVerificationNumber();
            String inv = grant.getInvoiceNumber();

            if (dummyVerificationNumber().equals(ver)
                    && verificationNumber != null) {
                ver = verificationNumber;
            }

            invoices.add(ver, inv);

            invoices.map(ver, grantId);
            beneficiaries.map(id, grantId);
        }
    }

    private String data(ImportGrant grant) {
        return String.format("%s:%s:%s",
                grant.getDeliveryDate(),
                grant.getPrescriptionDate(),
                grant.getAmount());
    }

    private class InvoiceStorage {
        Map<String, List<Long>> invoices = new HashMap<>();
        Map<String, String> numbers = new HashMap<>();

        void add(String verificationNumber) {
            add(verificationNumber, null);
        }

        void add(String verificationNumber, String invoiceNumber) {
            if (!invoices.containsKey(verificationNumber)) {
                invoices.put(verificationNumber, new ArrayList<Long>());
            }
            if (invoiceNumber != null) {
                numbers.put(verificationNumber, invoiceNumber);
            }
        }

        void map(String verificationNumber, Long grant) {
            add(verificationNumber);
            invoices.get(verificationNumber).add(grant);
        }
    }

    private class BeneficiaryStorage {
        Map<String, List<Long>> beneficiaries = new HashMap<>();
        Map<String, String> names = new HashMap<>();

        void add(String id) {
            add(id, null);
        }

        void add(String id, String name) {
            if (!beneficiaries.containsKey(id)) {
                beneficiaries.put(id, new ArrayList<Long>());
            }
            if (name != null) {
                names.put(id, name);
            }
        }

        void map(String id, Long grant) {
            add(id);
            beneficiaries.get(id).add(grant);
        }
    }

    private class GrantStorage {
        IdPool pool = new IdPool();
        Map<Long, String> grant = new HashMap<>();

        Long add(String data) {
            long id = pool.get();
            grant.put(id, data);
            return id;
        }
    }

    private class IdPool {
        private long cur = 1;

        long get() {
            long ret = cur;
            cur = cur + 1;
            return ret;
        }
    }
}
