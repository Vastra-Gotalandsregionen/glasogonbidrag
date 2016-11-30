package se.vgregion.service.glasogonbidrag.local.api;

import se.vgregion.portal.glasogonbidrag.domain.jpa.AccountingDistribution;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Grant;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public interface AccountingDistributionCalculationService {
    AccountingDistribution calculateFrom(Invoice invoice);

    int lookupResponsibility(Grant grant);
    int lookupFreeCode(Grant grant);
}
