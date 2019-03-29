package se.vgregion.service.glasogonbidrag.local;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.vgregion.portal.glasogonbidrag.domain.VisualLaterality;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Beneficiary;
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

import java.math.BigDecimal;
import java.util.*;

import static java.util.Calendar.JANUARY;
import static java.util.Calendar.FEBRUARY;
import static java.util.Calendar.MARCH;
import static java.util.Calendar.APRIL;
import static java.util.Calendar.MAY;
import static java.util.Calendar.JUNE;
import static java.util.Calendar.AUGUST;
import static java.util.Calendar.OCTOBER;
import static java.util.Calendar.DECEMBER;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:localTestContext.xml")
public class GrantRuleValidationServiceTestPre20160620 {

    @Autowired
    private GrantRuleValidationService validationService;

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
                new HashSet<>(Collections.singletonList(
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
                new HashSet<>(Collections.singletonList(
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
     * Test so that the amounts grantable for keratoconus before
     * 2016-06-20 works.
     * Tests diagnose for left eye, right eye and both eyes.
     */
    @Test
    public void keratoconus() {

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

    // Tests for Aphakia ------------------------------------------------------

    @Test
    public void aphakiaLeftEyeOneGrant() {
        // Test grant is within grantable amount.
        Grant grant1OkLeftEye = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2016, MARCH, 2).getTime())
                .amount(new BigDecimal("2000"))
                .area("14", "96")
                .beneficiary()
                    .fullName("User with a name")
                    .identification("196803012340")
                .end()
                .prescription()
                    .comment("empty.")
                    .date(new GregorianCalendar(2010, MARCH, 10).getTime())
                    .diagnose(new Aphakia(VisualLaterality.LEFT))
                    .prescriber("the doctor")
                .end()
                .build();

        GrantRuleResult result1 = validationService.test(
                grant1OkLeftEye, new HashSet<Grant>());
        Assert.assertFalse(result1.hasWarnings());
        Assert.assertFalse(result1.hasViolations());

        // Grant is above grantable amount.
        Grant grant1OverLeftEye = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2016, MARCH, 2).getTime())
                .amount(new BigDecimal("2100"))
                .area("14", "96")
                .beneficiary()
                    .fullName("User with a name")
                    .identification("196803012340")
                .end()
                .prescription()
                    .comment("empty.")
                    .date(new GregorianCalendar(2010, MARCH, 10).getTime())
                    .diagnose(new Aphakia(VisualLaterality.LEFT))
                    .prescriber("the doctor")
                .end()
                .build();

        GrantRuleResult result2 = validationService.test(
                grant1OverLeftEye, new HashSet<Grant>());
        Assert.assertFalse(result2.hasWarnings());
        Assert.assertTrue(result2.hasViolations());
        Assert.assertEquals(1, result2.violations());
        Assert.assertTrue(result2.getViolations().contains(
                new GrantRuleViolation(
                        "violation-amount-greater-than-2000-" +
                        "for-aphakia-one-eye-pre-20160620")));
    }

    @Test
    public void aphakiaRightEyeOneGrant() {
        // Test grant is within grantable amount.
        Grant grant1OkLeftEye = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2016, MARCH, 2).getTime())
                .amount(new BigDecimal("2000"))
                .area("14", "96")
                .beneficiary()
                    .fullName("Person with a name")
                    .identification("196803012340")
                .end()
                .prescription()
                    .comment("empty.")
                    .date(new GregorianCalendar(2010, MARCH, 10).getTime())
                    .diagnose(new Aphakia(VisualLaterality.RIGHT))
                    .prescriber("the doctor")
                .end()
                .build();

        GrantRuleResult result1 = validationService.test(
                grant1OkLeftEye, new HashSet<Grant>());
        Assert.assertFalse(result1.hasWarnings());
        Assert.assertFalse(result1.hasViolations());

