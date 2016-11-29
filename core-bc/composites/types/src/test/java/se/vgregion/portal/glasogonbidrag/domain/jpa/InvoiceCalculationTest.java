package se.vgregion.portal.glasogonbidrag.domain.jpa;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class InvoiceCalculationTest {

    @Test
    public void testGetAmountAsKrona() {
        Invoice invoice1 = new Invoice();
        invoice1.setAmount(100);
        BigDecimal actual1 = invoice1.getAmountAsKrona();
        BigDecimal expected1 =
                (new BigDecimal(1)).setScale(2, RoundingMode.HALF_EVEN);

        org.junit.Assert.assertEquals("amount of 100 should equal 1 as krona",
                expected1, actual1);

        Invoice invoice2  = new Invoice();
        invoice2.setAmount(50050);
        BigDecimal actual2 = invoice2.getAmountAsKrona();
        BigDecimal expected2 =
                (new BigDecimal(500))
                        .add((new BigDecimal(1)).divide(
                                new BigDecimal(2), 2, RoundingMode.HALF_EVEN));
        Assert.assertEquals("amount of 50050 should equal 500.50 as krona",
                expected2, actual2);

        Invoice invoice3  = new Invoice();
        invoice3.setAmount(50);
        BigDecimal actual3 = invoice3.getAmountAsKrona();
        BigDecimal expected3 =
                (new BigDecimal(1)).divide(
                        new BigDecimal(2), 2, RoundingMode.HALF_EVEN);
        Assert.assertEquals("amount of 50 should equal 0.5 as krona",
                expected3, actual3);
    }

    @Test
    public void testSetAmountAsKrona() {
        Invoice invoice = new Invoice();

        invoice.setAmountAsKrona(new BigDecimal(100));
        Assert.assertEquals("100 as krona should be 10000",
                10000L, invoice.getAmount());

        invoice.setAmountAsKrona(new BigDecimal(199.99));
        Assert.assertEquals("199.99 as krona should be 19999",
                19999L, invoice.getAmount());

        invoice.setAmountAsKrona(new BigDecimal(555.555));
        Assert.assertEquals("555.555 as krona should be 55555 (truncate)",
                55555L, invoice.getAmount());
    }


    @Test
    public void testSummationOfGrants() {
        Invoice invoice = new Invoice();

        Grant grant1 = new Grant();
        grant1.setAmount(10000);

        Grant grant2 = new Grant();
        grant2.setAmount(80000);

        Grant grant3 = new Grant();
        grant3.setAmount(30000);

        Set<Grant> grants = new HashSet<Grant>();
        grants.add(grant1);
        grants.add(grant2);
        grants.add(grant3);
        invoice.setGrants(grants);

        BigDecimal expectedAmount =
                new BigDecimal(1200).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal actualAmount = invoice.calculateGrantsAmountSumAsKrona();
        Assert.assertEquals("Sum of grants' amount 10000, 80000 and 30000 " +
                            "should equal 1200.00",
                expectedAmount,
                actualAmount);
    }

    @Test
    public void testDifferenceNoGrants() {
        Invoice invoice = new Invoice();
        invoice.setAmount(90000);

        BigDecimal expectedDifference =
                invoice.getAmountAsKrona();
        BigDecimal actualDifference =
                invoice.calculateDifferenceAsKrona();
        Assert.assertEquals(
                "Difference with no grants " +
                        "should be original amount.",
                expectedDifference,
                actualDifference);

    }

    @Test
    public void testDifferenceCalculation() {
        Invoice invoice = new Invoice();
        invoice.setAmount(90000);

        BigDecimal expectedDifference0 =
                (new BigDecimal(900)).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal actualDifference0 =
                invoice.calculateDifferenceAsKrona();
        Assert.assertEquals(
                "Difference with no grants " +
                        "should be 900.00.",
                expectedDifference0,
                actualDifference0);

        Grant grant1 = new Grant();
        grant1.setAmount(10000);
        invoice.addGrant(grant1);

        BigDecimal expectedDifference1 =
                (new BigDecimal(800)).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal actualDifference1 =
                invoice.calculateDifferenceAsKrona();
        Assert.assertEquals(
                "Difference with one grants " +
                        "should be 800.00.",
                expectedDifference1,
                actualDifference1);

        Grant grant2 = new Grant();
        grant2.setAmount(80000);
        invoice.addGrant(grant2);

        BigDecimal expectedDifference2 =
                (new BigDecimal(0)).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal actualDifference2 =
                invoice.calculateDifferenceAsKrona();
        Assert.assertEquals(
                "Difference with two grants " +
                        "should be 0.00.",
                expectedDifference2,
                actualDifference2);

        Grant grant3 = new Grant();
        grant3.setAmount(30000);
        invoice.addGrant(grant3);

        BigDecimal expectedDifference3 =
                (new BigDecimal(300))
                        .setScale(2, RoundingMode.HALF_EVEN).negate();
        BigDecimal actualDifference3 =
                invoice.calculateDifferenceAsKrona();
        Assert.assertEquals(
                "Difference with three grants " +
                        "should be negative 300.00.",
                expectedDifference3,
                actualDifference3);

    }
}
