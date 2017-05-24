package se.vgregion.service.glasogonbidrag.local;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.vgregion.portal.glasogonbidrag.domain.VisualLaterality;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Beneficiary;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Diagnose;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Grant;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Prescription;
import se.vgregion.portal.glasogonbidrag.domain.jpa.diagnose.Aphakia;
import se.vgregion.portal.glasogonbidrag.domain.jpa.diagnose.Keratoconus;
import se.vgregion.portal.glasogonbidrag.domain.jpa.diagnose.None;
import se.vgregion.portal.glasogonbidrag.domain.jpa.diagnose.Special;
import se.vgregion.service.glasogonbidrag.helper.BeneficiaryFactory;
import se.vgregion.service.glasogonbidrag.helper.GrantFactory;
import se.vgregion.service.glasogonbidrag.helper.PrescriptionFactory;
import se.vgregion.service.glasogonbidrag.local.api.GrantRuleValidationService;
import se.vgregion.service.glasogonbidrag.types.GrantRuleResult;
import se.vgregion.service.glasogonbidrag.types.GrantRuleViolation;
import se.vgregion.service.glasogonbidrag.types.GrantRuleWarning;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import static java.util.Calendar.JANUARY;
import static java.util.Calendar.FEBRUARY;
import static java.util.Calendar.MARCH;
import static java.util.Calendar.APRIL;
import static java.util.Calendar.MAY;
import static java.util.Calendar.JUNE;
import static java.util.Calendar.JULY;
import static java.util.Calendar.AUGUST;
import static java.util.Calendar.OCTOBER;
import static java.util.Calendar.DECEMBER;
import static java.util.Calendar.SEPTEMBER;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:localTestContext.xml")
public class GrantRuleValidationServiceTest {

    @Autowired
    private GrantRuleValidationService validationService;

    /**
     * The service should be handled by spring, this should ensures that
     * the service is auto wired by spring or other IoC framework.
     */
    @Test
    public void serviceShouldExists() {
        Assert.assertNotNull(validationService);
    }

    /**
     * The service should produce a warning if a beneficiary have multiple
     * grants for one prescription over one calendar.
     */
    @Test
    public void warnIfBeneficiaryHaveMoreThanOneGrantOnOnePrescription() {
        Set<Grant> historicalGrants = new HashSet<>();

        Beneficiary b = BeneficiaryFactory.newBeneficiary("Jens", "Blaut")
                .identification("199904031839")
                .build();

        Prescription p = PrescriptionFactory.newPrescription()
                .diagnose(new Aphakia(VisualLaterality.BILATERAL))
                .comment("Problem på båda ögonen.")
                .prescriber("Yngve Johansson")
                .date(2016, MARCH, 6)
                .build();

        Grant firstGrant = GrantFactory.newGrant()
                .delivery(2016, APRIL, 1)
                .amount(new BigDecimal("1000"))
                .area("14", "61")
                .beneficiary(b)
                .prescription(p)
                .build();


        GrantRuleResult result1 = validationService.test(
                firstGrant, historicalGrants);
        historicalGrants.add(firstGrant);

        Assert.assertFalse(result1.hasWarnings());
        Assert.assertFalse(result1.hasViolations());

        Grant secondGrant = GrantFactory.newGrant()
                .delivery(2016, APRIL, 12)
                .amount(new BigDecimal("1000"))
                .area("14", "61")
                .beneficiary(b)
                .prescription(p)
                .build();

        GrantRuleResult result2 = validationService.test(
                secondGrant, historicalGrants);

        Assert.assertTrue(result2.hasWarnings());
        Assert.assertFalse(result2.hasViolations());
        Assert.assertEquals(1, result2.warnings());
        Assert.assertTrue(result2.getWarnings().contains(
                new GrantRuleWarning("warning-more-than-one-grant")));
    }

    /**
     * A beneficiary should only be able to get a grant if the person
     * concerned do live in the Västra Götalands region.
     *
     * The grant object should contain this data "county" and "municipality"
     * of where the person concerned lived at time of delivery date or
     * prescription date depending on the diagnose.
     */
    @Test
    public void violationIfNotInVGR() {
        Grant grant1 = GrantFactory.newGrant()
                .delivery(2016, MAY, 4)
                .amount(new BigDecimal("1000"))
                .area("17", "30")
                .beneficiary()
                    .fullName("Teodore Qvist")
                    .identification("200204304570")
                .end()
                .prescription()
                    .comment("")
                    .date(2016, JANUARY, 3)
                    .diagnose(new None())
                    .prescriber("")
                .end()
                .build();

        GrantRuleResult result1 = validationService.test(
                grant1, new HashSet<Grant>());

        Assert.assertFalse(result1.hasWarnings());
        Assert.assertTrue(result1.hasViolations());
        Assert.assertEquals(1, result1.violations());
        Assert.assertTrue(result1.getViolations().contains(
                new GrantRuleViolation("violation-not-in-vgr")));

        // Problem when a beneficiary does live in "Lilla Edet".
        // This will test to validate that lilla edet works.
        Grant grant2 = GrantFactory.newGrant()
                .delivery(2016, MAY, 4)
                .amount(new BigDecimal("1000"))
                .area("14", "62")
                .beneficiary()
                    .fullName("Teodore Qvist")
                    .identification("200204304570")
                .end()
                .prescription()
                    .comment("")
                    .date(2016, JANUARY, 3)
                    .diagnose(new None())
                    .prescriber("")
                .end()
                .build();

        GrantRuleResult result2 = validationService.test(
                grant2, new HashSet<Grant>());

        Assert.assertFalse(result2.hasWarnings());
        Assert.assertFalse(result2.hasViolations());
    }

