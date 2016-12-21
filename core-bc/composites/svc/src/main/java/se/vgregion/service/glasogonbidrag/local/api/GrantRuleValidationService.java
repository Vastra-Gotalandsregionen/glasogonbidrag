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

    /**
     *
     * Method that checks if total grant sum including the
     * new grant is not larger than invoice amount
     *
     * @param grant the new grant that is shall be added
     * @param grants grants on the invoice
     * @param invoice the invoice
     */
    GrantRuleResult mayAddToInvoice(Grant grant, Set<Grant> grants,
                                    Invoice invoice);

    /**
     *
     * Method that checks data to se if a grant is valid or not.
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
     */
    GrantRuleResult test(final Grant grant,
                         final Set<Grant> historicalGrants);

}
