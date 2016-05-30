package se.vgregion.portal.glasogonbidrag.domain;

import java.math.BigDecimal;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public final class CurrencyConstants {
    /**
     * Swedish currency "Öre" multiplier.
     * There is 100 öre per swedish krona.
     */
    public static final BigDecimal PARTS_PER_KRONA = new BigDecimal(100);
}