    /**
     * One must receive their grant (based on delivery date) within a year
     * of their recipe date.
     */
    @Test
    public void deliveryDateIs12MonthsAfterRecipeDate() {
        Grant grant = GrantFactory.newGrant()
                .delivery(2016, MAY, 4)
                .amount(new BigDecimal("800"))
                .area("14", "73")
                .beneficiary()
                    .fullName("Anna Lönnkrona")
                    .identification("200408123149")
                .end()
                .prescription()
                    .comment("")
                    .date(2015, JANUARY, 3)
                    .diagnose(new None())
                    .prescriber("")
                .end()
                .build();

        GrantRuleResult result = validationService.test(
                grant, new HashSet<Grant>());

        Assert.assertFalse(result.hasWarnings());
        Assert.assertTrue(result.hasViolations());
        Assert.assertEquals(1, result.violations());
        Assert.assertTrue(result.getViolations().contains(
                new GrantRuleViolation("violation-" +
                        "delivery-date-is-12-month-after-recipe-date")));
    }

    /**
     * To be illegible to receive a grant one where needed to have a valid
     * prescription that where issued before their 16th birthday if this
     * occurred before 2016-03-01.
     */
    @Test
    public void recipeDateIssuedAfter16thBirthday() {
        Grant grantOver16Pre20160301 = GrantFactory.newGrant()
                .delivery(2014, APRIL, 17)
                .amount(new BigDecimal("800"))
                .area("14", "89")
                .beneficiary()
                    .fullName("Fanny Bertilsson")
                    .identification("199802247487")
                .end()
                .prescription()
                    .comment("")
                    .date(2014, MARCH, 3)
                    .diagnose(new None())
                    .prescriber("")
                .end()
                .build();

        Grant grantOver16Post20160301 = GrantFactory.newGrant()
                .delivery(2017, DECEMBER, 17)
                .amount(new BigDecimal("800"))
                .area("14", "47")
                .beneficiary()
                    .fullName("Jenna Rollani")
                    .identification("200106195381")
                .end()
                .prescription()
                    .comment("")
                    .date(2017, DECEMBER, 6)
                    .diagnose(new None())
                    .prescriber("")
                .end()
                .build();

        GrantRuleResult resultPre20160301 = validationService.test(
                grantOver16Pre20160301, new HashSet<Grant>());

        GrantRuleResult resultPost20160301 = validationService.test(
                grantOver16Post20160301, new HashSet<Grant>());

        Assert.assertFalse(resultPre20160301.hasWarnings());
        Assert.assertTrue(resultPre20160301.hasViolations());
        Assert.assertEquals(1, resultPre20160301.violations());
        Assert.assertTrue(resultPre20160301.getViolations().contains(
                new GrantRuleViolation("violation-" +
                        "recipe-date-after-age-16-pre-20160301")));

        Assert.assertFalse(resultPost20160301.hasWarnings());
        Assert.assertFalse(resultPost20160301.hasViolations());
    }

    /**
     * To be illegible to receive a grant one where needed to have a valid
     * prescription that where issued before their 20th birthday if this
     * occurred after 2016-03-01.
     */
    @Test
    public void recipeDateIssuedAfter20thBirthday() {
        Grant grantUnder20Year = GrantFactory.newGrant()
                .delivery(2016, MAY, 3)
                .amount(new BigDecimal("800"))
                .area("14", "84")
                .beneficiary()
                    .fullName("Alban Fredriksson")
                    .identification("199808263231")
                .end()
                .prescription()
                    .comment("")
                    .date(2016, APRIL, 23)
                    .diagnose(new None())
                    .prescriber("")
                .end()
                .build();

        Grant grantOver20Year = GrantFactory.newGrant()
                .delivery(2016, APRIL, 21)
                .amount(new BigDecimal("800"))
                .area("14", "35")
                .beneficiary()
                    .fullName("Tore Jorgal")
                    .identification("199601181416")
                .end()
                .prescription()
                    .comment("")
                    .date(2016, FEBRUARY, 4)
                    .diagnose(new None())
                    .prescriber("")
                .end()
                .build();

        GrantRuleResult resultUnder20Year = validationService.test(
                grantUnder20Year, new HashSet<Grant>());

        GrantRuleResult resultOver20Year = validationService.test(
                grantOver20Year, new HashSet<Grant>());

        Assert.assertFalse(resultUnder20Year.hasViolations());
        Assert.assertFalse(resultUnder20Year.hasWarnings());
        Assert.assertTrue(resultOver20Year.hasViolations());
        Assert.assertFalse(resultOver20Year.hasWarnings());
        Assert.assertEquals(1, resultOver20Year.violations());
        Assert.assertTrue(resultOver20Year.getViolations().contains(
                new GrantRuleViolation("violation-" +
                        "recipe-date-after-age-20-post-20160301")));
    }

    /**
     * For the rules before 2016-03-01 one could receive their grant (based
     * on delivery date) at most 6 month after their 16th birthday.
     */
    @Test
    public void deliveryDate6MonthAfter16thBirthday() {
        Grant over16deliveryDatePast6Month = GrantFactory.newGrant()
                .delivery(2013, AUGUST, 19)
                .amount(new BigDecimal("800"))
                .area("14", "15")
                .beneficiary()
                    .fullName("Seigurth Hammar")
                    .identification("199702031416")
                .end()
                .prescription()
                    .comment("")
                    .date(2013, JANUARY, 26)
                    .diagnose(new None())
                    .prescriber("")
                .end()
                .build();

        GrantRuleResult past6MonthResult = validationService.test(
                over16deliveryDatePast6Month, new HashSet<Grant>());

        Assert.assertFalse(past6MonthResult.hasWarnings());
        Assert.assertTrue(past6MonthResult.hasViolations());
        Assert.assertEquals(1, past6MonthResult.violations());
        Assert.assertTrue(past6MonthResult.getViolations().contains(
                new GrantRuleViolation("violation-" +
                        "delivery-date-6-month-after-age-16-pre-20160301")));
    }

