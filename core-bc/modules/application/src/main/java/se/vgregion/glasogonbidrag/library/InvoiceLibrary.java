package se.vgregion.glasogonbidrag.library;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Grant;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Migration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
class InvoiceLibrary {
    private Migration migration;
    private List<Invoice> invoices;

    public InvoiceLibrary() {
        migration = new Migration();
        migration.setDate(Calendar.getInstance().getTime());
        invoices = new ArrayList<>();
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public Migration getMigration() {
        return migration;
    }

    public void add(Invoice invoice) {
        invoices.add(invoice);
        migration.addInvoice(invoice);
        invoice.setMigration(migration);
    }

    public void add(String verificationNumber, Grant grant) {
        Invoice invoice = find(verificationNumber);
        invoice.addGrant(grant);
        grant.setInvoice(invoice);
    }

    public Invoice find(String verificationNumber) {
        for (Invoice invoice : invoices) {
            if (verificationNumber.equals(invoice.getVerificationNumber())) {
                return invoice;
            }
        }

        return null;
    }
}
