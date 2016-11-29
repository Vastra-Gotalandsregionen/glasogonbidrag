package se.vgregion.portal.glasogonbidrag.domain.jpa;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class GrantCalculationTest {
    @Test
    public void testGetAmountAsKrona() {
        Grant grant1 = new Grant();
        grant1.setAmount(100);
        BigDecimal actual1 = grant1.getAmountAsKrona();
        BigDecimal expected1 =
                (new BigDecimal(1)).setScale(2, RoundingMode.HALF_EVEN);

        org.junit.Assert.assertEquals("amount of 100 should equal 1 as krona",
                expected1, actual1);

        Grant grant2  = new Grant();
        grant2.setAmount(50050);
        BigDecimal actual2 = grant2.getAmountAsKrona();
        BigDecimal expected2 =
                (new BigDecimal(500))
                        .add((new BigDecimal(1)).divide(
                                new BigDecimal(2), 2, RoundingMode.HALF_EVEN));
        Assert.assertEquals("amount of 50050 should equal 500.50 as krona",
                expected2, actual2);

        Grant grant3  = new Grant();
        grant3.setAmount(50);
        BigDecimal actual3 = grant3.getAmountAsKrona();
        BigDecimal expected3 =
                (new BigDecimal(1)).divide(
                        new BigDecimal(2), 2, RoundingMode.HALF_EVEN);
        Assert.assertEquals("amount of 50 should equal 0.5 as krona",
                expected3, actual3);
    }

    @Test
    public void testSetAmountAsKrona() {
        Grant grant = new Grant();

        grant.setAmountAsKrona(new BigDecimal(100));
        Assert.assertEquals("100 as krona should be 10000",
                10000L, grant.getAmount());

        grant.setAmountAsKrona(new BigDecimal(199.99));
        Assert.assertEquals("199.99 as krona should be 19999",
                19999L, grant.getAmount());

        grant.setAmountAsKrona(new BigDecimal(555.555));
        Assert.assertEquals("555.555 as krona should be 55555 (truncate)",
                55555L, grant.getAmount());
    }

}