        // Grant is above grantable amount.
        Grant grant1OverLeftEye = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2016, MARCH, 2).getTime())
                .amount(new BigDecimal("2100"))
                .area("14", "96")
                .beneficiary()
                    .fullName("User with a name")
                    .identification("196803012340")
                .end()
                .prescription()
                    .comment("empty.")
                    .date(new GregorianCalendar(2010, MARCH, 10).getTime())
                    .diagnose(new Aphakia(VisualLaterality.RIGHT))
                    .prescriber("the doctor")
                .end()
                .build();

        GrantRuleResult result2 = validationService.test(
                grant1OverLeftEye, new HashSet<Grant>());
        Assert.assertFalse(result2.hasWarnings());
        Assert.assertTrue(result2.hasViolations());
        Assert.assertEquals(1, result2.violations());
        Assert.assertTrue(result2.getViolations().contains(
                new GrantRuleViolation(
                        "violation-amount-greater-than-2000-" +
                        "for-aphakia-one-eye-pre-20160620")));
    }

    @Test
    public void aphakiaBothEyesOneGrant() {
        Grant grant1OkBothEyes = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2016, MARCH, 2).getTime())
                .amount(new BigDecimal("4000"))
                .area("14", "96")
                .beneficiary()
                    .fullName("Person with a name")
                    .identification("196803012340")
                .end()
                .prescription()
                    .comment("empty.")
                    .date(new GregorianCalendar(2010, MARCH, 10).getTime())
                    .diagnose(new Aphakia(VisualLaterality.BILATERAL))
                    .prescriber("The doctor")
                .end()
                .build();

        GrantRuleResult result1 = validationService.test(
                grant1OkBothEyes, new HashSet<Grant>());
        Assert.assertFalse(result1.hasWarnings());
        Assert.assertFalse(result1.hasViolations());

        Grant grant1OverBothEyes = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2016, MARCH, 2).getTime())
                .amount(new BigDecimal("4100"))
                .area("14", "96")
                .beneficiary()
                    .fullName("Person with a name")
                    .identification("196803012340")
                .end()
                .prescription()
                    .comment("empty.")
                    .date(new GregorianCalendar(2010, MARCH, 10).getTime())
                    .diagnose(new Aphakia(VisualLaterality.BILATERAL))
                    .prescriber("The doctor")
                .end()
                .build();

        GrantRuleResult result2 = validationService.test(
                grant1OverBothEyes, new HashSet<Grant>());
        Assert.assertFalse(result2.hasWarnings());
        Assert.assertTrue(result2.hasViolations());
        Assert.assertTrue(result2.getViolations().contains(
                new GrantRuleViolation(
                        "violation-amount-greater-than-4000-" +
                        "for-aphakia-both-eyes-pre-20160620")));
    }

    @Test
    public void aphakiaRightEyeMultipleGrants() {
        // Two grants is okay.
        Prescription prescription = PrescriptionFactory.newPrescription()
                .comment("Person with a name")
                .date(new GregorianCalendar(2010, MARCH, 10).getTime())
                .diagnose(new Aphakia(VisualLaterality.RIGHT))
                .prescriber("The doctor")
                .build();

        Beneficiary beneficiary = BeneficiaryFactory.newBeneficiary()
                .fullName("Person with a name")
                .identification("196803012340")
                .build();


        Set<Grant> grantHistory = new HashSet<>();

        Grant grant1 = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2016, MARCH, 10).getTime())
                .amount(new BigDecimal("1000"))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(prescription)
                .build();

        Grant grant2 = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2016, MARCH, 11).getTime())
                .amount(new BigDecimal("500"))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(prescription)
                .build();

        grantHistory.add(grant1);

        GrantRuleResult result1 = validationService.test(grant2, grantHistory);
        Assert.assertTrue(result1.hasWarnings());
        Assert.assertFalse(result1.hasViolations());

        // More than two is not okay (with amount equals 2400 which is okay)
        Grant grant3 = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2016, MARCH, 11).getTime())
                .amount(new BigDecimal("500"))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(prescription)
                .build();

        grantHistory.add(grant2);

        GrantRuleResult result2 = validationService.test(grant3, grantHistory);
        Assert.assertTrue(result2.hasWarnings());
        Assert.assertTrue(result2.hasViolations());
        Assert.assertTrue(result2.getViolations().contains(
                new GrantRuleViolation(
                        "violation-too-many-grants-for-aphakia")));

        // More than two and above grantable amount. is not ok.
        Grant grant4 = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2016, MARCH, 11).getTime())
                .amount(new BigDecimal("600"))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(prescription)
                .build();

        GrantRuleResult result3 = validationService.test(grant4, grantHistory);
        Assert.assertTrue(result3.hasWarnings());
        Assert.assertTrue(result3.hasViolations());