    /**
     * For the rules after 2016-03-01 one could receive their grant (based
     * on delivery date) at most 6 month after their 20th birthday.
     */
    @Test
    public void deliveryDate6MonthAfter20thBirthday() {
        Grant over20deliveryDatePast6Month = GrantFactory.newGrant()
                .delivery(2017, OCTOBER, 3)
                .amount(new BigDecimal("800"))
                .area("14", "87")
                .beneficiary()
                    .fullName("Leif Trondheim")
                    .identification("199703015215")
                .end()
                .prescription()
                    .comment("")
                    .date(2016, DECEMBER, 13)
                    .diagnose(new None())
                    .prescriber("")
                .end()
                .build();

        GrantRuleResult past6MonthResult = validationService.test(
                over20deliveryDatePast6Month, new HashSet<Grant>());

        Assert.assertFalse(past6MonthResult.hasWarnings());
        Assert.assertTrue(past6MonthResult.hasViolations());
        Assert.assertEquals(1, past6MonthResult.violations());
        Assert.assertTrue(past6MonthResult.getViolations().contains(
                new GrantRuleViolation("violation-" +
                        "delivery-date-6-month-after-age-20-post-20160301")));
    }

    /**
     * Children 0 to 15 may at most receive 1000 SEK for one prescription in
     * one calendar, pre 2016-06-20
     */
    @Test
    public void children0To15Max1000kr() {
        // Testing when a beneficiary of age 13 receives more than 1000 SEK.
        // This should produce no warnings, and one violation since the amount
        // exceeds 1000 SEK.

        Grant child13AboveAmount = GrantFactory.newGrant()
                .delivery(2016, JUNE, 3)
                .amount(new BigDecimal("1200"))
                .area("14", "95")
                .beneficiary()
                    .fullName("Olivia Van Karsteen")
                    .identification("200208114652")
                .end()
                .prescription()
                    .comment("")
                    .date(2016, MAY, 18)
                    .diagnose(new None())
                    .prescriber("")
                .end()
                .build();

        GrantRuleResult result1 = validationService.test(
                child13AboveAmount, new HashSet<Grant>());
        Assert.assertFalse(result1.hasWarnings());
        Assert.assertTrue(result1.hasViolations());
        Assert.assertEquals(1, result1.violations());
        Assert.assertTrue(result1.getViolations().contains(
                new GrantRuleViolation("violation-" +
                        "amount-greater-than-1000-for-children-" +
                        "0-to-15-pre-20160620")));


        // Testing when a beneficiary of age 13 receives more than 1000 SEK
        // one two separate grants.
        // This should produce one warning because the beneficiary have
        // multiple grants for one prescription and calendar, and one
        // violation since the amount exceeds 1000 SEK.

        Beneficiary beneficiary = BeneficiaryFactory.newBeneficiary()
                .fullName("Olivia Van Karsteen")
                .identification("200208114652")
                .build();

        Prescription prescription = PrescriptionFactory.newPrescription()
                .comment("")
                .date(2016, MAY, 18)
                .diagnose(new None())
                .prescriber("")
                .build();

        Grant child13AboveAmountMultipleFirst = GrantFactory.newGrant()
                .delivery(2016, JUNE, 3)
                .amount(new BigDecimal("800"))
                .area("14", "95")
                .beneficiary(beneficiary)
                .prescription(prescription)
                .build();
        Grant child13AboveAmountMultipleSecond = GrantFactory.newGrant()
                .delivery(2016, JUNE, 19)
                .amount(new BigDecimal("300"))
                .area("14", "95")
                .beneficiary(beneficiary)
                .prescription(prescription)
                .build();

        GrantRuleResult result2 = validationService.test(
                child13AboveAmountMultipleSecond,
                new HashSet<>(Arrays.asList(
                        child13AboveAmountMultipleFirst)));
        Assert.assertTrue(result2.hasWarnings());
        Assert.assertTrue(result2.hasViolations());
        Assert.assertEquals(1, result2.violations());
        Assert.assertTrue(result2.getViolations().contains(
                new GrantRuleViolation("violation-" +
                        "amount-greater-than-1000-for-children-" +
                        "0-to-15-pre-20160620")));


        // Testing when a beneficiary of age 13 receives exactly 1000 SEK.
        // This should produce no warnings and no violations.

        Grant child13ExactAmount = GrantFactory.newGrant()
                .delivery(2016, JUNE, 3)
                .amount(new BigDecimal("1000"))
                .area("14", "95")
                .beneficiary()
                    .fullName("Olivia Van Karsteen")
                    .identification("200208114652")
                .end()
                .prescription()
                    .comment("")
                    .date(2016, MAY, 18)
                    .diagnose(new None())
                    .prescriber("")
                .end()
                .build();

        GrantRuleResult result3 = validationService.test(
                child13ExactAmount, new HashSet<Grant>());
        Assert.assertFalse(result3.hasWarnings());
        Assert.assertFalse(result3.hasViolations());


        // Testing when a beneficiary of age 13 receives under 1000 SEK.
        // This should produce no warnings and no violations.

        Grant child13UnderAmount = GrantFactory.newGrant()
                .delivery(2016, JUNE, 3)
                .amount(new BigDecimal("500"))
                .area("14", "95")
                .beneficiary()
                    .fullName("Olivia Van Karsteen")
                    .identification("200208114660")
                .end()
                .prescription()
                    .comment("")
                    .date(2016, MAY, 18)
                    .diagnose(new None())
                    .prescriber("")
                .end()
                .build();

        GrantRuleResult result4 = validationService.test(
                child13UnderAmount, new HashSet<Grant>());
        Assert.assertFalse(result4.hasWarnings());
        Assert.assertFalse(result4.hasViolations());
    }

