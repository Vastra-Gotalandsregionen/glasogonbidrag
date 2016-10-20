package se.vgregion.service.glasogonbidrag.local;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.vgregion.portal.glasogonbidrag.domain.VisualLaterality;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Diagnose;
import se.vgregion.portal.glasogonbidrag.domain.jpa.diagnose.Aphakia;
import se.vgregion.portal.glasogonbidrag.domain.jpa.diagnose.Keratoconus;
import se.vgregion.portal.glasogonbidrag.domain.jpa.diagnose.None;
import se.vgregion.portal.glasogonbidrag.domain.jpa.diagnose.Special;
import se.vgregion.service.glasogonbidrag.local.api.GrantAmountLookupService;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:localTestContext.xml")
public class GrantAmountLookupServiceTest {

    @Autowired
    private GrantAmountLookupService amountService;

    @Test
    public void serviceShouldExists() {
        Assert.assertNotNull(amountService);
    }

    @Test
    public void aphakiaGrantableAmount() {
        Diagnose diagnose = new Aphakia(VisualLaterality.BILATERAL);

        // Amount will differ depending on which rules have been used.
        // The rules have changed 2016-03-01 and 2016-06-20.
        Date delivery2000kr1 =
                new GregorianCalendar(2015, Calendar.OCTOBER, 1).getTime();
        Date delivery2000kr2 =
                new GregorianCalendar(2016, Calendar.MARCH, 1).getTime();
        Date delivery2000kr3 =
                new GregorianCalendar(2016, Calendar.JUNE, 19).getTime();
        Date delivery2400kr1 =
                new GregorianCalendar(2016, Calendar.JUNE, 20).getTime();
        Date delivery2400kr2 =
                new GregorianCalendar(2016, Calendar.OCTOBER,1).getTime();

        // The beneficiary's birthday doesn't matter, will use this for
        // all test cases.
        Date birthDate =
            new GregorianCalendar(1956, Calendar.AUGUST, 8).getTime();

        // Assertions
        Assert.assertEquals(new BigDecimal("2000"),
                amountService.grantableAmount(
                        diagnose, delivery2000kr1, birthDate));
        Assert.assertEquals(new BigDecimal("2000"),
                amountService.grantableAmount(
                        diagnose, delivery2000kr2, birthDate));
        Assert.assertEquals(new BigDecimal("2000"),
                amountService.grantableAmount(
                        diagnose, delivery2000kr3, birthDate));
        Assert.assertEquals(new BigDecimal("2400"),
                amountService.grantableAmount(
                        diagnose, delivery2400kr1, birthDate));
        Assert.assertEquals(new BigDecimal("2400"),
                amountService.grantableAmount(
                        diagnose, delivery2400kr2, birthDate));
    }

    @Test
    public void specialGrantableAmount() {
        Diagnose diagnose = new Special(VisualLaterality.LEFT, false);

        // Amount will differ depending on which rules have been used.
        // The rules have changed 2016-03-01 and 2016-06-20.
        Date delivery2000kr1 =
                new GregorianCalendar(2015, Calendar.OCTOBER, 1).getTime();
        Date delivery2000kr2 =
                new GregorianCalendar(2016, Calendar.MARCH, 1).getTime();
        Date delivery2000kr3 =
                new GregorianCalendar(2016, Calendar.JUNE, 19).getTime();
        Date delivery2400kr1 =
                new GregorianCalendar(2016, Calendar.JUNE, 20).getTime();
        Date delivery2400kr2 =
                new GregorianCalendar(2016, Calendar.OCTOBER,1).getTime();

        // The beneficiary's birthday doesn't matter, will use this for
        // all test cases.
        Date birthDate =
                new GregorianCalendar(1956, Calendar.AUGUST, 8).getTime();

        // Assertions
        Assert.assertEquals(new BigDecimal("2000"),
                amountService.grantableAmount(
                        diagnose, delivery2000kr1, birthDate));
        Assert.assertEquals(new BigDecimal("2000"),
                amountService.grantableAmount(
                        diagnose, delivery2000kr2, birthDate));
        Assert.assertEquals(new BigDecimal("2000"),
                amountService.grantableAmount(
                        diagnose, delivery2000kr3, birthDate));
        Assert.assertEquals(new BigDecimal("2400"),
                amountService.grantableAmount(
                        diagnose, delivery2400kr1, birthDate));
        Assert.assertEquals(new BigDecimal("2400"),
                amountService.grantableAmount(
                        diagnose, delivery2400kr2, birthDate));
    }