//        Assert.assertTrue(result3.getViolations().contains(
//                new GrantRuleViolation(
//                        "violation-too-many-grants-" +
//                        "for-aphakia-or-special-post-20160620")));
    }

    @Test
    public void aphakiaLeftEyeMultipleGrants() {
        // Two grants is okay.
        Prescription prescription = PrescriptionFactory.newPrescription()
                .comment("Person with a name")
                .date(new GregorianCalendar(2010, MARCH, 10).getTime())
                .diagnose(new Aphakia(VisualLaterality.LEFT))
                .prescriber("The doctor")
                .build();

        Beneficiary beneficiary = BeneficiaryFactory.newBeneficiary()
                .fullName("Person with a name")
                .identification("196803012340")
                .build();


        Set<Grant> grantHistory = new HashSet<>();

        Grant grant1 = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2016, MARCH, 10).getTime())
                .amount(new BigDecimal("1000"))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(prescription)
                .build();

        Grant grant2 = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2016, MARCH, 11).getTime())
                .amount(new BigDecimal("500"))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(prescription)
                .build();

        grantHistory.add(grant1);

        GrantRuleResult result1 = validationService.test(grant2, grantHistory);
        Assert.assertTrue(result1.hasWarnings());
        Assert.assertFalse(result1.hasViolations());

        // More than two is not okay (with amount equals 2400 which is okay)
        Grant grant3 = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2016, MARCH, 11).getTime())
                .amount(new BigDecimal("500"))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(prescription)
                .build();

        grantHistory.add(grant2);

        GrantRuleResult result2 = validationService.test(grant3, grantHistory);
        Assert.assertTrue(result2.hasWarnings());
        Assert.assertTrue(result2.hasViolations());
        Assert.assertTrue(result2.getViolations().contains(
                new GrantRuleViolation(
                        "violation-too-many-grants-for-aphakia")));

        // More than two and above grantable amount. is not ok.
        Grant grant4 = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2016, MARCH, 11).getTime())
                .amount(new BigDecimal("600"))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(prescription)
                .build();

        GrantRuleResult result3 = validationService.test(grant4, grantHistory);
        Assert.assertTrue(result3.hasWarnings());
        Assert.assertTrue(result3.hasViolations());
//        Assert.assertTrue(result3.getViolations().contains(
//                new GrantRuleViolation(
//                        "violation-too-many-grants-" +
//                        "for-aphakia-or-special-post-20160620")));
    }

    @Test
    public void aphakiaBothEyeMultipleGrants() {
        // Two grants is okay.
        Prescription prescription = PrescriptionFactory.newPrescription()
                .comment("Person with a name")
                .date(new GregorianCalendar(2010, MARCH, 10).getTime())
                .diagnose(new Aphakia(VisualLaterality.BILATERAL))
                .prescriber("The doctor")
                .build();

        Beneficiary beneficiary = BeneficiaryFactory.newBeneficiary()
                .fullName("Person with a name")
                .identification("196803012340")
                .build();


        Set<Grant> grantHistory = new HashSet<>();

        Grant grant1 = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2016, MARCH, 10).getTime())
                .amount(new BigDecimal("2000"))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(prescription)
                .build();

        Grant grant2 = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2016, MARCH, 11).getTime())
                .amount(new BigDecimal("1000"))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(prescription)
                .build();

        grantHistory.add(grant1);

        GrantRuleResult result1 = validationService.test(grant2, grantHistory);
        Assert.assertTrue(result1.hasWarnings());
        Assert.assertFalse(result1.hasViolations());

        // More than two is not okay (with amount equals 2400 which is okay)
        Grant grant3 = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2016, MARCH, 11).getTime())
                .amount(new BigDecimal("1000"))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(prescription)
                .build();

        grantHistory.add(grant2);

        GrantRuleResult result2 = validationService.test(grant3, grantHistory);
        Assert.assertTrue(result2.hasWarnings());
        Assert.assertTrue(result2.hasViolations());
        Assert.assertTrue(result2.getViolations().contains(
                new GrantRuleViolation(
                        "violation-too-many-grants-for-aphakia")));

        // More than two and above grantable amount. is not ok.
        Grant grant4 = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2016, MARCH, 11).getTime())
                .amount(new BigDecimal("1100"))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(prescription)
                .build();

        GrantRuleResult result3 = validationService.test(grant4, grantHistory);
        Assert.assertTrue(result3.hasWarnings());
        Assert.assertTrue(result3.hasViolations());