    /**
     * Children 16 to 19 may at most receive 800 SEK for one prescription in
     * one calendar, between 2016-03-01 and 2016-06-20.
     */
    @Test
    public void children16To19Max800kr() {
        // Testing when a beneficiary of age 18 receives more than 800 SEK.
        // This should produce no warnings, and one violation since the amount
        // exceeds 800 SEK.

        Grant child18AboveAmount = GrantFactory.newGrant()
               .delivery(2016, JUNE, 3)
                .amount(new BigDecimal("1000"))
                .area("14", "96")
                .beneficiary()
                    .fullName("Valdemar Mur")
                    .identification("199804103431")
                .end()
                .prescription()
                    .comment("")
                    .date(2016, MAY, 18)
                    .diagnose(new None())
                    .prescriber("")
                .end()
                .build();

        GrantRuleResult result1 = validationService.test(
                child18AboveAmount, new HashSet<Grant>());
        Assert.assertFalse(result1.hasWarnings());
        Assert.assertTrue(result1.hasViolations());
        Assert.assertEquals(1, result1.violations());
        Assert.assertTrue(result1.getViolations().contains(
                new GrantRuleViolation("violation-" +
                        "amount-greater-than-800-for-children-16-to-19-" +
                        "post-20160301-and-pre-20160620")));


        // Testing when a beneficiary of age 18 receives more than 800 SEK
        // one two separate grants.
        // This should produce one warning because the beneficiary have
        // multiple grants for one prescription and calendar, and one
        // violation since the amount exceeds 800 SEK.

        Beneficiary beneficiary = BeneficiaryFactory.newBeneficiary()
                .fullName("Valdemar Mur")
                .identification("199804103431")
                .build();

        Prescription prescription = PrescriptionFactory.newPrescription()
                .comment("")
                .date(2016, MAY, 18)
                .diagnose(new None())
                .prescriber("")
                .build();

        Grant child18AboveAmountMultipleFirst = GrantFactory.newGrant()
                .delivery(2016, JUNE, 3)
                .amount(new BigDecimal("800"))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(prescription)
                .build();
        Grant child18AboveAmountMultipleSecond = GrantFactory.newGrant()
                .delivery(2016, JUNE, 18)
                .amount(new BigDecimal("300"))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(prescription)
                .build();

        GrantRuleResult result2 = validationService.test(
                child18AboveAmountMultipleSecond,
                new HashSet<>(Arrays.asList(
                        child18AboveAmountMultipleFirst)));
        Assert.assertTrue(result2.hasWarnings());
        Assert.assertTrue(result2.hasViolations());
        Assert.assertEquals(1, result2.violations());
        Assert.assertTrue(result2.getViolations().contains(
                new GrantRuleViolation("violation-" +
                        "amount-greater-than-800-for-children-16-to-19-" +
                        "post-20160301-and-pre-20160620")));


        // Testing when a beneficiary of age 18 receives exactly 800 SEK.
        // This should produce no warnings and no violations.

        Grant child18ExactAmount = GrantFactory.newGrant()
                .delivery(2016, JUNE, 3)
                .amount(new BigDecimal("800"))
                .area("14", "96")
                .beneficiary()
                    .fullName("Valdemar Mur")
                    .identification("199804103431")
                .end()
                .prescription()
                    .comment("")
                    .date(2016, MAY, 18)
                    .diagnose(new None())
                    .prescriber("")
                .end()
                .build();

        GrantRuleResult result3 = validationService.test(
                child18ExactAmount, new HashSet<Grant>());
        Assert.assertFalse(result3.hasWarnings());
        Assert.assertFalse(result3.hasViolations());


        // Testing when a beneficiary of age 18 receives under 800 SEK.
        // This should produce no warnings and no violations.

        Grant child18UnderAmount = GrantFactory.newGrant()
                .delivery(2016, JUNE, 3)
                .amount(new BigDecimal("600"))
                .area("14", "96")
                .beneficiary()
                    .fullName("Valdemar Mur")
                    .identification("199804103431")
                .end()
                .prescription()
                    .comment("")
                    .date(2016, MAY, 18)
                    .diagnose(new None())
                    .prescriber("")
                .end()
                .build();

        GrantRuleResult result4 = validationService.test(
                child18UnderAmount, new HashSet<Grant>());
        Assert.assertFalse(result4.hasWarnings());
        Assert.assertFalse(result4.hasViolations());
    }

