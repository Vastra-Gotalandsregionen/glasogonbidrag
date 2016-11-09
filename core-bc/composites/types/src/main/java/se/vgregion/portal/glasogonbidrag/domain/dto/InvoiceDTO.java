package se.vgregion.portal.glasogonbidrag.domain.dto;

import se.vgregion.portal.glasogonbidrag.domain.InvoiceStatus;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;

/**
 * @author Martin Lind - Monator Technologies AB
 */
// TODO: Add owner when owner is added to main relation.
public class InvoiceDTO {
    private long id;

    private String verificationNumber;
    private String supplier;
    private String invoiceNumber;
    private long amount;
    private long count;
    private InvoiceStatus status;
//    private String owner;

    private Invoice invoice;

    public InvoiceDTO(long id, String verificationNumber,
                      String supplier, String invoiceNumber,
                      long amount, long count, InvoiceStatus status,
                      String owner) {
        this.id = id;
        this.verificationNumber = verificationNumber;
        this.supplier = supplier;
        this.invoiceNumber = invoiceNumber;
        this.amount = amount;
        this.count = count;
        this.status = status;
//        this.owner = owner;

        this.invoice = null;
    }

    public InvoiceDTO(Invoice invoice) {
        this.invoice = invoice;

        this.id = invoice.getId();
        this.verificationNumber = invoice.getVerificationNumber();
        this.supplier = invoice.getSupplier().getName();
        this.invoiceNumber = invoice.getInvoiceNumber();
        this.amount = invoice.getAmount();
        this.count = invoice.getGrants().size();
        this.status = invoice.getStatus();
//        this.owner = invoice.getOwner;
    }

    public long getId() {
        return id;
    }

    public String getVerificationNumber() {
        return verificationNumber;
    }

    public String getSupplier() {
        return supplier;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public long getAmount() {
        return amount;
    }

    public long getCount() {
        return count;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

//    public String getOwner() {
//        return owner;
//    }

    public Invoice getInvoice() {
        return invoice;
    }
}