//        Assert.assertTrue(result3.getViolations().contains(
//                new GrantRuleViolation(
//                        "violation-too-many-grants-" +
//                        "for-aphakia-or-special-post-20160620")));
    }

    // Tests for Special ------------------------------------------------------

    @Test
    public void specialLeftEyeOneGrant() {
        // Test grant is within grantable amount.
        Grant grant1OkLeftEye = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2016, MARCH, 2).getTime())
                .amount(new BigDecimal("2000"))
                .area("14", "96")
                .beneficiary()
                    .fullName("User with a name")
                    .identification("196803012340")
                .end()
                .prescription()
                    .comment("empty.")
                    .date(new GregorianCalendar(2010, MARCH, 10).getTime())
                    .diagnose(new Special(VisualLaterality.LEFT, false))
                    .prescriber("the doctor")
                .end()
                .build();

        GrantRuleResult result1 = validationService.test(
                grant1OkLeftEye, new HashSet<Grant>());
        Assert.assertFalse(result1.hasWarnings());
        Assert.assertFalse(result1.hasViolations());

        // Grant is above grantable amount.
        Grant grant1OverLeftEye = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2016, MARCH, 2).getTime())
                .amount(new BigDecimal("2100"))
                .area("14", "96")
                .beneficiary()
                    .fullName("User with a name")
                    .identification("196803012340")
                .end()
                .prescription()
                    .comment("empty.")
                    .date(new GregorianCalendar(2010, MARCH, 10).getTime())
                    .diagnose(new Special(VisualLaterality.LEFT, false))
                    .prescriber("the doctor")
                .end()
                .build();

        GrantRuleResult result2 = validationService.test(
                grant1OverLeftEye, new HashSet<Grant>());
        Assert.assertFalse(result2.hasWarnings());
        Assert.assertTrue(result2.hasViolations());
        Assert.assertEquals(1, result2.violations());
        Assert.assertTrue(result2.getViolations().contains(
                new GrantRuleViolation(
                        "violation-amount-greater-than-2000-" +
                        "for-special-one-eye-pre-20160620")));
    }

    @Test
    public void specialRightEyeOneGrant() {
        // Test grant is within grantable amount.
        Grant grant1OkLeftEye = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2016, MARCH, 2).getTime())
                .amount(new BigDecimal("2000"))
                .area("14", "96")
                .beneficiary()
                    .fullName("Person with a name")
                    .identification("196803012340")
                .end()
                .prescription()
                    .comment("empty.")
                    .date(new GregorianCalendar(2010, MARCH, 10).getTime())
                    .diagnose(new Special(VisualLaterality.RIGHT, false))
                    .prescriber("the doctor")
                .end()
                .build();

        GrantRuleResult result1 = validationService.test(
                grant1OkLeftEye, new HashSet<Grant>());
        Assert.assertFalse(result1.hasWarnings());
        Assert.assertFalse(result1.hasViolations());

        // Grant is above grantable amount.
        Grant grant1OverLeftEye = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2016, MARCH, 2).getTime())
                .amount(new BigDecimal("2500"))
                .area("14", "96")
                .beneficiary()
                    .fullName("User with a name")
                    .identification("196803012340")
                .end()
                .prescription()
                    .comment("empty.")
                    .date(new GregorianCalendar(2010, MARCH, 10).getTime())
                    .diagnose(new Special(VisualLaterality.RIGHT, false))
                    .prescriber("the doctor")
                .end()
                .build();

        GrantRuleResult result2 = validationService.test(
                grant1OverLeftEye, new HashSet<Grant>());
        Assert.assertFalse(result2.hasWarnings());
        Assert.assertTrue(result2.hasViolations());
        Assert.assertEquals(1, result2.violations());
        Assert.assertTrue(result2.getViolations().contains(
                new GrantRuleViolation(
                        "violation-amount-greater-than-2000-" +
                        "for-special-one-eye-pre-20160620")));
    }

    @Test
    public void specialBothEyesOneGrant() {
        Grant grant1OkBothEyes = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2016, MARCH, 2).getTime())
                .amount(new BigDecimal("4000"))
                .area("14", "96")
                .beneficiary()
                    .fullName("Person with a name")
                    .identification("196803012340")
                .end()
                .prescription()
                    .comment("empty.")
                    .date(new GregorianCalendar(2010, MARCH, 10).getTime())
                    .diagnose(new Special(VisualLaterality.BILATERAL, false))
                    .prescriber("The doctor")
                .end()
                .build();

        GrantRuleResult result1 = validationService.test(
                grant1OkBothEyes, new HashSet<Grant>());
        Assert.assertFalse(result1.hasWarnings());
        Assert.assertFalse(result1.hasViolations());

        Grant grant1OverBothEyes = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2016, MARCH, 2).getTime())
                .amount(new BigDecimal("4100"))
                .area("14", "96")
                .beneficiary()
                    .fullName("Person with a name")
                    .identification("196803012340")
                .end()
                .prescription()
                    .comment("empty.")
                    .date(new GregorianCalendar(2010, MARCH, 10).getTime())
                    .diagnose(new Special(VisualLaterality.BILATERAL, false))
                    .prescriber("The doctor")
                .end()
                .build();

        GrantRuleResult result2 = validationService.test(
                grant1OverBothEyes, new HashSet<Grant>());
        Assert.assertFalse(result2.hasWarnings());
        Assert.assertTrue(result2.hasViolations());
        Assert.assertTrue(result2.getViolations().contains(
                new GrantRuleViolation(
                        "violation-amount-greater-than-4000-" +
                        "for-special-both-eyes-pre-20160620")));
    }

    @Test
    public void specialRightEyeMultipleGrants() {
        // Two grants is okay.
        Prescription prescription = PrescriptionFactory.newPrescription()
                .comment("Person with a name")
                .date(new GregorianCalendar(2010, MARCH, 10).getTime())
                .diagnose(new Special(VisualLaterality.RIGHT, false))
                .prescriber("The doctor")
                .build();

        Beneficiary beneficiary = BeneficiaryFactory.newBeneficiary()
                .fullName("Person with a name")
                .identification("196803012340")
                .build();


        Set<Grant> grantHistory = new HashSet<>();

        Grant grant1 = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2016, MARCH, 10).getTime())
                .amount(new BigDecimal("1000"))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(prescription)
                .build();

        Grant grant2 = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2016, MARCH, 11).getTime())
                .amount(new BigDecimal("500"))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(prescription)
                .build();

        grantHistory.add(grant1);

        GrantRuleResult result1 = validationService.test(grant2, grantHistory);
        Assert.assertTrue(result1.hasWarnings());
        Assert.assertFalse(result1.hasViolations());

        // More than two is not okay (with amount equals 2400 which is okay)
        Grant grant3 = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2016, MARCH, 11).getTime())
                .amount(new BigDecimal("500"))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(prescription)
                .build();

        grantHistory.add(grant2);

        GrantRuleResult result2 = validationService.test(grant3, grantHistory);
        Assert.assertTrue(result2.hasWarnings());
        Assert.assertTrue(result2.hasViolations());
        Assert.assertTrue(result2.getViolations().contains(
                new GrantRuleViolation(
                        "violation-too-many-grants-for-special")));

        // More than two and above grantable amount. is not ok.
        Grant grant4 = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2016, MARCH, 11).getTime())
                .amount(new BigDecimal("600"))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(prescription)
                .build();

        GrantRuleResult result3 = validationService.test(grant4, grantHistory);
        Assert.assertTrue(result3.hasWarnings());
        Assert.assertTrue(result3.hasViolations());