    @Test
    public void keratoconusGrantableAmount() {
        Diagnose diagnose =
                new Keratoconus(VisualLaterality.RIGHT, 1.0f, 0.0f, false);

        // Amount will differ depending on which rules have been used.
        // The rules have changed 2016-03-01 and 2016-06-20.
        Date delivery2400kr1 =
                new GregorianCalendar(2015, Calendar.OCTOBER, 1).getTime();
        Date delivery2400kr2 =
                new GregorianCalendar(2016, Calendar.MARCH, 1).getTime();
        Date delivery2400kr3 =
                new GregorianCalendar(2016, Calendar.JUNE, 19).getTime();
        Date delivery3000kr1 =
                new GregorianCalendar(2016, Calendar.JUNE, 20).getTime();
        Date delivery3000kr2 =
                new GregorianCalendar(2016, Calendar.OCTOBER,1).getTime();

        // The beneficiary's birthday doesn't matter, will use this for
        // all test cases.
        Date birthDate =
                new GregorianCalendar(1956, Calendar.AUGUST, 8).getTime();

        // Assertions
        Assert.assertEquals(new BigDecimal("2400"),
                amountService.grantableAmount(
                        diagnose, delivery2400kr1, birthDate));
        Assert.assertEquals(new BigDecimal("2400"),
                amountService.grantableAmount(
                        diagnose, delivery2400kr2, birthDate));
        Assert.assertEquals(new BigDecimal("2400"),
                amountService.grantableAmount(
                        diagnose, delivery2400kr3, birthDate));
        Assert.assertEquals(new BigDecimal("3000"),
                amountService.grantableAmount(
                        diagnose, delivery3000kr1, birthDate));
        Assert.assertEquals(new BigDecimal("3000"),
                amountService.grantableAmount(
                        diagnose, delivery3000kr2, birthDate));
    }

    @Test
    public void children0to15pre20160301() {
        Diagnose diagnose = new None();

        // Delivery date should be before 2016-03-01
        Date deliveryDate =
                new GregorianCalendar(2016, Calendar.FEBRUARY, 1).getTime();

        // Tests cases
        Date birthDateAge1 =
                new GregorianCalendar(2015, Calendar.FEBRUARY, 1).getTime();
        Date birthDateAge4 =
                new GregorianCalendar(2012, Calendar.FEBRUARY, 1).getTime();
        Date birthDateAge15 =
                new GregorianCalendar(2001, Calendar.FEBRUARY, 1).getTime();

        Calendar age16plusLessThan6Month =
                new GregorianCalendar(2000, Calendar.FEBRUARY, 1);
        age16plusLessThan6Month.add(Calendar.MONTH, -5);
        Date birthDateAge16plusLessThan6Month =
                age16plusLessThan6Month.getTime();

        Calendar age16plusExact6Month =
                new GregorianCalendar(2000, Calendar.FEBRUARY, 1);
        age16plusExact6Month.add(Calendar.MONTH, -6);
        Date birthDateAge16plusExact6Month = age16plusExact6Month.getTime();

        Calendar age16plusMoreThan6Month =
                new GregorianCalendar(2000, Calendar.FEBRUARY, 1);
        age16plusMoreThan6Month.add(Calendar.MONTH, -7);
        Date birthDateAge16plusMoreThan6Month =
                age16plusMoreThan6Month.getTime();

        // Assertions
        Assert.assertEquals(new BigDecimal("1000"),
                amountService.grantableAmount(
                        diagnose, deliveryDate, birthDateAge1));
        Assert.assertEquals(new BigDecimal("1000"),
                amountService.grantableAmount(
                        diagnose, deliveryDate, birthDateAge4));
        Assert.assertEquals(new BigDecimal("1000"),
                amountService.grantableAmount(
                        diagnose, deliveryDate, birthDateAge15));
        Assert.assertEquals(new BigDecimal("1000"),
                amountService.grantableAmount(
                        diagnose, deliveryDate,
                        birthDateAge16plusLessThan6Month));
        Assert.assertEquals(new BigDecimal("1000"),
                amountService.grantableAmount(
                        diagnose, deliveryDate,
                        birthDateAge16plusExact6Month));
        Assert.assertEquals(new BigDecimal("0"),
                amountService.grantableAmount(
                        diagnose, deliveryDate,
                        birthDateAge16plusMoreThan6Month));
    }

