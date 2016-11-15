package se.vgregion.portal.glasogonbidrag.domain.dto;

import se.vgregion.portal.glasogonbidrag.domain.InvoiceStatus;
import se.vgregion.portal.glasogonbidrag.domain.internal.KronaCalculationUtil;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;

import java.math.BigDecimal;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class InvoiceDTO {
    private final long id;

    private final String verificationNumber;
    private final String supplier;
    private final String invoiceNumber;
    private final long amount;
    private final long count;
    private final InvoiceStatus status;
    private final String caseWorker;

    private final Invoice invoice;

    private final KronaCalculationUtil currency =
            new KronaCalculationUtil();

    public InvoiceDTO(long id, String verificationNumber,
                      String supplier, String invoiceNumber,
                      long amount, long count, InvoiceStatus status,
                      String caseWorker) {
        this.id = id;
        this.verificationNumber = verificationNumber;
        this.supplier = supplier;
        this.invoiceNumber = invoiceNumber;
        this.amount = amount;
        this.count = count;
        this.status = status;
        this.caseWorker = caseWorker;

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
        this.caseWorker = invoice.getCaseWorker();
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

    public BigDecimal getAmountAsKrona() {
        return currency.calculatePartsAsKrona(amount);
    }

    public long getCount() {
        return count;
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