//        Assert.assertTrue(result3.getViolations().contains(
//                new GrantRuleViolation(
//                        "violation-too-many-grants-" +
//                        "for-aphakia-or-special-post-20160620")));
    }

    @Test
    public void specialLeftEyeMultipleGrants() {
        // Two grants is okay.
        Prescription prescription = PrescriptionFactory.newPrescription()
                .comment("Person with a name")
                .date(new GregorianCalendar(2010, MARCH, 10).getTime())
                .diagnose(new Special(VisualLaterality.LEFT, false))
                .prescriber("The doctor")
                .build();

        Beneficiary beneficiary = BeneficiaryFactory.newBeneficiary()
                .fullName("Person with a name")
                .identification("196803012340")
                .build();


        Set<Grant> grantHistory = new HashSet<>();

        Grant grant1 = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2016, MARCH, 10).getTime())
                .amount(new BigDecimal("1000"))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(prescription)
                .build();

        Grant grant2 = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2016, MARCH, 11).getTime())
                .amount(new BigDecimal("500"))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(prescription)
                .build();

        grantHistory.add(grant1);

        GrantRuleResult result1 = validationService.test(grant2, grantHistory);
        Assert.assertTrue(result1.hasWarnings());
        Assert.assertFalse(result1.hasViolations());

        // More than two is not okay (with amount equals 2400 which is okay)
        Grant grant3 = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2016, MARCH, 11).getTime())
                .amount(new BigDecimal("500"))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(prescription)
                .build();

        grantHistory.add(grant2);

        GrantRuleResult result2 = validationService.test(grant3, grantHistory);
        Assert.assertTrue(result2.hasWarnings());
        Assert.assertTrue(result2.hasViolations());
        Assert.assertTrue(result2.getViolations().contains(
                new GrantRuleViolation(
                        "violation-too-many-grants-for-special")));

        // More than two and above grantable amount. is not ok.
        Grant grant4 = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2016, MARCH, 11).getTime())
                .amount(new BigDecimal("600"))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(prescription)
                .build();

        GrantRuleResult result3 = validationService.test(grant4, grantHistory);
        Assert.assertTrue(result3.hasWarnings());
        Assert.assertTrue(result3.hasViolations());