    @Test
    public void children16to19pre20160301() {
        Diagnose diagnose = new None();

        // Delivery date should be before 2016-03-01
        Date deliveryDate =
                new GregorianCalendar(2016, Calendar.FEBRUARY, 1).getTime();

        // Test cases
        Calendar age16plusMoreThan6Month =
                new GregorianCalendar(2000, Calendar.FEBRUARY, 1);
        age16plusMoreThan6Month.add(Calendar.MONTH, -7);
        Date birthDateAge16plusMoreThan6Month =
                age16plusMoreThan6Month.getTime();

        Date birthDateAge17 =
                new GregorianCalendar(1999, Calendar.FEBRUARY, 1).getTime();
        Date birthDateAge20 =
                new GregorianCalendar(1996, Calendar.FEBRUARY, 1).getTime();
        Calendar age20plusLessThan6Month =
                new GregorianCalendar(1996, Calendar.FEBRUARY, 1);
        age20plusLessThan6Month.add(Calendar.MONTH, -5);
        Date birthDateAge20plusLessThan6Month =
                age20plusLessThan6Month.getTime();

        Calendar age20plusMoreThan6Month =
                new GregorianCalendar(1996, Calendar.FEBRUARY, 1);
        age20plusMoreThan6Month.add(Calendar.MONTH, -7);
        Date birthDateAge20plusMoreThan6Month =
                age20plusMoreThan6Month.getTime();

        // Assertions
        Assert.assertEquals(new BigDecimal("0"),
                amountService.grantableAmount(
                        diagnose, deliveryDate,
                        birthDateAge16plusMoreThan6Month));
        Assert.assertEquals(new BigDecimal("0"),
                amountService.grantableAmount(
                        diagnose, deliveryDate, birthDateAge17));
        Assert.assertEquals(new BigDecimal("0"),
                amountService.grantableAmount(
                        diagnose, deliveryDate, birthDateAge20));
        Assert.assertEquals(new BigDecimal("0"),
                amountService.grantableAmount(
                        diagnose, deliveryDate,
                        birthDateAge20plusLessThan6Month));
        Assert.assertEquals(new BigDecimal("0"),
                amountService.grantableAmount(
                        diagnose, deliveryDate,
                        birthDateAge20plusMoreThan6Month));
    }

