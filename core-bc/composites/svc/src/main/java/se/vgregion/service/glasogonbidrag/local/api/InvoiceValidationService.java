package se.vgregion.service.glasogonbidrag.local.api;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public interface InvoiceValidationService {
    boolean validateInvoice(Invoice invoice);
}
