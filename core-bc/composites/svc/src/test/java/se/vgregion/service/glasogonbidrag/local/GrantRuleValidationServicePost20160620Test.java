package se.vgregion.service.glasogonbidrag.local;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.vgregion.portal.glasogonbidrag.domain.VisualLaterality;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Grant;
import se.vgregion.portal.glasogonbidrag.domain.jpa.diagnose.Aphakia;
import se.vgregion.service.glasogonbidrag.helper.GrantFactory;
import se.vgregion.service.glasogonbidrag.local.api.GrantRuleValidationService;
import se.vgregion.service.glasogonbidrag.types.GrantRuleResult;
import se.vgregion.service.glasogonbidrag.types.GrantRuleViolation;

import java.math.BigDecimal;
import java.util.GregorianCalendar;
import java.util.HashSet;

import static java.util.Calendar.MARCH;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:localTestContext.xml")
public class GrantRuleValidationServicePost20160620Test {

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


    @Test
    public void aphakiaLeftEyeOneGrant() {
        // Test grant is within grantable amount.
        Grant grant1OkLeftEye = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2017, MARCH, 2).getTime())
                .amount(new BigDecimal("1200"))
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
                .amount(new BigDecimal("1300"))
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
                        "violation-amount-greater-than-1200-" +
                        "for-aphakia-or-special-left-eye-post-20160620")));
    }

    @Test
    public void aphakiaRightEyeOneGrant() {
        // Test grant is within grantable amount.
        Grant grant1OkLeftEye = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2017, MARCH, 2).getTime())
                .amount(new BigDecimal("1200"))
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
                .amount(new BigDecimal("1300"))
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
                        "violation-amount-greater-than-1200-" +
                        "for-aphakia-or-special-right-eye-post-20160620")));
    }

    @Test
    public void aphakiaBothEyesOneGrant() {
        Grant grant1OkBothEyes = GrantFactory.newGrant()
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
                    .diagnose(new Aphakia(VisualLaterality.BILATERAL))
                    .prescriber("The doctor")
                .end()
                .build();

        GrantRuleResult result1 = validationService.test(
                grant1OkBothEyes, new HashSet<Grant>());
        Assert.assertFalse(result1.hasWarnings());
        Assert.assertTrue(result1.hasViolations());

        Grant grant1OverBothEyes = GrantFactory.newGrant()
                .delivery(new GregorianCalendar(2017, MARCH, 2).getTime())
                .amount(new BigDecimal("2500"))
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
                        "violation-amount-greater-than-2400-" +
                        "for-aphakia-or-special-both-eyes-post-20160620")));
    }
}
