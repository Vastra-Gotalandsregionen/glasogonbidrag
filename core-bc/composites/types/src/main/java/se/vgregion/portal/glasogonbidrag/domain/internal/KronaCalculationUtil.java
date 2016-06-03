package se.vgregion.portal.glasogonbidrag.domain.internal;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class KronaCalculationUtil {
    /**
     * Swedish currency "Öre" multiplier.
     * There is 100 öre per swedish krona.
     */
    private static final BigDecimal PARTS_PER_KRONA = new BigDecimal(100);

    public BigDecimal calculatePartsAsKrona(long parts) {
        BigDecimal krona = new BigDecimal(parts);

        return krona.divide(PARTS_PER_KRONA, 2, RoundingMode.HALF_EVEN);
    }

    public long calculateKronaAsParts(BigDecimal krona) {
        return krona.multiply(PARTS_PER_KRONA).longValue();
    }

    public ValueAndVat calculateValueAndVatAsParts(BigDecimal valueAndVat) {
        BigDecimal value = valueAndVat.multiply(PARTS_PER_KRONA);

        BigDecimal valueDecimal = value.multiply(new BigDecimal("0.8"));
        BigDecimal vatDecimal = value.subtract(valueDecimal);

        long amount = valueDecimal
                .setScale(0, RoundingMode.HALF_DOWN).longValue();
        long vat = vatDecimal
                .setScale(0, RoundingMode.HALF_UP).longValue();

        return new ValueAndVat(amount, vat);
    }

    /**
     * Utility class to return single object encapsulating both VAT and
     * the actual value.
     */
    public class ValueAndVat {
        private long value;
        private long vat;

        ValueAndVat(long value, long vat) {
            this.value = value;
            this.vat = vat;
        }

        public long getValue() {
            return value;
        }

        public long getVat() {
            return vat;
        }
    }
}
