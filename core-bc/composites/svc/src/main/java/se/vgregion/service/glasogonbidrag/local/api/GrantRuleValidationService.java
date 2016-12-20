package se.vgregion.service.glasogonbidrag.local.api;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Diagnose;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Grant;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.service.glasogonbidrag.types.GrantRuleResult;

import java.util.Date;
import java.util.Set;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public interface GrantRuleValidationService {

    // TODO: document method
    GrantRuleResult mayAddToInvoice(Grant grant, Invoice invoice);

    /**
     *
     * Service that checks data to se if a grant is valid or not.
     *
     * We check:
     * <ul>
     *     <li>Receipt date is earlier than delivery date.</li>
     *     <li>Receipt should be valid</li>
     *     <li>
     *         For 0-15/0-19 (other diagnose) the total amount for this
     *         diagnose may not be exceeded per receipt on a calendar year.
     *     </li>
     *     <li>Receipt and delivery date should be in the past</li>
     *     <li>Visual acuity for Keratoconus must be less than 0.3</li>
     * </ul>
     *
     * Validity check for Receipt:
     * <ul>
     *     <li>Date is not in the future</li>
     * </ul>
     *
     * @param grant the grant that should be validated
     * @return
     */
    GrantRuleResult test(final Grant grant,
                         final Set<Grant> historicalGrants);

}
