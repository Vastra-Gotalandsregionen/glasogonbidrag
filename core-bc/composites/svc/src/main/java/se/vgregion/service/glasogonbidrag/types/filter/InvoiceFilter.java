package se.vgregion.service.glasogonbidrag.types.filter;

import se.vgregion.portal.glasogonbidrag.domain.InvoiceStatus;

import java.util.Map;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class InvoiceFilter {
    private String verificationNumber = null;
    private String supplier = null;
    private String invoiceNumber = null;
    private InvoiceStatus status = null;
    private String caseWorker = null;

    public InvoiceFilter(Map<String, Object> filters) {
        if (filters.containsKey("verificationNumber")) {
            verificationNumber = (String) filters.get("verificationNumber");
        }

        if (filters.containsKey("supplier")) {
            supplier = (String) filters.get("supplier");
        }

        if (filters.containsKey("invoiceNumber")) {
            invoiceNumber = (String) filters.get("invoiceNumber");
        }

        if (filters.containsKey("status")) {
            status = InvoiceStatus.parse((String)filters.get("status"));
        }

        if (filters.containsKey("caseWorker")) {
            caseWorker = (String) filters.get("caseWorker");
        }
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

    public InvoiceStatus getStatus() {
        return status;
    }

    public String getCaseWorker() {
        return caseWorker;
    }

    public boolean hasVerificationNumberFilter() {
        return verificationNumber != null;
    }

    public boolean hasSupplierFilter() {
        return supplier != null;
    }

    public boolean hasInvoiceNumberFilter() {
        return invoiceNumber != null;
    }

    public boolean hasStatusFilter() {
        return status != null;
    }

    public boolean hasCaseWorkerFilter() {
        return caseWorker != null;
    }

    public boolean hasFilters() {
        return hasVerificationNumberFilter()
                || hasSupplierFilter()
                || hasInvoiceNumberFilter()
                || hasStatusFilter()
                || hasCaseWorkerFilter();
    }
}
