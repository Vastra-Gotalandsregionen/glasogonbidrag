package se.vgregion.service.glasogonbidrag.types;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Grant;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class InvoiceGrantTuple {
    private final Invoice invoice;
    private final Grant grant;

    public InvoiceGrantTuple(Invoice invoice, Grant grant) {
        this.invoice = invoice;
        this.grant = grant;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public Grant getGrant() {
        return grant;
    }
}
