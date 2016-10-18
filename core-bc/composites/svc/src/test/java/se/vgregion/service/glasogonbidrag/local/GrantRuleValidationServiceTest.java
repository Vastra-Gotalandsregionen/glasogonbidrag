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
import java.util.Date;
import java.util.GregorianCalendar;

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
        Beneficiary b = BeneficiaryFactory.newBeneficiary()
                .firstName("Jens")
                .lastName("Blaut")
                .identification("199904031839")
                .build();

        Prescription p = PrescriptionFactory.newPrescription()
                .diagnose(new Aphakia(VisualLaterality.BILATERAL))
                .comment("Problem på båda ögonen.")
                .prescriber("Yngve Johansson")
                .date(2016, 3, 6)
                .build();

        Grant firstGrant = GrantFactory.newGrant()
                .delivery(2016, 4, 1)
                .amount(new BigDecimal("1000"))
                .area("14", "61")
                .beneficiary(b)
                .prescription(p)
                .build();

        GrantRuleResult result1 = validationService.test(
                firstGrant, new GregorianCalendar(2016, 5, 4).getTime());

        Assert.assertFalse(result1.hasWarnings());
        Assert.assertFalse(result1.hasViolations());

        Grant secondGrant = GrantFactory.newGrant()
                .delivery(2016, 4, 12)
                .amount(new BigDecimal("1000"))
                .area("14", "61")
                .beneficiary(b)
                .prescription(p)
                .build();

        GrantRuleResult result2 = validationService.test(
                secondGrant, new GregorianCalendar(2016, 5, 4).getTime());

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
        Grant grant = GrantFactory.newGrant()
                .delivery(2016, 5, 4)
                .amount(new BigDecimal("1000"))
                .area("17", "30")
                .beneficiary()
                    .firstName("Teodore")
                    .lastName("Qvist")
                    .identification("200204304570")
                .end()
                .prescription()
                    .comment("")
                    .date(2016, 1, 3)
                    .diagnose(new None())
                    .prescriber("")
                .end()
                .build();

        GrantRuleResult result = validationService.test(
                grant, new GregorianCalendar(2016, 5, 6).getTime());

        Assert.assertFalse(result.hasWarnings());
        Assert.assertTrue(result.hasViolations());
        Assert.assertEquals(1, result.violations());
        Assert.assertTrue(result.getViolations().contains(
                new GrantRuleViolation("violation-not-in-vgr")));
    }

    /**
     * One must receive their grant (based on delivery date) within a year
     * of their recipe date.
     */
    @Test
    public void deliveryDateIs12MonthsAfterRecipeDate() {
        Grant grant = GrantFactory.newGrant()
                .delivery(2016, 5, 4)
                .amount(new BigDecimal("800"))
                .area("14", "73")
                .beneficiary()
                    .firstName("Anna")
                    .lastName("Lönnkrona")
                    .identification("200408123149")
                .end()
                .prescription()
                    .comment("")
                    .date(2015, 1, 3)
                    .diagnose(new None())
                    .prescriber("")
                .end()
                .build();

        GrantRuleResult result = validationService.test(
                grant, new GregorianCalendar(2016, 5, 6).getTime());

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
                .delivery(2014, 4, 17)
                .amount(new BigDecimal("800"))
                .area("14", "89")
                .beneficiary()
                    .firstName("Fanny")
                    .lastName("Bertilsson")
                    .identification("199802247487")
                .end()
                .prescription()
                    .comment("")
                    .date(2014, 3, 3)
                    .diagnose(new None())
                    .prescriber("")
                .end()
                .build();

        Grant grantOver16Post20160301 = GrantFactory.newGrant()
                .delivery(2017, 12, 17)
                .amount(new BigDecimal("800"))
                .area("14", "47")
                .beneficiary()
                    .firstName("Jenna")
                    .lastName("Rollani")
                    .identification("200106195381")
                .end()
                .prescription()
                    .comment("")
                    .date(2017, 12, 6)
                    .diagnose(new None())
                    .prescriber("")
                .end()
                .build();

        GrantRuleResult resultPre20160301 = validationService.test(
                grantOver16Pre20160301,
                new GregorianCalendar(2014, 7, 15).getTime());
        GrantRuleResult resultPost20160301 = validationService.test(
                grantOver16Post20160301,
                new GregorianCalendar(2018, 1, 6).getTime());

        Assert.assertFalse(resultPre20160301.hasWarnings());
        Assert.assertTrue(resultPre20160301.hasViolations());
        Assert.assertEquals(1, resultPre20160301.violations());
        Assert.assertTrue(resultPre20160301.getViolations().contains(
                new GrantRuleViolation("violation-" +
                        "recipe-date-after-age-16-pre-20160301")));

        Assert.assertFalse(resultPost20160301.hasWarnings());
        Assert.assertFalse(resultPost20160301.hasViolations());
        Assert.assertEquals(1, resultPost20160301.violations());
    }

    /**
     * To be illegible to receive a grant one where needed to have a valid
     * prescription that where issued before their 20th birthday if this
     * occurred after 2016-03-01.
     */
    @Test
    public void recipeDateIssuedAfter20thBirthday() {
        Grant grantUnder20Year = GrantFactory.newGrant()
                .delivery(2016, 5, 3)
                .amount(new BigDecimal("800"))
                .area("14", "84")
                .beneficiary()
                    .firstName("Alban")
                    .lastName("Fredriksson")
                    .identification("199808263231")
                .end()
                .prescription()
                    .comment("")
                    .date(2016, 4, 23)
                    .diagnose(new None())
                    .prescriber("")
                .end()
                .build();

        Grant grantOver20Year = GrantFactory.newGrant()
                .delivery(2016, 4, 21)
                .amount(new BigDecimal("800"))
                .area("14", "35")
                .beneficiary()
                    .firstName("Tore")
                    .lastName("Jorgal")
                    .identification("199601181416")
                .end()
                .prescription()
                    .comment("")
                    .date(2016, 2, 4)
                    .diagnose(new None())
                    .prescriber("")
                .end()
                .build();


        GrantRuleResult resultUnder20Year = validationService.test(
                grantUnder20Year,
                new GregorianCalendar(2016, 5, 31).getTime());
        GrantRuleResult resultOver20Year = validationService.test(
                grantOver20Year, new GregorianCalendar(2016, 5, 6).getTime());

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
                .delivery(2013, 8, 19)
                .amount(new BigDecimal("800"))
                .area("14", "15")
                .beneficiary()
                    .firstName("Seigurth")
                    .lastName("Hammar")
                    .identification("199702031416")
                .end()
                .prescription()
                    .comment("")
                    .date(2013, 1, 26)
                    .diagnose(new None())
                    .prescriber("")
                .end()
                .build();

        GrantRuleResult past6MonthResult = validationService.test(
                over16deliveryDatePast6Month,
                new GregorianCalendar(2013, 12, 16).getTime());
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
                .delivery(2017, 10, 3)
                .amount(new BigDecimal("800"))
                .area("14", "87")
                .beneficiary()
                    .firstName("Leif")
                    .lastName("Trondheim")
                    .identification("199703015215")
                .end()
                .prescription()
                    .comment("")
                    .date(2016, 12, 13)
                    .diagnose(new None())
                    .prescriber("")
                .end()
                .build();

        GrantRuleResult past6MonthResult = validationService.test(
                over20deliveryDatePast6Month,
                new GregorianCalendar(2015, 2, 1).getTime());
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
        Date testDate = new GregorianCalendar(2016, 7, 4).getTime();

        // Testing when a beneficiary of age 13 receives more than 1000 SEK.
        // This should produce no warnings, and one violation since the amount
        // exceeds 1000 SEK.

        Grant child13AboveAmount = GrantFactory.newGrant()
                .delivery(2016, 6, 3)
                .amount(new BigDecimal("1200"))
                .area("14", "95")
                .beneficiary()
                    .firstName("Olivia")
                    .lastName("Van Karsteen")
                    .identification("200208114652")
                .end()
                .prescription()
                    .comment("")
                    .date(2016, 5, 18)
                    .diagnose(new None())
                    .prescriber("")
                .end()
                .build();

        GrantRuleResult result1 = validationService.test(
                child13AboveAmount, testDate);
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
                .firstName("Olivia")
                .lastName("Van Karsteen")
                .identification("200208114652")
                .build();

        Prescription prescription = PrescriptionFactory.newPrescription()
                .comment("")
                .date(2016, 5, 18)
                .diagnose(new None())
                .prescriber("")
                .build();

        GrantFactory.newGrant()
                .delivery(2016, 6, 3)
                .amount(new BigDecimal("800"))
                .area("14", "95")
                .beneficiary(beneficiary)
                .prescription(prescription)
                .build();
        Grant child13AboveAmountMultiple = GrantFactory.newGrant()
                .delivery(2016, 6, 19)
                .amount(new BigDecimal("300"))
                .area("14", "95")
                .beneficiary(beneficiary)
                .prescription(prescription)
                .build();

        GrantRuleResult result2 = validationService.test(
                child13AboveAmountMultiple, testDate);
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
                .delivery(2016, 6, 3)
                .amount(new BigDecimal("1000"))
                .area("14", "95")
                .beneficiary()
                    .firstName("Olivia")
                    .lastName("Van Karsteen")
                    .identification("200208114652")
                .end()
                .prescription()
                    .comment("")
                    .date(2016, 5, 18)
                    .diagnose(new None())
                    .prescriber("")
                .end()
                .build();

        GrantRuleResult result3 = validationService.test(
                child13ExactAmount, testDate);
        Assert.assertFalse(result3.hasWarnings());
        Assert.assertFalse(result3.hasViolations());


        // Testing when a beneficiary of age 13 receives under 1000 SEK.
        // This should produce no warnings and no violations.

        Grant child13UnderAmount = GrantFactory.newGrant()
                .delivery(2016, 6, 3)
                .amount(new BigDecimal("500"))
                .area("14", "95")
                .beneficiary()
                    .firstName("Olivia")
                    .lastName("Van Karsteen")
                    .identification("200208114660")
                .end()
                .prescription()
                    .comment("")
                    .date(2016, 5, 18)
                    .diagnose(new None())
                    .prescriber("")
                .end()
                .build();

        GrantRuleResult result4 = validationService.test(
                child13ExactAmount, testDate);
        Assert.assertFalse(result4.hasWarnings());
        Assert.assertFalse(result4.hasViolations());
    }

    /**
     * Children 16 to 19 may at most receive 800 SEK for one prescription in
     * one calendar, between 2016-03-01 and 2016-06-20.
     */
    @Test
    public void children16To19Max800kr() {
        Date testDate = new GregorianCalendar(2016, 7, 4).getTime();

        // Testing when a beneficiary of age 18 receives more than 800 SEK.
        // This should produce no warnings, and one violation since the amount
        // exceeds 800 SEK.

        Grant child18AboveAmount = GrantFactory.newGrant()
               .delivery(2016, 6, 3)
                .amount(new BigDecimal("1000"))
                .area("14", "96")
                .beneficiary()
                    .firstName("Valdemar")
                    .lastName("Mur")
                    .identification("199804103431")
                .end()
                .prescription()
                    .comment("")
                    .date(2016, 5, 18)
                    .diagnose(new None())
                    .prescriber("")
                .end()
                .build();

        GrantRuleResult result1 = validationService.test(
                child18AboveAmount, testDate);
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
                .firstName("Valdemar")
                .lastName("Mur")
                .identification("199804103431")
                .build();

        Prescription prescription = PrescriptionFactory.newPrescription()
                .comment("")
                .date(2016, 5, 18)
                .diagnose(new None())
                .prescriber("")
                .build();

        GrantFactory.newGrant()
                .delivery(2016, 6, 3)
                .amount(new BigDecimal("800"))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(prescription)
                .build();
        Grant child18AboveAmountMultiple = GrantFactory.newGrant()
                .delivery(2016, 6, 18)
                .amount(new BigDecimal("300"))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(prescription)
                .build();

        GrantRuleResult result2 = validationService.test(
                child18AboveAmountMultiple, testDate);
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
                .delivery(2016, 6, 3)
                .amount(new BigDecimal("800"))
                .area("14", "96")
                .beneficiary()
                    .firstName("Valdemar")
                    .lastName("Mur")
                    .identification("199804103431")
                .end()
                .prescription()
                    .comment("")
                    .date(2016, 5, 18)
                    .diagnose(new None())
                    .prescriber("")
                .end()
                .build();

        GrantRuleResult result3 = validationService.test(
                child18ExactAmount, testDate);
        Assert.assertFalse(result3.hasWarnings());
        Assert.assertFalse(result3.hasViolations());


        // Testing when a beneficiary of age 18 receives under 800 SEK.
        // This should produce no warnings and no violations.

        Grant child18UnderAmount = GrantFactory.newGrant()
                .delivery(2016, 6, 3)
                .amount(new BigDecimal("600"))
                .area("14", "96")
                .beneficiary()
                    .firstName("Valdemar")
                    .lastName("Mur")
                    .identification("199804103431")
                .end()
                .prescription()
                    .comment("")
                    .date(2016, 5, 18)
                    .diagnose(new None())
                    .prescriber("")
                .end()
                .build();

        GrantRuleResult result4 = validationService.test(
                child18UnderAmount, testDate);
        Assert.assertFalse(result4.hasWarnings());
        Assert.assertFalse(result4.hasViolations());
    }

    /**
     * Children 0 to 19 may at most receive 1600 SEK for one prescription in
     * one calendar, after 2016-06-20.
     */
    @Test
    public void children0To19Max1600krPost20160620() {
        Date testDate = new GregorianCalendar(2016, 10, 31).getTime();

        // Testing when a beneficiary of age 16 receives more than 1600 SEK.
        // This should produce no warnings, and one violation since the amount
        // exceeds 1600 SEK.

        Grant child16AboveAmount = GrantFactory.newGrant()
                .delivery(2016, 8, 3)
                .amount(new BigDecimal("2000"))
                .area("14", "96")
                .beneficiary()
                    .firstName("Sven")
                    .lastName("Larsson")
                    .identification("200001013431")
                .end()
                .prescription()
                    .comment("")
                    .date(2016, 7, 18)
                    .diagnose(new None())
                    .prescriber("")
                .end()
                .build();

        GrantRuleResult result1 = validationService.test(
                child16AboveAmount, testDate);
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
                .firstName("Sven")
                .lastName("Larsson")
                .identification("200001013431")
                .build();

        Prescription prescription = PrescriptionFactory.newPrescription()
                .comment("")
                .date(2016, 7, 18)
                .diagnose(new None())
                .prescriber("")
                .build();

        GrantFactory.newGrant()
                .delivery(2016, 8, 3)
                .amount(new BigDecimal("800"))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(prescription)
                .build();
        Grant child16AboveAmountMultiple = GrantFactory.newGrant()
                .delivery(2016, 8, 13)
                .amount(new BigDecimal("900"))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(prescription)
                .build();

        GrantRuleResult result2 = validationService.test(
                child16AboveAmountMultiple, testDate);
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
                .delivery(2016, 8, 3)
                .amount(new BigDecimal("1600"))
                .area("14", "96")
                .beneficiary()
                    .firstName("Sven")
                    .lastName("Larsson")
                    .identification("200001013431")
                .end()
                .prescription()
                    .comment("")
                    .date(2016, 7, 18)
                    .diagnose(new None())
                    .prescriber("")
                .end()
                .build();

        GrantRuleResult result3 = validationService.test(
                child16ExactAmount, testDate);
        Assert.assertFalse(result3.hasWarnings());
        Assert.assertFalse(result3.hasViolations());


        // Testing when a beneficiary of age 18 receives under 800 SEK.
        // This should produce no warnings and no violations.

        Grant child16UnderAmount = GrantFactory.newGrant()
                .delivery(2016, 8, 3)
                .amount(new BigDecimal("1200"))
                .area("14", "96")
                .beneficiary()
                    .firstName("Sven")
                    .lastName("Larsson")
                    .identification("200001013431")
                .end()
                .prescription()
                    .comment("")
                    .date(2016, 7, 18)
                    .diagnose(new None())
                    .prescriber("")
                .end()
                .build();

        GrantRuleResult result4 = validationService.test(
                child16UnderAmount, testDate);
        Assert.assertFalse(result4.hasWarnings());
        Assert.assertFalse(result4.hasViolations());
    }

    @Test
    public void aphakiaMax2000kr() {
        testCustomDiagnoseAndAmount(
                new Aphakia(VisualLaterality.BILATERAL),
                "196803012340",
                new BigDecimal("2000"),
                new GregorianCalendar(2010, 3, 10).getTime(),
                new GregorianCalendar(2016, 1, 9).getTime(),
                new GregorianCalendar(2016, 1, 14).getTime(),
                new GregorianCalendar(2016, 2, 1).getTime(),
                "violation-amount-greater-than-2000-" +
                        "for-aphakia-and-special-pre-20160620");
    }

    @Test
    public void aphakiaMax2400krPost20160620() {
        testCustomDiagnoseAndAmount(
                new Aphakia(VisualLaterality.BILATERAL),
                "196803012340",
                new BigDecimal("2400"),
                new GregorianCalendar(2010, 3, 10).getTime(),
                new GregorianCalendar(2016, 7, 9).getTime(),
                new GregorianCalendar(2016, 7, 14).getTime(),
                new GregorianCalendar(2016, 8, 1).getTime(),
                "violation-amount-greater-than-2400-" +
                        "for-aphakia-and-special-post-20160620");
    }

    @Test
    public void specialMax2000kr() {
        testCustomDiagnoseAndAmount(
                new Special(VisualLaterality.BILATERAL, false),
                "197304194579",
                new BigDecimal("2000"),
                new GregorianCalendar(2010, 3, 10).getTime(),
                new GregorianCalendar(2016, 1, 9).getTime(),
                new GregorianCalendar(2016, 1, 14).getTime(),
                new GregorianCalendar(2016, 2, 1).getTime(),
                "violation-amount-greater-than-2000-" +
                        "for-aphakia-and-special-pre-20160620");
    }

    @Test
    public void specialMax2400krPost20160620() {
        testCustomDiagnoseAndAmount(
                new Special(VisualLaterality.BILATERAL, false),
                "197304194579",
                new BigDecimal("2400"),
                new GregorianCalendar(2010, 3, 10).getTime(),
                new GregorianCalendar(2016, 7, 9).getTime(),
                new GregorianCalendar(2016, 7, 14).getTime(),
                new GregorianCalendar(2016, 8, 1).getTime(),
                "violation-amount-greater-than-2400-" +
                        "for-aphakia-and-special-post-20160620");
    }

    @Test
    public void keratoconusMax3400kr() {
        testCustomDiagnoseAndAmount(
                new Keratoconus(VisualLaterality.LEFT, 0.0f, 0.3f, false),
                "193912207143",
                new BigDecimal("2400"),
                new GregorianCalendar(2010, 3, 10).getTime(),
                new GregorianCalendar(2016, 1, 9).getTime(),
                new GregorianCalendar(2016, 1, 14).getTime(),
                new GregorianCalendar(2016, 2, 1).getTime(),
                "violation-amount-greater-than-2400-" +
                        "for-keratoconus-pre-20160620");
    }

    @Test
    public void keratoconusMax3000krPost20160620() {
        testCustomDiagnoseAndAmount(
                new Keratoconus(VisualLaterality.LEFT, 0.0f, 0.3f, false),
                "193912207143",
                new BigDecimal("3000"),
                new GregorianCalendar(2010, 3, 10).getTime(),
                new GregorianCalendar(2016, 7, 9).getTime(),
                new GregorianCalendar(2016, 7, 14).getTime(),
                new GregorianCalendar(2016, 8, 1).getTime(),
                "violation-amount-greater-than-3000-" +
                        "for-keratoconus-post-20160620");
    }

    // Helper
    private void testCustomDiagnoseAndAmount(Diagnose diagnose,
                                            String identification,
                                            BigDecimal amount,
                                            Date prescriptionDate,
                                            Date deliveryDate1,
                                            Date deliveryDate2,
                                            Date testDate,
                                            String violation) {
        // Test when grant exceed amount.
        Grant grant1 = GrantFactory.newGrant()
                .delivery(deliveryDate1)
                .amount(amount.add(new BigDecimal("100")))
                .area("14", "96")
                .beneficiary()
                    .firstName("Test1")
                    .lastName("Test1")
                    .identification(identification)
                .end()
                .prescription()
                    .comment("")
                    .date(prescriptionDate)
                    .diagnose(diagnose)
                    .prescriber("")
                .end()
                .build();

        GrantRuleResult result1 = validationService.test(grant1, testDate);
        Assert.assertFalse(result1.hasWarnings());
        Assert.assertTrue(result1.hasViolations());
        Assert.assertEquals(1, result1.violations());
        Assert.assertTrue(result1.getViolations().contains(
                new GrantRuleViolation(violation)));


        // Test when multiple grants exceed amount.
        Beneficiary beneficiary = BeneficiaryFactory.newBeneficiary()
                .firstName("Test2")
                .lastName("Test2")
                .identification(identification)
                .build();

        Prescription prescription = PrescriptionFactory.newPrescription()
                .comment("")
                .date(prescriptionDate)
                .diagnose(diagnose)
                .prescriber("")
                .build();

        GrantFactory.newGrant()
                .delivery(deliveryDate1)
                .amount(amount.divide(new BigDecimal("2"))
                              .add(new BigDecimal("100")))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(prescription)
                .build();
        Grant grant2 = GrantFactory.newGrant()
                .delivery(deliveryDate2)
                .amount(amount.divide(new BigDecimal("2")))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(prescription)
                .build();

        GrantRuleResult result2 = validationService.test(grant2, testDate);
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
                    .firstName("Test3")
                    .lastName("Test3")
                    .identification(identification)
                .end()
                .prescription()
                    .comment("")
                    .date(prescriptionDate)
                    .diagnose(diagnose)
                    .prescriber("")
                .end()
                .build();

        GrantRuleResult result3 = validationService.test(grant3, testDate);
        Assert.assertFalse(result3.hasWarnings());
        Assert.assertFalse(result3.hasViolations());

        // Grant's amount is below max amount.
        Grant grant4 = GrantFactory.newGrant()
                .delivery(deliveryDate1)
                .amount(amount.subtract(new BigDecimal("200")))
                .area("14", "96")
                .beneficiary()
                    .firstName("Test4")
                    .lastName("Test4")
                    .identification(identification)
                .end()
                .prescription()
                    .comment("")
                    .date(prescriptionDate)
                    .diagnose(diagnose)
                    .prescriber("")
                .end()
                .build();

        GrantRuleResult result4 = validationService.test(grant4, testDate);
        Assert.assertFalse(result4.hasWarnings());
        Assert.assertFalse(result4.hasViolations());
    }
}
