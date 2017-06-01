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
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import static java.util.Calendar.*;
import static java.util.Calendar.JULY;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:localTestContext.xml")
public class GrantRuleValidationServiceTestPost20160620 {

    @Autowired
    private GrantRuleValidationService validationService;

    // Tests for Children -----------------------------------------------------

    /**
     * Children 0 to 19 may at most receive 1600 SEK for one prescription in
     * one calendar, after 2016-06-20.
     */
    @Test
    public void children0To19Max1600kr() {
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

    // Tests for Keratokonus --------------------------------------------------

    /**
     * Test so that the changed amounts for keratoconus works after 2016-06-20
     * Tests diagnose for left eye, right eye and both eyes.
     */
    @Test
    public void keratoconusTests() {

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

    // Tests for Aphakia ------------------------------------------------------

    @Test
    public void aphakiaLeftEyeOneGrant() {
        // Test grant is within grantable amount.
        Grant grant1OkLeftEye = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2017, MARCH, 2).getTime())
                .amount(new BigDecimal("2400"))
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
                .delivery(new GregorianCalendar(2017, MARCH, 2).getTime())
                .amount(new BigDecimal("2500"))
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
                        "violation-amount-greater-than-2400-" +
                        "for-aphakia-or-special-one-eye-post-20160620")));
    }

    @Test
    public void aphakiaRightEyeOneGrant() {
        // Test grant is within grantable amount.
        Grant grant1OkLeftEye = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2017, MARCH, 2).getTime())
                .amount(new BigDecimal("2400"))
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
                .delivery(new GregorianCalendar(2017, MARCH, 2).getTime())
                .amount(new BigDecimal("2500"))
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
                        "violation-amount-greater-than-2400-" +
                        "for-aphakia-or-special-one-eye-post-20160620")));
    }

    @Test
    public void aphakiaBothEyesOneGrant() {
        Grant grant1OkBothEyes = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2017, MARCH, 2).getTime())
                .amount(new BigDecimal("4800"))
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
                .delivery(new GregorianCalendar(2017, MARCH, 2).getTime())
                .amount(new BigDecimal("4900"))
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
                        "violation-amount-greater-than-4800-" +
                        "for-aphakia-or-special-both-eyes-post-20160620")));
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
                .delivery(new GregorianCalendar(2017, MARCH, 10).getTime())
                .amount(new BigDecimal("1200"))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(prescription)
                .build();

        Grant grant2 = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2017, MARCH, 11).getTime())
                .amount(new BigDecimal("600"))
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
                .delivery(new GregorianCalendar(2017, MARCH, 11).getTime())
                .amount(new BigDecimal("600"))
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
                        "violation-too-many-grants-for-aphakia-or-special")));

        // More than two and above grantable amount. is not ok.
        Grant grant4 = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2017, MARCH, 11).getTime())
                .amount(new BigDecimal("700"))
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
                .delivery(new GregorianCalendar(2017, MARCH, 10).getTime())
                .amount(new BigDecimal("1200"))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(prescription)
                .build();

        Grant grant2 = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2017, MARCH, 11).getTime())
                .amount(new BigDecimal("600"))
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
                .delivery(new GregorianCalendar(2017, MARCH, 11).getTime())
                .amount(new BigDecimal("600"))
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
                        "violation-too-many-grants-for-aphakia-or-special")));

        // More than two and above grantable amount. is not ok.
        Grant grant4 = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2017, MARCH, 11).getTime())
                .amount(new BigDecimal("700"))
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
                .delivery(new GregorianCalendar(2017, MARCH, 10).getTime())
                .amount(new BigDecimal("2400"))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(prescription)
                .build();

        Grant grant2 = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2017, MARCH, 11).getTime())
                .amount(new BigDecimal("1200"))
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
                .delivery(new GregorianCalendar(2017, MARCH, 11).getTime())
                .amount(new BigDecimal("1200"))
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
                        "violation-too-many-grants-for-aphakia-or-special")));

        // More than two and above grantable amount. is not ok.
        Grant grant4 = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2017, MARCH, 11).getTime())
                .amount(new BigDecimal("1300"))
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
                .delivery(new GregorianCalendar(2017, MARCH, 2).getTime())
                .amount(new BigDecimal("2400"))
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
                .delivery(new GregorianCalendar(2017, MARCH, 2).getTime())
                .amount(new BigDecimal("2500"))
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
                        "violation-amount-greater-than-2400-" +
                        "for-aphakia-or-special-one-eye-post-20160620")));
    }

    @Test
    public void specialRightEyeOneGrant() {
        // Test grant is within grantable amount.
        Grant grant1OkLeftEye = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2017, MARCH, 2).getTime())
                .amount(new BigDecimal("2400"))
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
                .delivery(new GregorianCalendar(2017, MARCH, 2).getTime())
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
                        "violation-amount-greater-than-2400-" +
                        "for-aphakia-or-special-one-eye-post-20160620")));
    }

    @Test
    public void specialBothEyesOneGrant() {
        Grant grant1OkBothEyes = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2017, MARCH, 2).getTime())
                .amount(new BigDecimal("4800"))
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
                .delivery(new GregorianCalendar(2017, MARCH, 2).getTime())
                .amount(new BigDecimal("4900"))
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
                        "violation-amount-greater-than-4800-" +
                        "for-aphakia-or-special-both-eyes-post-20160620")));
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
                .delivery(new GregorianCalendar(2017, MARCH, 10).getTime())
                .amount(new BigDecimal("1200"))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(prescription)
                .build();

        Grant grant2 = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2017, MARCH, 11).getTime())
                .amount(new BigDecimal("600"))
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
                .delivery(new GregorianCalendar(2017, MARCH, 11).getTime())
                .amount(new BigDecimal("600"))
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
                        "violation-too-many-grants-for-aphakia-or-special")));

        // More than two and above grantable amount. is not ok.
        Grant grant4 = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2017, MARCH, 11).getTime())
                .amount(new BigDecimal("700"))
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
                .delivery(new GregorianCalendar(2017, MARCH, 10).getTime())
                .amount(new BigDecimal("1200"))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(prescription)
                .build();

        Grant grant2 = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2017, MARCH, 11).getTime())
                .amount(new BigDecimal("600"))
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
                .delivery(new GregorianCalendar(2017, MARCH, 11).getTime())
                .amount(new BigDecimal("600"))
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
                        "violation-too-many-grants-for-aphakia-or-special")));

        // More than two and above grantable amount. is not ok.
        Grant grant4 = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2017, MARCH, 11).getTime())
                .amount(new BigDecimal("700"))
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
                .delivery(new GregorianCalendar(2017, MARCH, 10).getTime())
                .amount(new BigDecimal("2400"))
                .area("14", "96")
                .beneficiary(beneficiary)
                .prescription(prescription)
                .build();

        Grant grant2 = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2017, MARCH, 11).getTime())
                .amount(new BigDecimal("1200"))
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
                .delivery(new GregorianCalendar(2017, MARCH, 11).getTime())
                .amount(new BigDecimal("1200"))
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
                        "violation-too-many-grants-for-aphakia-or-special")));

        // More than two and above grantable amount. is not ok.
        Grant grant4 = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2017, MARCH, 11).getTime())
                .amount(new BigDecimal("1300"))
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
}