//        Assert.assertTrue(result3.getViolations().contains(
//                new GrantRuleViolation(
//                        "violation-too-many-grants-" +
//                        "for-aphakia-or-special-post-20160620")));
    }

    @Test
    public void specialBothEyeMultipleGrants() {
        // Two grants is okay.
        Prescription prescription = PrescriptionFactory.newPrescription()
                .comment("Person with a name")
                .date(new GregorianCalendar(2010, MARCH, 10).getTime())
                .diagnose(new Special(VisualLaterality.BILATERAL, false))
                .prescriber("The doctor")
                .build();

        Beneficiary beneficiary = BeneficiaryFactory.newBeneficiary()
                .fullName("Person with a name")
                .identification("196803012340")
                .build();


        Set<Grant> grantHistory = new HashSet<>();

        Grant grant1 = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2016, MARCH, 10).getTime())
                .amount(new BigDecimal("2000"))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(prescription)
                .build();

        Grant grant2 = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2016, MARCH, 11).getTime())
                .amount(new BigDecimal("1000"))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(prescription)
                .build();

        grantHistory.add(grant1);

        GrantRuleResult result1 = validationService.test(grant2, grantHistory);
        Assert.assertTrue(result1.hasWarnings());
        Assert.assertFalse(result1.hasViolations());

        // More than two is not okay (with amount equals 2400 which is okay)
        Grant grant3 = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2016, MARCH, 11).getTime())
                .amount(new BigDecimal("1000"))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(prescription)
                .build();

        grantHistory.add(grant2);

        GrantRuleResult result2 = validationService.test(grant3, grantHistory);
        Assert.assertTrue(result2.hasWarnings());
        Assert.assertTrue(result2.hasViolations());
        Assert.assertTrue(result2.getViolations().contains(
                new GrantRuleViolation(
                        "violation-too-many-grants-for-special")));

        // More than two and above grantable amount. is not ok.
        Grant grant4 = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2016, MARCH, 11).getTime())
                .amount(new BigDecimal("1100"))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(prescription)
                .build();

        GrantRuleResult result3 = validationService.test(grant4, grantHistory);
        Assert.assertTrue(result3.hasWarnings());
        Assert.assertTrue(result3.hasViolations());
//        Assert.assertTrue(result3.getViolations().contains(
//                new GrantRuleViolation(
//                        "violation-too-many-grants-" +
//                        "for-aphakia-or-special-pre-20160620")));
    }
}
