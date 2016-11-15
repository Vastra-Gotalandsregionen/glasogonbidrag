package se.vgregion.portal.glasogonbidrag.domain.dto;

import se.vgregion.portal.glasogonbidrag.domain.InvoiceStatus;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class SupplierInvoiceDTO {
    private final long id;

    private final String verificationNumber;
    private final InvoiceStatus status;
    private final String caseWorker;

    private final Invoice invoice;

    public SupplierInvoiceDTO(long id, String verificationNumber,
                              InvoiceStatus status, String caseWorker) {
        this.id = id;

        this.verificationNumber = verificationNumber;
        this.status = status;
        this.caseWorker = caseWorker;

        this.invoice = null;
    }

    public SupplierInvoiceDTO(Invoice invoice) {
        this.invoice = invoice;

        this.id = invoice.getId();
        this.verificationNumber = invoice.getVerificationNumber();
        this.status = invoice.getStatus();
        this.caseWorker = invoice.getCaseWorker();
    }

    public long getId() {
        return id;
    }

    public String getVerificationNumber() {
        return verificationNumber;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public String getCaseWorker() {
        return caseWorker;
    }

    public Invoice getInvoice() {
        return invoice;
    }
}
