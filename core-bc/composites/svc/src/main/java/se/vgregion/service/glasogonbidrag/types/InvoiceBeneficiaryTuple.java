package se.vgregion.service.glasogonbidrag.types;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Beneficiary;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class InvoiceBeneficiaryTuple {
    private final Invoice invoice;
    private final Beneficiary beneficiary;

    public InvoiceBeneficiaryTuple(Invoice invoice, Beneficiary beneficiary) {
        this.invoice = invoice;
        this.beneficiary = beneficiary;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public Beneficiary getBeneficiary() {
        return beneficiary;
    }
}