    /**
     * Children 0 to 19 may at most receive 1600 SEK for one prescription in
     * one calendar, after 2016-06-20.
     */
    @Test
    public void children0To19Max1600krPost20160620() {
        // Testing when a beneficiary of age 16 receives more than 1600 SEK.
        // This should produce no warnings, and one violation since the amount
        // exceeds 1600 SEK.

        Grant child16AboveAmount = GrantFactory.newGrant()
                .delivery(2016, AUGUST, 3)
                .amount(new BigDecimal("2000"))
                .area("14", "96")
                .beneficiary()
                    .fullName("Sven Larsson")
                    .identification("200001013431")
                .end()
                .prescription()
                    .comment("")
                    .date(2016, JULY, 18)
                    .diagnose(new None())
                    .prescriber("")
                .end()
                .build();

        GrantRuleResult result1 = validationService.test(
                child16AboveAmount, new HashSet<Grant>());
        Assert.assertFalse(result1.hasWarnings());
        Assert.assertTrue(result1.hasViolations());
        Assert.assertEquals(1, result1.violations());
        Assert.assertTrue(result1.getViolations().contains(
                new GrantRuleViolation("violation-" +
                        "amount-greater-than-1600-for-children-0-to-19-" +
                        "post-20160620")));


        // Testing when a beneficiary of age 16 receives more than 1600 SEK
        // one two separate grants.
        // This should produce one warning because the beneficiary have
        // multiple grants for one prescription and calendar, and one
        // violation since the amount exceeds 1600 SEK.

        Beneficiary beneficiary = BeneficiaryFactory.newBeneficiary()
                .fullName("Sven Larsson")
                .identification("200001013431")
                .build();

        Prescription prescription = PrescriptionFactory.newPrescription()
                .comment("")
                .date(2016, JULY, 18)
                .diagnose(new None())
                .prescriber("")
                .build();

        Grant child16AboveAmountMultipleFirst = GrantFactory.newGrant()
                .delivery(2016, AUGUST, 3)
                .amount(new BigDecimal("800"))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(prescription)
                .build();
        Grant child16AboveAmountMultipleSecond = GrantFactory.newGrant()
                .delivery(2016, AUGUST, 13)
                .amount(new BigDecimal("900"))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(prescription)
                .build();

        GrantRuleResult result2 = validationService.test(
                child16AboveAmountMultipleSecond,
                new HashSet<>(Arrays.asList(
                        child16AboveAmountMultipleFirst)));
        Assert.assertTrue(result2.hasWarnings());
        Assert.assertTrue(result2.hasViolations());
        Assert.assertEquals(1, result2.violations());
        Assert.assertTrue(result1.getViolations().contains(
                new GrantRuleViolation("violation-" +
                        "amount-greater-than-1600-for-children-0-to-19-" +
                        "post-20160620")));


        // Testing when a beneficiary of age 18 receives exactly 800 SEK.
        // This should produce no warnings and no violations.

        Grant child16ExactAmount = GrantFactory.newGrant()
                .delivery(2016, AUGUST, 3)
                .amount(new BigDecimal("1600"))
                .area("14", "96")
                .beneficiary()
                    .fullName("Sven Larsson")
                    .identification("200001013431")
                .end()
                .prescription()
                    .comment("")
                    .date(2016, JULY, 18)
                    .diagnose(new None())
                    .prescriber("")
                .end()
                .build();

        GrantRuleResult result3 = validationService.test(
                child16ExactAmount, new HashSet<Grant>());
        Assert.assertFalse(result3.hasWarnings());
        Assert.assertFalse(result3.hasViolations());


        // Testing when a beneficiary of age 18 receives under 800 SEK.
        // This should produce no warnings and no violations.

        Grant child16UnderAmount = GrantFactory.newGrant()
                .delivery(2016, AUGUST, 3)
                .amount(new BigDecimal("1200"))
                .area("14", "96")
                .beneficiary()
                    .fullName("Sven Larsson")
                    .identification("200001013431")
                .end()
                .prescription()
                    .comment("")
                    .date(2016, JULY, 18)
                    .diagnose(new None())
                    .prescriber("")
                .end()
                .build();

        GrantRuleResult result4 = validationService.test(
                child16UnderAmount, new HashSet<Grant>());
        Assert.assertFalse(result4.hasWarnings());
        Assert.assertFalse(result4.hasViolations());
    }

