package se.vgregion.portal.glasogonbidrag.domain.dto;

import se.vgregion.portal.glasogonbidrag.domain.InvoiceStatus;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class SupplierInvoiceDTO {
    private final long id;

    private final InvoiceStatus status;
//    private final String owner;

    private final Invoice invoice;

    public SupplierInvoiceDTO(long id, InvoiceStatus status, String owner) {
        this.id = id;
        this.status = status;
//        this.owner = owner;

        this.invoice = null;
    }

    public SupplierInvoiceDTO(Invoice invoice) {
        this.invoice = invoice;
        this.id = invoice.getId();
        this.status = invoice.getStatus();
//        this.owner = invoice.getOwner();
    }

    public long getId() {
        return id;
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
