package se.vgregion.service.glasogonbidrag.local.internal;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Grant;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.service.glasogonbidrag.local.api.InvoiceValidationService;

import java.util.Set;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class InvoiceValidationServiceImpl implements InvoiceValidationService {
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validateInvoice(Invoice invoice) {
        Set<Grant> grants = invoice.getGrants();

        long amountSum = invoice.getAmount();
        long grantAmountSum = 0L;
        for (Grant grant : grants) {
            grantAmountSum += grant.getAmount();
        }

        return grantAmountSum == amountSum;
    }
}