    /**
     * Test so that the amounts grantable for keratoconus before
     * 2016-06-20 works.
     * Tests diagnose for left eye, right eye and both eyes.
     */
    @Test
    public void keratoconusPre20160620() {

        // A beneficiary to test with -----------------------------------------

        Beneficiary beneficiary = BeneficiaryFactory.newBeneficiary()
                .fullName("Goran Pontare")
                .identification("193912207143")
                .build();


        // One eye will max give 1200kr per grant -----------------------------

        Prescription leftEye = PrescriptionFactory.newPrescription()
                .comment("")
                .date(new GregorianCalendar(2016, MAY, 4).getTime())
                .diagnose(new Keratoconus(
                        VisualLaterality.LEFT, 0.0f, 0.2f, false))
                .prescriber("")
                .build();

        // Empty history set to keep track of previous grants.
        Set<Grant> historyLeftEye = new HashSet<>();

        // Grant of 1200, which should work
        Grant grant1LeftEyeOk = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2016, MAY, 19).getTime())
                .amount(new BigDecimal("1200"))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(leftEye)
                .build();

        GrantRuleResult result1LeftEyeOk =
                validationService.test(grant1LeftEyeOk, historyLeftEye);
        Assert.assertFalse(result1LeftEyeOk.hasWarnings());
        Assert.assertFalse(result1LeftEyeOk.hasViolations());

        // Add grant to history
        historyLeftEye.add(grant1LeftEyeOk);

        // Previous grant of 1200 and this of 400 is 1600, which is above 1200
        Grant grant1LeftEyeAbove = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2016, MAY, 20).getTime())
                .amount(new BigDecimal("400"))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(leftEye)
                .build();

        GrantRuleResult result1LeftEyeAbove =
                validationService.test(grant1LeftEyeAbove, historyLeftEye);
        Assert.assertTrue(result1LeftEyeAbove.hasWarnings());
        Assert.assertTrue(result1LeftEyeAbove.hasViolations());
        Assert.assertTrue(result1LeftEyeAbove.getViolations().contains(
                new GrantRuleViolation(
                        "violation-amount-greater-than-1200-" +
                                "for-keratoconus-left-eye-pre-20160620")));


        // Same for the right eye 1200kr. -------------------------------------

        Prescription rightEye = PrescriptionFactory.newPrescription()
                .comment("")
                .date(new GregorianCalendar(2016, MAY, 4).getTime())
                .diagnose(new Keratoconus(
                        VisualLaterality.RIGHT, 0.1f, 0.0f, false))
                .prescriber("")
                .build();

        Set<Grant> historyRightEye = new HashSet<>();

        // Grant of 1200, which should work
        Grant grant1RightEyeOk = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2016, MAY, 19).getTime())
                .amount(new BigDecimal("1200"))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(rightEye)
                .build();

        GrantRuleResult result1RightEyeOk =
                validationService.test(grant1RightEyeOk, historyRightEye);
        Assert.assertFalse(result1RightEyeOk.hasWarnings());
        Assert.assertFalse(result1RightEyeOk.hasViolations());

        // Add grant to history
        historyRightEye.add(grant1RightEyeOk);

        // Previous grant of 1200 and this of 300 is 1500, which is above 1200
        Grant grant1RightEyeAbove = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2016, MAY, 20).getTime())
                .amount(new BigDecimal("300"))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(rightEye)
                .build();

        GrantRuleResult result1RightEyeAbove =
                validationService.test(grant1RightEyeAbove, historyRightEye);
        Assert.assertTrue(result1RightEyeAbove.hasWarnings());
        Assert.assertTrue(result1RightEyeAbove.hasViolations());
        Assert.assertTrue(result1RightEyeAbove.getViolations().contains(
                new GrantRuleViolation(
                        "violation-amount-greater-than-1200-" +
                                "for-keratoconus-right-eye-pre-20160620")));


        // Grantable 1200kr per eye. ------------------------------------------

        Prescription bothEyes = PrescriptionFactory.newPrescription()
                .comment("")
                .date(new GregorianCalendar(2016, MAY, 4).getTime())
                .diagnose(new Keratoconus(
                        VisualLaterality.BILATERAL, 0.1f, 0.2f, false))
                .prescriber("")
                .build();

        // Empty history set to keep track of previous grants.
        Set<Grant> historyBothEyes = new HashSet<>();

        // Grant of 2400, which is okay (1200 for each eye)
        Grant grant1BothEyesOk = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2016, MAY, 19).getTime())
                .amount(new BigDecimal("2400"))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(bothEyes)
                .build();

        GrantRuleResult result1BothEyesOk = validationService.test(
                grant1BothEyesOk, historyBothEyes);
        Assert.assertFalse(result1BothEyesOk.hasWarnings());
        Assert.assertFalse(result1BothEyesOk.hasViolations());

        // Add grant to history
        historyBothEyes.add(grant1BothEyesOk);

        // Previous grant of 2400 and this of 100 is 2500 which is above 2500
        Grant grant1BothEyesMoreThan2400 = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2016, MAY, 19).getTime())
                .amount(new BigDecimal("100"))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(bothEyes)
                .build();

        GrantRuleResult result1BothEyesMoreThan2400 = validationService.test(
                grant1BothEyesMoreThan2400, historyBothEyes);
        Assert.assertTrue(result1BothEyesMoreThan2400.hasWarnings());
        Assert.assertTrue(result1BothEyesMoreThan2400.hasViolations());
        Assert.assertTrue(result1BothEyesMoreThan2400.getViolations().contains(
                new GrantRuleViolation(
                        "violation-amount-greater-than-2400-" +
                                "for-keratoconus-both-eyes-pre-20160620")));
    }

    /**
     * Test so that the changed amounts for keratoconus works after 2016-06-20
     * Tests diagnose for left eye, right eye and both eyes.
     */
    @Test
    public void keratoconusPost20160620() {

        // A Beneficiary to test with -----------------------------------------

        Beneficiary beneficiary = BeneficiaryFactory.newBeneficiary()
                .fullName("Test1 Test1")
                .identification("193912207143")
                .build();

        // One eye will max give 1500kr per grant -----------------------------

        Prescription leftEye = PrescriptionFactory.newPrescription()
                .comment("")
                .date(new GregorianCalendar(2016, AUGUST, 26).getTime())
                .diagnose(new Keratoconus(
                        VisualLaterality.LEFT, 0.0f, 0.2f, false))
                .prescriber("")
                .build();

        Set<Grant> historyLeftEye = new HashSet<>();

        // Grant of 1500 post 2016-06-20, which should work
        Grant grant1LeftEyeOk = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2016, SEPTEMBER, 10).getTime())
                .amount(new BigDecimal("1500"))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(leftEye)
                .build();

        GrantRuleResult result1LeftEyeOk =
                validationService.test(grant1LeftEyeOk, historyLeftEye);
        Assert.assertFalse(result1LeftEyeOk.hasWarnings());
        Assert.assertFalse(result1LeftEyeOk.hasViolations());

        // Add grant to history
        historyLeftEye.add(grant1LeftEyeOk);

        // Previous grant of 1500 and this 300 is 1800, which is above 1500
        Grant grant2LeftEyeAbove = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2016, SEPTEMBER, 12).getTime())
                .amount(new BigDecimal("300"))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(leftEye)
                .build();

        GrantRuleResult result2LeftEyeAbove =
                validationService.test(grant2LeftEyeAbove, historyLeftEye);
        Assert.assertTrue(result2LeftEyeAbove.hasWarnings());
        Assert.assertTrue(result2LeftEyeAbove.hasViolations());
        Assert.assertTrue(result2LeftEyeAbove.getViolations().contains(
                new GrantRuleViolation(
                        "violation-amount-greater-than-1500-" +
                                "for-keratoconus-left-eye-post-20160620")));

        // Same for the right eye 1500kr --------------------------------------

        Prescription rightEye = PrescriptionFactory.newPrescription()
                .comment("")
                .date(new GregorianCalendar(2016, AUGUST, 26).getTime())
                .diagnose(new Keratoconus(
                        VisualLaterality.RIGHT, 0.1f, 0.0f, false))
                .prescriber("")
                .build();

        Set<Grant> historyRightEye = new HashSet<>();

        // Grant of 1500 post 2016-06-20, which should work
        Grant grant1RightEyeOk = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2016, SEPTEMBER, 10).getTime())
                .amount(new BigDecimal("1500"))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(rightEye)
                .build();

        GrantRuleResult result1RightEyeOk =
                validationService.test(grant1RightEyeOk, historyRightEye);
        Assert.assertFalse(result1RightEyeOk.hasWarnings());
        Assert.assertFalse(result1RightEyeOk.hasViolations());

        // Add grant to history
        historyRightEye.add(grant1RightEyeOk);

        // Previous grant of 1500 and this of 600 is 2100, which is above 1500
        Grant grant2RightEyeAbove = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2016, SEPTEMBER, 12).getTime())
                .amount(new BigDecimal("600"))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(rightEye)
                .build();

        GrantRuleResult result2RightEyeAbove =
                validationService.test(grant2RightEyeAbove, historyRightEye);
        Assert.assertTrue(result2RightEyeAbove.hasWarnings());
        Assert.assertTrue(result2RightEyeAbove.hasViolations());
        Assert.assertTrue(result2RightEyeAbove.getViolations().contains(
                new GrantRuleViolation(
                        "violation-amount-greater-than-1500-" +
                                "for-keratoconus-right-eye-post-20160620")));


        // Grantable 1500kr per eye post 2016-06-20 ---------------------------

        Prescription bothEyes = PrescriptionFactory.newPrescription()
                .comment("")
                .date(new GregorianCalendar(2016, AUGUST, 26).getTime())
                .diagnose(new Keratoconus(
                        VisualLaterality.BILATERAL, 0.1f, 0.2f, false))
                .prescriber("")
                .build();

        // Empty history set to keep track of previous grants.
        Set<Grant> historyBothEyes = new HashSet<>();

        // Grant of 3000, which is okay (1500 for each eye)
        Grant grant1BothEyesOk = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2016, SEPTEMBER, 10).getTime())
                .amount(new BigDecimal("3000"))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(bothEyes)
                .build();

        GrantRuleResult result1BothEyesOk =
                validationService.test(grant1BothEyesOk, historyBothEyes);
        Assert.assertFalse(result1BothEyesOk.hasWarnings());
        Assert.assertFalse(result1BothEyesOk.hasViolations());

        // Add grant to history
        historyBothEyes.add(grant1BothEyesOk);

        // Previous grant of 3000 and this of 1000, which is above 3000
        Grant grant2BothEyesAbove = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2016, SEPTEMBER, 12).getTime())
                .amount(new BigDecimal("1000"))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(bothEyes)
                .build();

        GrantRuleResult result2BothEyesAbove =
                validationService.test(grant2BothEyesAbove, historyBothEyes);
        Assert.assertTrue(result2BothEyesAbove.hasWarnings());
        Assert.assertTrue(result2BothEyesAbove.hasViolations());
        Assert.assertTrue(result2BothEyesAbove.getViolations().contains(
                new GrantRuleViolation(
                        "violation-amount-greater-than-3000-" +
                                "for-keratoconus-both-eyes-post-20160620")));
    }

