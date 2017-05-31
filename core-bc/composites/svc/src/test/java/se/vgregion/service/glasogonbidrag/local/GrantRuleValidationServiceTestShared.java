package se.vgregion.service.glasogonbidrag.local;

/**
 * @author Martin Lind - Monator Technologies AB
 */

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
import se.vgregion.portal.glasogonbidrag.domain.jpa.diagnose.None;
import se.vgregion.service.glasogonbidrag.helper.BeneficiaryFactory;
import se.vgregion.service.glasogonbidrag.helper.GrantFactory;
import se.vgregion.service.glasogonbidrag.helper.PrescriptionFactory;
import se.vgregion.service.glasogonbidrag.local.api.GrantRuleValidationService;
import se.vgregion.service.glasogonbidrag.types.GrantRuleResult;
import se.vgregion.service.glasogonbidrag.types.GrantRuleViolation;
import se.vgregion.service.glasogonbidrag.types.GrantRuleWarning;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static java.util.Calendar.*;
import static java.util.Calendar.JANUARY;
import static java.util.Calendar.MAY;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:localTestContext.xml")
public class GrantRuleValidationServiceTestShared {

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
}