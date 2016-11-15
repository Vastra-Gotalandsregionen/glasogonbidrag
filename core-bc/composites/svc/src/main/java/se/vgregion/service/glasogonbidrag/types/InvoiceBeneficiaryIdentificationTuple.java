package se.vgregion.service.glasogonbidrag.types;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Beneficiary;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Identification;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class InvoiceBeneficiaryIdentificationTuple {
    private final Invoice invoice;
    private final Beneficiary beneficiary;
    private final Identification identification;

    public InvoiceBeneficiaryIdentificationTuple(Invoice invoice,
                                                 Beneficiary beneficiary,
                                                 Identification identification) {
        this.invoice = invoice;
        this.beneficiary = beneficiary;
        this.identification = identification;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public Beneficiary getBeneficiary() {
        return beneficiary;
    }

    public Identification getIdentification() {
        return identification;
    }
}