    @Test
    public void children0to15pre20160620() {
        Diagnose diagnose = new None();

        // Delivery date should be before 2016-06-20 and after 2016-03-01
        Date deliveryDate =
                new GregorianCalendar(2016, Calendar.MAY, 1).getTime();

        // Tests cases
        Date birthDateAge1 =
                new GregorianCalendar(2015, Calendar.MAY, 1).getTime();
        Date birthDateAge4 =
                new GregorianCalendar(2012, Calendar.MAY, 1).getTime();
        Date birthDateAge15 =
                new GregorianCalendar(2001, Calendar.MAY, 1).getTime();

        Calendar age16plusLessThan6Month =
                new GregorianCalendar(2000, Calendar.MAY, 1);
        age16plusLessThan6Month.add(Calendar.MONTH, -5);
        Date birthDateAge16plusLessThan6Month =
                age16plusLessThan6Month.getTime();

        Calendar age16plusExact6Month =
                new GregorianCalendar(2000, Calendar.MAY, 1);
        age16plusExact6Month.add(Calendar.MONTH, -6);
        Date birthDateAge16plusExact6Month = age16plusExact6Month.getTime();

        Calendar age16plusMoreThan6Month =
                new GregorianCalendar(2000, Calendar.MAY, 1);
        age16plusMoreThan6Month.add(Calendar.MONTH, -7);
        Date birthDateAge16plusMoreThan6Month =
                age16plusMoreThan6Month.getTime();

        // Assertions
        Assert.assertEquals(new BigDecimal("1000"),
                amountService.grantableAmount(
                        diagnose, deliveryDate, birthDateAge1));
        Assert.assertEquals(new BigDecimal("1000"),
                amountService.grantableAmount(
                        diagnose, deliveryDate, birthDateAge4));
        Assert.assertEquals(new BigDecimal("1000"),
                amountService.grantableAmount(
                        diagnose, deliveryDate, birthDateAge15));
        Assert.assertEquals(new BigDecimal("1000"),
                amountService.grantableAmount(
                        diagnose, deliveryDate,
                        birthDateAge16plusLessThan6Month));
        Assert.assertEquals(new BigDecimal("1000"),
                amountService.grantableAmount(
                        diagnose, deliveryDate,
                        birthDateAge16plusExact6Month));
        Assert.assertEquals(new BigDecimal("800"),
                amountService.grantableAmount(
                        diagnose, deliveryDate,
                        birthDateAge16plusMoreThan6Month));
    }

    @Test
    public void children16to19pre20160620() {
        Diagnose diagnose = new None();

        // Delivery date should be before 2016-06-20 and after 2016-03-01
        Date deliveryDate =
                new GregorianCalendar(2016, Calendar.MAY, 1).getTime();

        // Test cases
        Calendar age16plusMoreThan6Month =
                new GregorianCalendar(2000, Calendar.MAY, 1);
        age16plusMoreThan6Month.add(Calendar.MONTH, -7);
        Date birthDateAge16plusMoreThan6Month =
                age16plusMoreThan6Month.getTime();

        Date birthDateAge17 =
                new GregorianCalendar(1999, Calendar.MAY, 1).getTime();
        Date birthDateAge20 =
                new GregorianCalendar(1996, Calendar.MAY, 1).getTime();
        Calendar age20plusLessThan6Month =
                new GregorianCalendar(1996, Calendar.MAY, 1);
        age20plusLessThan6Month.add(Calendar.MONTH, -5);
        Date birthDateAge20plusLessThan6Month =
                age20plusLessThan6Month.getTime();

        Calendar age20plusMoreThan6Month =
                new GregorianCalendar(1996, Calendar.MAY, 1);
        age20plusMoreThan6Month.add(Calendar.MONTH, -7);
        Date birthDateAge20plusMoreThan6Month =
                age20plusMoreThan6Month.getTime();

        // Assertions
        Assert.assertEquals(new BigDecimal("800"),
                amountService.grantableAmount(
                        diagnose, deliveryDate,
                        birthDateAge16plusMoreThan6Month));
        Assert.assertEquals(new BigDecimal("800"),
                amountService.grantableAmount(
                        diagnose, deliveryDate, birthDateAge17));
        Assert.assertEquals(new BigDecimal("800"),
                amountService.grantableAmount(
                        diagnose, deliveryDate, birthDateAge20));
        Assert.assertEquals(new BigDecimal("800"),
                amountService.grantableAmount(
                        diagnose, deliveryDate,
                        birthDateAge20plusLessThan6Month));
        Assert.assertEquals(new BigDecimal("0"),
                amountService.grantableAmount(
                        diagnose, deliveryDate,
                        birthDateAge20plusMoreThan6Month));
    }

