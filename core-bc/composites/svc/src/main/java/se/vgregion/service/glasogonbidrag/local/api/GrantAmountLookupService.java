package se.vgregion.service.glasogonbidrag.local.api;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Diagnose;

import java.math.BigDecimal;
import java.util.Date;

/**
 * This service will return the max amount a grant will be able to grant.
 *
 * @author Martin Lind - Monator Technologies AB
 */
public interface GrantAmountLookupService {

    /**
     * Return the amount that is granted for a diagnose at a
     * specific delivery date for someone at a specific with birth date.
     *
     * @param diagnose the diagnose to lookup grantable amount for.
     * @param deliveryDate the date when the grant was granted.
     * @param birthDate birth date of the person receiving the grant
     * @return Amount in SEK that is grantable.
     */
    BigDecimal grantableAmount(Diagnose diagnose,
                               Date deliveryDate,
                               Date birthDate);

}
