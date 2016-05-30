package se.vgregion.portal.glasogonbidrag.domain.jpa;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

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
    public void testGetVatAsKrona() {
        Invoice invoice1  = new Invoice();
        invoice1.setVat(100);
        BigDecimal actual1 = invoice1.getVatAsKrona();
        BigDecimal expected1 =
                (new BigDecimal(1)).setScale(2, RoundingMode.HALF_EVEN);

        Assert.assertEquals("vat of 100 should equal 1 as krona",
                expected1, actual1);

        Invoice invoice2  = new Invoice();
        invoice2.setVat(50050);
        BigDecimal actual2 = invoice2.getVatAsKrona();
        BigDecimal expected2 =
                (new BigDecimal(500))
                        .add((new BigDecimal(1)).divide(
                                new BigDecimal(2), 2, RoundingMode.HALF_EVEN));
        Assert.assertEquals("vat of 50050 should equal 500.50 as krona",
                expected2, actual2);

        Invoice invoice3  = new Invoice();
        invoice3.setVat(50);
        BigDecimal actual3 = invoice3.getVatAsKrona();
        BigDecimal expected3 =
                (new BigDecimal(1)).divide(
                        new BigDecimal(2), 2, RoundingMode.HALF_EVEN);
        Assert.assertEquals("vat of 50 should equal 0.5 as krona",
                expected3, actual3);
    }

    @Test
    public void testSetVatAsKrona() {
        Invoice invoice = new Invoice();

        invoice.setVatAsKrona(new BigDecimal(100));
        Assert.assertEquals("100 as krona should be 10000",
                10000L, invoice.getVat());

        invoice.setVatAsKrona(new BigDecimal(199.99));
        Assert.assertEquals("199.99 as krona should be 19999",
                19999L, invoice.getVat());

        invoice.setVatAsKrona(new BigDecimal(555.555));
        Assert.assertEquals("555.555 as krona should be 55555 (truncate)",
                55555L, invoice.getVat());
    }

    @Test
    public void testGetAmountIncludingVatAsKrona() {
        Invoice invoice1  = new Invoice();
        invoice1.setAmount(100);
        invoice1.setVat(25);
        BigDecimal actual1 = invoice1.getAmountIncludingVatAsKrona();
        BigDecimal expected1 =
                (new BigDecimal(1)).add(
                        (new BigDecimal(1)).divide(
                                new BigDecimal(4), 2, RoundingMode.HALF_EVEN));
        Assert.assertEquals("amount of 100 and vat of 25 " +
                            "should equal 1.25 as krona",
                expected1, actual1);

        Invoice invoice2  = new Invoice();
        invoice2.setAmount(50050);
        invoice2.setVat(12512);
        BigDecimal actual2 = invoice2.getAmountIncludingVatAsKrona();
        BigDecimal expected2 =
                (new BigDecimal(625))
                        .add((new BigDecimal(62)).divide(
                                new BigDecimal(100), 2, RoundingMode.HALF_EVEN));
        Assert.assertEquals("amount of 50050 and vat of 12512 " +
                            "should equal 625.62 as krona",
                expected2, actual2);

        Invoice invoice3  = new Invoice();
        invoice3.setAmount(50);
        invoice3.setVat(12);
        BigDecimal actual3 = invoice3.getAmountIncludingVatAsKrona();
        BigDecimal expected3 =
                (new BigDecimal(62)).divide(
                        new BigDecimal(100), 2, RoundingMode.HALF_EVEN);
        Assert.assertEquals("amount of 50 vat of 12 " +
                            "should equal 0.62 as krona",
                expected3, actual3);
    }

    @Test
    public void testSetAmountIncludingVatAsKrona() {
        Invoice invoice = new Invoice();

        // Set amount and vat to 100.
        BigDecimal value1 = new BigDecimal("100");
        invoice.setAmountIncludingVatAsKrona(value1);

        BigDecimal expectedAmountKrona1 =
                (new BigDecimal("80")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal amountKrona1 = invoice.getAmountAsKrona();

        BigDecimal expectedVatKrona1 =
                (new BigDecimal("20")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal vatKrona1 = invoice.getVatAsKrona();

        BigDecimal expectedValueKrona1 =
                (new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal valueKrona1 = invoice.getAmountIncludingVatAsKrona();

        Assert.assertEquals("Amount krona should be 80",
                expectedAmountKrona1, amountKrona1);
        Assert.assertEquals("Vat krona should be 20",
                expectedVatKrona1, vatKrona1);
        Assert.assertEquals("Amount including vat as krona should be 100",
                expectedValueKrona1, valueKrona1);

        Assert.assertEquals("Amount should be 8000",
                8000L,
                invoice.getAmount());
        Assert.assertEquals("vat should be 2000",
                2000L,
                invoice.getVat());


        // Set amount and vat to 125.
        BigDecimal value2 = new BigDecimal("125");
        invoice.setAmountIncludingVatAsKrona(value2);

        BigDecimal expectedAmountKrona2 =
                (new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal amountKrona2 = invoice.getAmountAsKrona();

        BigDecimal expectedVatKrona2 =
                (new BigDecimal("25")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal vatKrona2 = invoice.getVatAsKrona();

        BigDecimal expectedValueKrona2 =
                (new BigDecimal("125")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal valueKrona2 = invoice.getAmountIncludingVatAsKrona();

        Assert.assertEquals("Amount krona should be 100",
                expectedAmountKrona2, amountKrona2);
        Assert.assertEquals("Vat krona should be 25",
                expectedVatKrona2, vatKrona2);
        Assert.assertEquals("Amount including vat as krona should be 125",
                expectedValueKrona2, valueKrona2);

        Assert.assertEquals("Amount should be 10000",
                10000L,
                invoice.getAmount());
        Assert.assertEquals("vat should be 2500",
                2500L,
                invoice.getVat());

        // Set amount and vat to 199.99.
        BigDecimal value3 = new BigDecimal("199.99");
        invoice.setAmountIncludingVatAsKrona(value3);

        BigDecimal expectedAmountKrona3 =
                (new BigDecimal("159.99")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal amountKrona3 = invoice.getAmountAsKrona();

        BigDecimal expectedVatKrona3 =
                (new BigDecimal("40")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal vatKrona3 = invoice.getVatAsKrona();

        BigDecimal expectedValueKrona3 =
                (new BigDecimal("199.99")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal valueKrona3 = invoice.getAmountIncludingVatAsKrona();

        Assert.assertEquals("Amount krona should be 159.99",
                expectedAmountKrona3, amountKrona3);
        Assert.assertEquals("Vat krona should be 40",
                expectedVatKrona3, vatKrona3);
        Assert.assertEquals("Amount including vat as krona should be 199.99",
                expectedValueKrona3, valueKrona3);

        Assert.assertEquals("Amount should be 15999",
                15999L, invoice.getAmount());
        Assert.assertEquals("vat should be 4000",
                4000L, invoice.getVat());

        // Set amount and vat to 99.50.
        BigDecimal value4 = new BigDecimal("99.50");
        invoice.setAmountIncludingVatAsKrona(value4);

        BigDecimal expectedAmountKrona4 =
                (new BigDecimal("79.60")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal amountKrona4 = invoice.getAmountAsKrona();

        BigDecimal expectedVatKrona4 =
                (new BigDecimal("19.90")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal vatKrona4 = invoice.getVatAsKrona();

        BigDecimal expectedValueKrona4 =
                (new BigDecimal("99.50")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal valueKrona4 = invoice.getAmountIncludingVatAsKrona();

        Assert.assertEquals("Amount krona should be 79.60",
                expectedAmountKrona4, amountKrona4);
        Assert.assertEquals("Vat krona should be 19.90",
                expectedVatKrona4, vatKrona4);
        Assert.assertEquals("Amount including vat as krona should be 99.50",
                expectedValueKrona4, valueKrona4);

        Assert.assertEquals("Amount should be 7960",
                7960L,
                invoice.getAmount());
        Assert.assertEquals("vat should be 1990",
                1990L,
                invoice.getVat());

        // Set amount and vat to 249.99375 (actual result shuold be 249.99).
        //   This is an edge-case calculating VAT.
        BigDecimal value5 = new BigDecimal("249.99375");
        invoice.setAmountIncludingVatAsKrona(value5);

        BigDecimal expectedAmountKrona5 =
                (new BigDecimal("199.99")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal amountKrona5 = invoice.getAmountAsKrona();

        BigDecimal expectedVatKrona5 =
                (new BigDecimal("50")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal vatKrona5 = invoice.getVatAsKrona();

        BigDecimal expectedValueKrona5 =
                (new BigDecimal("249.99375")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal valueKrona5 = invoice.getAmountIncludingVatAsKrona();

        Assert.assertEquals("Amount krona should be 199.99",
                expectedAmountKrona5, amountKrona5);
        Assert.assertEquals("Vat krona should be 50",
                expectedVatKrona5, vatKrona5);
        Assert.assertEquals("Amount including vat as krona should be 249.99",
                expectedValueKrona5, valueKrona5);

        Assert.assertEquals("Amount should be 19999",
                19999L,
                invoice.getAmount());
        Assert.assertEquals("vat should be 5000",
                5000L,
                invoice.getVat());
    }

    @Test
    public void testSummationOfGrants() {
        Invoice invoice = new Invoice();

        Grant grant1 = new Grant();
        grant1.setAmount(10000);
        grant1.setVat(2500);

        Grant grant2 = new Grant();
        grant2.setAmount(80000);
        grant2.setVat(20000);

        Grant grant3 = new Grant();
        grant3.setAmount(30000);
        grant3.setVat(7500);

        List<Grant> grants = Arrays.asList(grant1, grant2, grant3);
        invoice.setGrants(grants);

        BigDecimal expectedAmount =
                new BigDecimal(1200).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal actualAmount = invoice.calculateGrantsAmountSumAsKrona();
        Assert.assertEquals("Sum of grants' amount 10000, 80000 and 30000 " +
                            "should equal 1200.00",
                expectedAmount,
                actualAmount);

        BigDecimal expectedVat =
                new BigDecimal(300).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal actualVat = invoice.calculateGrantsVatSumAsKrona();
        Assert.assertEquals("Sum of grants' vat 2500, 20000 and 7500 " +
                        "should equal 300.00",
                expectedAmount,
                actualAmount);

        BigDecimal expectedAmountIncludingVat =
                new BigDecimal(1500).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal actualAmountIncludingVat =
                invoice.calculateGrantsAmountIncludingVatSumAsKrona();
        Assert.assertEquals("Sum of grants' amount 10000, 80000 and 30000 " +
                            "and vat 2500, 20000 and 7500 " +
                            "should equal 1500.00",
                expectedAmountIncludingVat,
                actualAmountIncludingVat);
    }

    @Test
    public void testDifferenceNoGrants() {
        Invoice invoice = new Invoice();
        invoice.setAmount(90000);
        invoice.setVat(22500);

        BigDecimal expectedDifferenceInclVat =
                invoice.getAmountIncludingVatAsKrona();
        BigDecimal actualDifferenceInclVat =
                invoice.calculateDifferenceIncludingVatAsKrona();
        Assert.assertEquals(
                "Difference including vat with no grants " +
                        "should be original amount including vat.",
                expectedDifferenceInclVat,
                actualDifferenceInclVat);

        BigDecimal expectedDifferenceExclVat =
                invoice.getAmountAsKrona();
        BigDecimal actualDifferenceExclvat =
                invoice.calculateDifferenceExcludingVatAsKrona();
        Assert.assertEquals(
                "Difference excluding vat with no grants " +
                        "should be original amount including vat.",
                expectedDifferenceExclVat,
                actualDifferenceExclvat);

    }

    @Test
    public void testDifferenceCalculation() {
        Invoice invoice = new Invoice();
        invoice.setAmount(90000);
        invoice.setVat(22500);

        BigDecimal expectedDifferenceExcludingVat0 =
                (new BigDecimal(900)).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal actualDifferenceExcludingVat0 =
                invoice.calculateDifferenceExcludingVatAsKrona();
        Assert.assertEquals(
                "Difference excluding vat with no grants " +
                        "should be 900.00.",
                expectedDifferenceExcludingVat0,
                actualDifferenceExcludingVat0);

        BigDecimal expectedDifferenceIncludingVat0 =
                (new BigDecimal(1125)).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal actualDifferenceIncludingVat0 =
                invoice.calculateDifferenceIncludingVatAsKrona();
        Assert.assertEquals(
                "Difference including vat with no grants " +
                        "should be 1125.00.",
                expectedDifferenceExcludingVat0,
                actualDifferenceExcludingVat0);

        Grant grant1 = new Grant();
        grant1.setAmount(10000);
        grant1.setVat(2500);
        invoice.addGrant(grant1);

        BigDecimal expectedDifferenceExcludingVat1 =
                (new BigDecimal(800)).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal actualDifferenceExcludingVat1 =
                invoice.calculateDifferenceExcludingVatAsKrona();
        Assert.assertEquals(
                "Difference excluding vat with no grants " +
                        "should be 900.00.",
                expectedDifferenceExcludingVat1,
                actualDifferenceExcludingVat1);

        BigDecimal expectedDifferenceIncludingVat1 =
                (new BigDecimal(1000)).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal actualDifferenceIncludingVat1 =
                invoice.calculateDifferenceIncludingVatAsKrona();
        Assert.assertEquals(
                "Difference including vat with no grants " +
                        "should be 1000.00.",
                expectedDifferenceExcludingVat1,
                actualDifferenceExcludingVat1);

        Grant grant2 = new Grant();
        grant2.setAmount(80000);
        grant2.setVat(20000);
        invoice.addGrant(grant2);

        BigDecimal expectedDifferenceExcludingVat2 =
                (new BigDecimal(0)).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal actualDifferenceExcludingVat2 =
                invoice.calculateDifferenceExcludingVatAsKrona();
        Assert.assertEquals(
                "Difference excluding vat with no grants " +
                        "should be 0.00.",
                expectedDifferenceExcludingVat2,
                actualDifferenceExcludingVat2);

        BigDecimal expectedDifferenceIncludingVat2 =
                (new BigDecimal(0)).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal actualDifferenceIncludingVat2 =
                invoice.calculateDifferenceIncludingVatAsKrona();
        Assert.assertEquals(
                "Difference including vat with no grants " +
                        "should be 0.00.",
                expectedDifferenceExcludingVat2,
                actualDifferenceExcludingVat2);

        Grant grant3 = new Grant();
        grant3.setAmount(30000);
        grant3.setVat(7500);
        invoice.addGrant(grant3);

        BigDecimal expectedDifferenceExcludingVat3 =
                (new BigDecimal(300))
                        .setScale(2, RoundingMode.HALF_EVEN).negate();
        BigDecimal actualDifferenceExcludingVat3 =
                invoice.calculateDifferenceExcludingVatAsKrona();
        Assert.assertEquals(
                "Difference excluding vat with no grants " +
                        "should be negative 300.00.",
                expectedDifferenceExcludingVat3,
                actualDifferenceExcludingVat3);

        BigDecimal expectedDifferenceIncludingVat3 =
                (new BigDecimal(375))
                        .setScale(2, RoundingMode.HALF_EVEN).negate();
        BigDecimal actualDifferenceIncludingVat3 =
                invoice.calculateDifferenceIncludingVatAsKrona();
        Assert.assertEquals(
                "Difference including vat with no grants " +
                        "should be negative 375.00.",
                expectedDifferenceExcludingVat3,
                actualDifferenceExcludingVat3);

    }
}