//    @Test
//    public void aphakiaMax2000kr() {
//        testCustomDiagnoseAndAmount(
//                new Aphakia(VisualLaterality.BILATERAL),
//                "196803012340",
//                new BigDecimal("2000"),
//                new GregorianCalendar(2010, MARCH, 10).getTime(),
//                new GregorianCalendar(2016, JANUARY, 9).getTime(),
//                new GregorianCalendar(2016, JANUARY, 14).getTime(),
//                "violation-amount-greater-than-2000-" +
//                        "for-aphakia-and-special-pre-20160620");
//    }
//
//    @Test
//    public void aphakiaMax2400krPost20160620() {
//        testCustomDiagnoseAndAmount(
//                new Aphakia(VisualLaterality.BILATERAL),
//                "196803012340",
//                new BigDecimal("2400"),
//                new GregorianCalendar(2010, MARCH, 10).getTime(),
//                new GregorianCalendar(2016, JULY, 9).getTime(),
//                new GregorianCalendar(2016, JULY, 14).getTime(),
//                "violation-amount-greater-than-2400-" +
//                        "for-aphakia-and-special-post-20160620");
//    }

    @Test
    public void specialMax2000kr() {
        testCustomDiagnoseAndAmount(
                new Special(VisualLaterality.BILATERAL, false),
                "197304194579",
                new BigDecimal("2000"),
                new GregorianCalendar(2010, MARCH, 10).getTime(),
                new GregorianCalendar(2016, JANUARY, 9).getTime(),
                new GregorianCalendar(2016, JANUARY, 14).getTime(),
                "violation-amount-greater-than-2000-" +
                        "for-aphakia-and-special-pre-20160620");
    }

    @Test
    public void specialMax2400krPost20160620() {
        testCustomDiagnoseAndAmount(
                new Special(VisualLaterality.BILATERAL, false),
                "197304194579",
                new BigDecimal("2400"),
                new GregorianCalendar(2010, MARCH, 10).getTime(),
                new GregorianCalendar(2016, JULY, 9).getTime(),
                new GregorianCalendar(2016, JULY, 14).getTime(),
                "violation-amount-greater-than-2400-" +
                        "for-aphakia-and-special-post-20160620");
    }

