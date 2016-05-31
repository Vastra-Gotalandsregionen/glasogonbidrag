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

    @Test
    public void testGetVatAsKrona() {
        Grant grant1  = new Grant();
        grant1.setVat(100);
        BigDecimal actual1 = grant1.getVatAsKrona();
        BigDecimal expected1 =
                (new BigDecimal(1)).setScale(2, RoundingMode.HALF_EVEN);

        Assert.assertEquals("vat of 100 should equal 1 as krona",
                expected1, actual1);

        Grant grant2  = new Grant();
        grant2.setVat(50050);
        BigDecimal actual2 = grant2.getVatAsKrona();
        BigDecimal expected2 =
                (new BigDecimal(500))
                        .add((new BigDecimal(1)).divide(
                                new BigDecimal(2), 2, RoundingMode.HALF_EVEN));
        Assert.assertEquals("vat of 50050 should equal 500.50 as krona",
                expected2, actual2);

        Grant grant3  = new Grant();
        grant3.setVat(50);
        BigDecimal actual3 = grant3.getVatAsKrona();
        BigDecimal expected3 =
                (new BigDecimal(1)).divide(
                        new BigDecimal(2), 2, RoundingMode.HALF_EVEN);
        Assert.assertEquals("vat of 50 should equal 0.5 as krona",
                expected3, actual3);
    }

    @Test
    public void testSetVatAsKrona() {
        Grant grant = new Grant();

        grant.setVatAsKrona(new BigDecimal(100));
        Assert.assertEquals("100 as krona should be 10000",
                10000L, grant.getVat());

        grant.setVatAsKrona(new BigDecimal(199.99));
        Assert.assertEquals("199.99 as krona should be 19999",
                19999L, grant.getVat());

        grant.setVatAsKrona(new BigDecimal(555.555));
        Assert.assertEquals("555.555 as krona should be 55555 (truncate)",
                55555L, grant.getVat());
    }

    @Test
    public void testGetAmountIncludingVatAsKrona() {
        Grant grant1  = new Grant();
        grant1.setAmount(100);
        grant1.setVat(25);
        BigDecimal actual1 = grant1.getAmountIncludingVatAsKrona();
        BigDecimal expected1 =
                (new BigDecimal(1)).add(
                        (new BigDecimal(1)).divide(
                                new BigDecimal(4), 2, RoundingMode.HALF_EVEN));
        Assert.assertEquals("amount of 100 and vat of 25 " +
                        "should equal 1.25 as krona",
                expected1, actual1);

        Grant grant2  = new Grant();
        grant2.setAmount(50050);
        grant2.setVat(12512);
        BigDecimal actual2 = grant2.getAmountIncludingVatAsKrona();
        BigDecimal expected2 =
                (new BigDecimal(625))
                        .add((new BigDecimal(62)).divide(
                                new BigDecimal(100), 2, RoundingMode.HALF_EVEN));
        Assert.assertEquals("amount of 50050 and vat of 12512 " +
                        "should equal 625.62 as krona",
                expected2, actual2);

        Grant grant3  = new Grant();
        grant3.setAmount(50);
        grant3.setVat(12);
        BigDecimal actual3 = grant3.getAmountIncludingVatAsKrona();
        BigDecimal expected3 =
                (new BigDecimal(62)).divide(
                        new BigDecimal(100), 2, RoundingMode.HALF_EVEN);
        Assert.assertEquals("amount of 50 vat of 12 " +
                        "should equal 0.62 as krona",
                expected3, actual3);
    }

    @Test
    public void testSetAmountIncludingVatAsKrona() {
        Grant grant = new Grant();

        // Set amount and vat to 100.
        BigDecimal value1 = new BigDecimal("100");
        grant.setAmountIncludingVatAsKrona(value1);

        BigDecimal expectedAmountKrona1 =
                (new BigDecimal("80")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal amountKrona1 = grant.getAmountAsKrona();

        BigDecimal expectedVatKrona1 =
                (new BigDecimal("20")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal vatKrona1 = grant.getVatAsKrona();

        BigDecimal expectedValueKrona1 =
                (new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal valueKrona1 = grant.getAmountIncludingVatAsKrona();

        Assert.assertEquals("Amount krona should be 80",
                expectedAmountKrona1, amountKrona1);
        Assert.assertEquals("Vat krona should be 20",
                expectedVatKrona1, vatKrona1);
        Assert.assertEquals("Amount including vat as krona should be 100",
                expectedValueKrona1, valueKrona1);

        Assert.assertEquals("Amount should be 8000",
                8000L,
                grant.getAmount());
        Assert.assertEquals("vat should be 2000",
                2000L,
                grant.getVat());


        // Set amount and vat to 125.
        BigDecimal value2 = new BigDecimal("125");
        grant.setAmountIncludingVatAsKrona(value2);

        BigDecimal expectedAmountKrona2 =
                (new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal amountKrona2 = grant.getAmountAsKrona();

        BigDecimal expectedVatKrona2 =
                (new BigDecimal("25")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal vatKrona2 = grant.getVatAsKrona();

        BigDecimal expectedValueKrona2 =
                (new BigDecimal("125")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal valueKrona2 = grant.getAmountIncludingVatAsKrona();

        Assert.assertEquals("Amount krona should be 100",
                expectedAmountKrona2, amountKrona2);
        Assert.assertEquals("Vat krona should be 25",
                expectedVatKrona2, vatKrona2);
        Assert.assertEquals("Amount including vat as krona should be 125",
                expectedValueKrona2, valueKrona2);

        Assert.assertEquals("Amount should be 10000",
                10000L,
                grant.getAmount());
        Assert.assertEquals("vat should be 2500",
                2500L,
                grant.getVat());

        // Set amount and vat to 199.99.
        BigDecimal value3 = new BigDecimal("199.99");
        grant.setAmountIncludingVatAsKrona(value3);

        BigDecimal expectedAmountKrona3 =
                (new BigDecimal("159.99")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal amountKrona3 = grant.getAmountAsKrona();

        BigDecimal expectedVatKrona3 =
                (new BigDecimal("40")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal vatKrona3 = grant.getVatAsKrona();

        BigDecimal expectedValueKrona3 =
                (new BigDecimal("199.99")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal valueKrona3 = grant.getAmountIncludingVatAsKrona();

        Assert.assertEquals("Amount krona should be 159.99",
                expectedAmountKrona3, amountKrona3);
        Assert.assertEquals("Vat krona should be 40",
                expectedVatKrona3, vatKrona3);
        Assert.assertEquals("Amount including vat as krona should be 199.99",
                expectedValueKrona3, valueKrona3);

        Assert.assertEquals("Amount should be 15999",
                15999L, grant.getAmount());
        Assert.assertEquals("vat should be 4000",
                4000L, grant.getVat());

        // Set amount and vat to 99.50.
        BigDecimal value4 = new BigDecimal("99.50");
        grant.setAmountIncludingVatAsKrona(value4);

        BigDecimal expectedAmountKrona4 =
                (new BigDecimal("79.60")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal amountKrona4 = grant.getAmountAsKrona();

        BigDecimal expectedVatKrona4 =
                (new BigDecimal("19.90")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal vatKrona4 = grant.getVatAsKrona();

        BigDecimal expectedValueKrona4 =
                (new BigDecimal("99.50")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal valueKrona4 = grant.getAmountIncludingVatAsKrona();

        Assert.assertEquals("Amount krona should be 79.60",
                expectedAmountKrona4, amountKrona4);
        Assert.assertEquals("Vat krona should be 19.90",
                expectedVatKrona4, vatKrona4);
        Assert.assertEquals("Amount including vat as krona should be 99.50",
                expectedValueKrona4, valueKrona4);

        Assert.assertEquals("Amount should be 7960",
                7960L,
                grant.getAmount());
        Assert.assertEquals("vat should be 1990",
                1990L,
                grant.getVat());

        // Set amount and vat to 249.99375 (actual result shuold be 249.99).
        //   This is an edge-case calculating VAT.
        BigDecimal value5 = new BigDecimal("249.99375");
        grant.setAmountIncludingVatAsKrona(value5);

        BigDecimal expectedAmountKrona5 =
                (new BigDecimal("199.99")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal amountKrona5 = grant.getAmountAsKrona();

        BigDecimal expectedVatKrona5 =
                (new BigDecimal("50")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal vatKrona5 = grant.getVatAsKrona();

        BigDecimal expectedValueKrona5 =
                (new BigDecimal("249.99375")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal valueKrona5 = grant.getAmountIncludingVatAsKrona();

        Assert.assertEquals("Amount krona should be 199.99",
                expectedAmountKrona5, amountKrona5);
        Assert.assertEquals("Vat krona should be 50",
                expectedVatKrona5, vatKrona5);
        Assert.assertEquals("Amount including vat as krona should be 249.99",
                expectedValueKrona5, valueKrona5);

        Assert.assertEquals("Amount should be 19999",
                19999L,
                grant.getAmount());
        Assert.assertEquals("vat should be 5000",
                5000L,
                grant.getVat());
    }
}
