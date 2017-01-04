package se.vgregion.service.glasogonbidrag.local.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Grant;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.service.glasogonbidrag.local.api.InvoiceValidationService;

import java.util.Set;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Service
public class InvoiceValidationServiceImpl implements InvoiceValidationService {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(InvoiceValidationServiceImpl.class);

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

        LOGGER.debug("Grant amount {} and amount {}", grantAmountSum, amountSum);

        return grantAmountSum == amountSum;
    }
}