    @Test
    public void children0to19post20160620() {
        Diagnose diagnose = new None();

        // Delivery date should be before 2016-03-01
        Date deliveryDate =
                new GregorianCalendar(2016, Calendar.JUNE, 21).getTime();

        // Tests cases
        Date birthDateAge1 =
                new GregorianCalendar(2015, Calendar.JUNE, 21).getTime();
        Date birthDateAge4 =
                new GregorianCalendar(2012, Calendar.JUNE, 21).getTime();
        Date birthDateAge15 =
                new GregorianCalendar(2001, Calendar.JUNE, 21).getTime();

        Calendar age16plusLessThan6Month =
                new GregorianCalendar(2000, Calendar.JUNE, 21);
        age16plusLessThan6Month.add(Calendar.MONTH, -5);
        Date birthDateAge16plusLessThan6Month =
                age16plusLessThan6Month.getTime();

        Calendar age16plusExact6Month =
                new GregorianCalendar(2000, Calendar.JUNE, 21);
        age16plusExact6Month.add(Calendar.MONTH, -6);
        Date birthDateAge16plusExact6Month = age16plusExact6Month.getTime();

        Calendar age16plusMoreThan6Month =
                new GregorianCalendar(2000, Calendar.JUNE, 21);
        age16plusMoreThan6Month.add(Calendar.MONTH, -7);
        Date birthDateAge16plusMoreThan6Month =
                age16plusMoreThan6Month.getTime();

        Date birthDateAge17 =
                new GregorianCalendar(1999, Calendar.JUNE, 21).getTime();
        Date birthDateAge20 =
                new GregorianCalendar(1996, Calendar.JUNE, 21).getTime();
        Calendar age20plusLessThan6Month =
                new GregorianCalendar(1996, Calendar.JUNE, 21);
        age20plusLessThan6Month.add(Calendar.MONTH, -5);
        Date birthDateAge20plusLessThan6Month =
                age20plusLessThan6Month.getTime();

        Calendar age20plusMoreThan6Month =
                new GregorianCalendar(1996, Calendar.JUNE, 21);
        age20plusMoreThan6Month.add(Calendar.MONTH, -7);
        Date birthDateAge20plusMoreThan6Month =
                age20plusMoreThan6Month.getTime();


        // Assertions
        Assert.assertEquals(new BigDecimal("1600"),
                amountService.grantableAmount(
                        diagnose, deliveryDate, birthDateAge1));
        Assert.assertEquals(new BigDecimal("1600"),
                amountService.grantableAmount(
                        diagnose, deliveryDate, birthDateAge4));
        Assert.assertEquals(new BigDecimal("1600"),
                amountService.grantableAmount(
                        diagnose, deliveryDate, birthDateAge15));
        Assert.assertEquals(new BigDecimal("1600"),
                amountService.grantableAmount(
                        diagnose, deliveryDate,
                        birthDateAge16plusLessThan6Month));
        Assert.assertEquals(new BigDecimal("1600"),
                amountService.grantableAmount(
                        diagnose, deliveryDate,
                        birthDateAge16plusExact6Month));
        Assert.assertEquals(new BigDecimal("1600"),
                amountService.grantableAmount(
                        diagnose, deliveryDate,
                        birthDateAge16plusMoreThan6Month));
        Assert.assertEquals(new BigDecimal("1600"),
                amountService.grantableAmount(
                        diagnose, deliveryDate, birthDateAge17));
        Assert.assertEquals(new BigDecimal("1600"),
                amountService.grantableAmount(
                        diagnose, deliveryDate, birthDateAge20));
        Assert.assertEquals(new BigDecimal("1600"),
                amountService.grantableAmount(
                        diagnose, deliveryDate,
                        birthDateAge20plusLessThan6Month));
        Assert.assertEquals(new BigDecimal("0"),
                amountService.grantableAmount(
                        diagnose, deliveryDate,
                        birthDateAge20plusMoreThan6Month));
    }
}