//    @Test
//    public void keratoconusMax2400kr() {
//        testCustomDiagnoseAndAmount(
//                new Keratoconus(VisualLaterality.LEFT, 0.0f, 0.3f, false),
//                "193912207143",
//                new BigDecimal("2400"),
//                new GregorianCalendar(2010, MARCH, 10).getTime(),
//                new GregorianCalendar(2016, JANUARY, 9).getTime(),
//                new GregorianCalendar(2016, JANUARY, 14).getTime(),
//                "violation-amount-greater-than-2400-" +
//                        "for-keratoconus-pre-20160620");
//    }
//
//    @Test
//    public void keratoconusMax3000krPost20160620() {
//        testCustomDiagnoseAndAmount(
//                new Keratoconus(VisualLaterality.LEFT, 0.0f, 0.3f, false),
//                "193912207143",
//                new BigDecimal("3000"),
//                new GregorianCalendar(2010, MARCH, 10).getTime(),
//                new GregorianCalendar(2016, JULY, 9).getTime(),
//                new GregorianCalendar(2016, JULY, 14).getTime(),
//                "violation-amount-greater-than-3000-" +
//                        "for-keratoconus-post-20160620");
//    }

    // Helper
    private void testCustomDiagnoseAndAmount(Diagnose diagnose,
                                            String identification,
                                            BigDecimal amount,
                                            Date prescriptionDate,
                                            Date deliveryDate1,
                                            Date deliveryDate2,
                                            String violation) {
        // Test when grant exceed amount.
        Grant grant1 = GrantFactory.newGrant()
                .delivery(deliveryDate1)
                .amount(amount.add(new BigDecimal("100")))
                .area("14", "96")
                .beneficiary()
                    .fullName("Test1 Test1")
                    .identification(identification)
                .end()
                .prescription()
                    .comment("")
                    .date(prescriptionDate)
                    .diagnose(diagnose)
                    .prescriber("")
                .end()
                .build();

        GrantRuleResult result1 = validationService.test(
                grant1, new HashSet<Grant>());
        Assert.assertFalse(result1.hasWarnings());
        Assert.assertTrue(result1.hasViolations());
        Assert.assertEquals(1, result1.violations());
        Assert.assertTrue(result1.getViolations().contains(
                new GrantRuleViolation(violation)));


        // Test when multiple grants exceed amount.
        Beneficiary beneficiary = BeneficiaryFactory.newBeneficiary()
                .fullName("Test2 Test2")
                .identification(identification)
                .build();

        Prescription prescription = PrescriptionFactory.newPrescription()
                .comment("")
                .date(prescriptionDate)
                .diagnose(diagnose)
                .prescriber("")
                .build();

        Grant grant2_1 = GrantFactory.newGrant()
                .delivery(deliveryDate1)
                .amount(amount.divide(new BigDecimal("2"))
                              .add(new BigDecimal("100")))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(prescription)
                .build();
        Grant grant2_2 = GrantFactory.newGrant()
                .delivery(deliveryDate2)
                .amount(amount.divide(new BigDecimal("2")))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(prescription)
                .build();

        GrantRuleResult result2 = validationService.test(
                grant2_2, new HashSet<>(Arrays.asList(grant2_1)));
        Assert.assertTrue(result2.hasWarnings());
        Assert.assertTrue(result2.hasViolations());
        Assert.assertEquals(1, result2.violations());
        Assert.assertTrue(result1.getViolations().contains(
                new GrantRuleViolation(violation)));

        // Grant's amount is exactly max amount
        Grant grant3 = GrantFactory.newGrant()
                .delivery(deliveryDate1)
                .amount(amount)
                .area("14", "96")
                .beneficiary()
                    .fullName("Test3 Test3")
                    .identification(identification)
                .end()
                .prescription()
                    .comment("")
                    .date(prescriptionDate)
                    .diagnose(diagnose)
                    .prescriber("")
                .end()
                .build();

        GrantRuleResult result3 = validationService.test(
                grant3, new HashSet<Grant>());
        Assert.assertFalse(result3.hasWarnings());
        Assert.assertFalse(result3.hasViolations());

        // Grant's amount is below max amount.
        Grant grant4 = GrantFactory.newGrant()
                .delivery(deliveryDate1)
                .amount(amount.subtract(new BigDecimal("200")))
                .area("14", "96")
                .beneficiary()
                    .fullName("Test4 Test4")
                    .identification(identification)
                .end()
                .prescription()
                    .comment("")
                    .date(prescriptionDate)
                    .diagnose(diagnose)
                    .prescriber("")
                .end()
                .build();

        GrantRuleResult result4 = validationService.test(
                grant4, new HashSet<Grant>());
        Assert.assertFalse(result4.hasWarnings());
        Assert.assertFalse(result4.hasViolations());
    }
}
