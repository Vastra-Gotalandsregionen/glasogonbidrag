package se.vgregion.service.glasogonbidrag.local.internal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.vgregion.portal.glasogonbidrag.domain.DiagnoseType;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Beneficiary;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Diagnose;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Grant;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Prescription;
import se.vgregion.service.glasogonbidrag.local.api.AreaCodeLookupService;
import se.vgregion.service.glasogonbidrag.local.api.GrantRuleValidationService;
import se.vgregion.service.glasogonbidrag.local.api.RegionResponsibilityLookupService;
import se.vgregion.service.glasogonbidrag.types.GrantRuleResult;
import se.vgregion.service.glasogonbidrag.types.GrantRuleViolation;
import se.vgregion.service.glasogonbidrag.types.GrantRuleWarning;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Service
public class GrantRuleValidationServiceImpl
        implements GrantRuleValidationService {

    private static Date PRE_20160301;
    private static Date PRE_20160620;

    static {
        PRE_20160301 = new GregorianCalendar(2016, 3, 1, 0, 0, 0).getTime();
        PRE_20160620 = new GregorianCalendar(2016, 6, 20, 0, 0, 0).getTime();
    }

    @Autowired
    private AreaCodeLookupService areaCodeLookupService;

    @Autowired
    private RegionResponsibilityLookupService responsibilityLookupService;

    /**
     * {@inheritDoc}
     */
    @Override
    public GrantRuleResult test(Grant grant) {
        GrantRuleResult result = new GrantRuleResult();

        // Fetch data we need
        Diagnose diagnose = grant.getPrescription().getDiagnose();
        String county = grant.getCounty();
        String municipality = grant.getMunicipality();
        Date deliveryDate = grant.getDeliveryDate();
        Date recipeDate = grant.getPrescription().getDate();
        Date birthDate = grant.getBeneficiary()
                .getIdentification().getBirthDate();

        // Fetch data from grant
        List<Grant> grants = fetchGrantsPerCalendarForLatestRecipe(grant);

        // Calculate data used by checks
        long totalAmount = calculateAmount(grants);

        // If one has more than one grant generate a warning!
        if (testMoreThanOneGrant(grants)) {
            result.add(new GrantRuleWarning("warning-more-than-one-grant"));
        }

        // The beneficiary have to live in Västra Götalands Regionen
        // to be eligible for a grant.
        if (!testGrantInVGRegion(county, municipality)) {
            result.add(new GrantRuleViolation("violation-not-in-vgr"));
        }


        if (diagnose.getType() == DiagnoseType.NONE) {
            // When a don't have a special kind of diagnosis this
            // block will validate this.

            // The beneficiary may use the recipe for at most 12 months.
            if (testDeliveryDateIs12MonthsAfterRecipeDate(
                    deliveryDate, recipeDate)) {
                result.add(new GrantRuleViolation(
                        "violation-" +
                                "delivery-date-is-12-month-" +
                                "after-recipe-date"));
            }

            // BEGIN "check recipe in age intervals"
            // REASON these tests are flawed, check if they should
            //        be used or not.
            // CODE BLOCK
            // // If the beneficiary is aged 0 to 7 the recipe needs
            // // to be prescribed before he or she became 8 years old.
            // if (0 <= age && age < 8) {
            //     if (!testRecipeDateBeforeAge8()) {
            //         result.add(new GrantRuleViolation());
            //     }
            // }
            //
            // // If the beneficiary is aged 8 to 15 the recipe needs
            // // to be prescribed before he or she became 16 years old.
            // if (deliveryDate.before(PRE_20160301) && 8 <= age && age < 16) {
            //     if (!testRecipeDateBetweenAge8and16()) {
            //         result.add(new GrantRuleViolation());
            //     }
            // }
            //
            // // If the beneficiary is aged 8 to 15 the recipe needs
            // // to be prescribed before he or she became 16 years old.
            // if (deliveryDate.after(PRE_20160301) && 8 <= age && age < 20) {
            //     if (!testRecipeDateBetweenAge8and20()) {
            //         result.add(new GrantRuleViolation());
            //     }
            // }
            // END

            if (deliveryDate.before(PRE_20160301)) {
                // For the old system a recipe must be granted before
                // the age of 16.
                if (!testRecipeDateBeforeAge16(recipeDate, null)) {
                    result.add(new GrantRuleViolation(
                            "violation-recipe-date-after-age-16-" +
                                    "pre-20160301"));
                }

                // For the old system a recipe need to be delivered
                // at most 6 months after one have come of age 16
                if (testDeliveryDateIs6MonthAfterAge16(recipeDate, null)) {
                    result.add(new GrantRuleViolation(
                            "violation-delivery-date-6-month-after-age-16-" +
                                    "pre-20160301"));
                }
            }

            // For both the new rules changed at 2016-03-01 and the newer that
            // rules changed at 2016-06-20
            if (!deliveryDate.before(PRE_20160301)) {
                // For the new and new new system a recipe must be granted
                // before the age of 20.
                if (!testRecipeDateBeforeAge20(recipeDate, null)) {
                    result.add(new GrantRuleViolation(
                            "violation-recipe-date-after-age-20-" +
                                    "post-20160301"));
                }

                // For the new and new new system a recipe need to be
                // delivered at most 6 months after one have come of age 20
                if (testDeliveryDateIs6MonthAfterAge20(recipeDate, null)) {
                    result.add(new GrantRuleViolation(
                            "violation-delivery-date-6-month-after-age-20-" +
                                    "post-20160301"));
                }
            }

            // For both old and new rules,
            // a beneficiary may max get 1000kr for ages 0 to 16.
            if (deliveryDate.before(PRE_20160620)) {
                // Check: 0 <= age < 16
                if (inRange(recipeDate, birthDate, addYears(birthDate, 16))) {
                    if (!testAmountLessThanOrEqual1000(totalAmount)) {
                        result.add(new GrantRuleViolation(
                                "violation-amount-greater-than-1000-" +
                                        "for-children-0-to-15-" +
                                        "pre-20160620"));
                    }
                }
            }

            // For new rules
            // a beneficiary of age 16 to 19 may get a max of 800kr
            if (!deliveryDate.before(PRE_20160301)
                    && deliveryDate.before(PRE_20160620)) {
                // Check: 16 <= age < 20
                if (inRange(recipeDate,
                        addYears(birthDate,16), addYears(birthDate, 20))) {
                    if (!testAmountLessThanOrEqual800(totalAmount)) {
                        result.add(new GrantRuleViolation(
                                "violation-amount-greater-than-800-" +
                                        "for-children-16-to-19-" +
                                        "post-20160301-and-pre-20160620"));
                    }
                }
            }

            // For "new new" rules
            // a beneficiary of age 0 to 19 may get a max of 1600kr
            if (!deliveryDate.before(PRE_20160620)) {
                // Check: 0 <= age < 20
                if (inRange(recipeDate, birthDate, addYears(birthDate, 20))) {
                    if (!testAmountLessThanOrEqual1600(totalAmount)) {
                        result.add(new GrantRuleViolation(
                                "violation-amount-greater-than-1600-" +
                                        "for-children-0-to-19-" +
                                        "post-20160620"));
                    }
                }
            }
        } else if (diagnose.getType() == DiagnoseType.APHAKIA
                || diagnose.getType() == DiagnoseType.SPECIAL) {
            // Beneficiaries with Aphakia or Special glasses or lens needs
            // may be granted 2000kr maximum or for the new new system
            // 2400kr.
            if (deliveryDate.before(PRE_20160620)) {
                if (!testAmountLessThanOrEqual2000(totalAmount)) {
                    result.add(new GrantRuleViolation(
                            "violation-amount-greater-than-2000-" +
                                    "for-aphakia-and-special-" +
                                    "pre-20160620"));
                }
            } else {
                if (!testAmountLessThanOrEqual2400(totalAmount)) {
                    result.add(new GrantRuleViolation(
                            "violation-amount-greater-than-2400-" +
                                    "for-aphakia-and-special-" +
                                    "post-20160620"));
                }
            }
        } else if (diagnose.getType() == DiagnoseType.KERATOCONUS) {
            // Beneficiaries with Keratoconus may be granted 2400kr maximum
            // or for the new new system 3000kr.
            if (deliveryDate.before(PRE_20160620)) {
                if (!testAmountLessThanOrEqual2400(totalAmount)) {
                    result.add(new GrantRuleViolation(
                            "violation-amount-greater-than-2400-" +
                                    "for-keratoconus-" +
                                    "pre-20160620"));
                }
            } else {
                if (!testAmountLessThanOrEqual3000(totalAmount)) {
                    result.add(new GrantRuleViolation(
                            "violation-amount-greater-than-3000-" +
                                    "for-keratoconus-" +
                                    "post-20160620"));
                }
            }
        }

        return result;
    }

    // Helpers

    public List<Grant> fetchGrantsPerCalendarForLatestRecipe(Grant grant) {
        Prescription prescription = grant.getPrescription();
        List<Grant> grants = prescription.getGrants();

        Calendar cal = new GregorianCalendar();
        cal.setTime(grant.getDeliveryDate());

        int suppliedYear = cal.get(Calendar.YEAR);

        List<Grant> calendarGrants = new ArrayList<>();
        for (Grant g : grants) {
            cal.setTime(g.getDeliveryDate());

            if (cal.get(Calendar.YEAR) == suppliedYear) {
                calendarGrants.add(g);
            }
        }

        return calendarGrants;
    }

    public int calculateBeneficiaryAge() {
        return 0;
    }

    public int calculateAmount(List<Grant> grants) {
        return 0;
    }

    public Calendar getCalendar(Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);

        return cal;
    }

    public Date addMonths(Date date, int months) {
        Calendar cal = getCalendar(date);
        cal.add(Calendar.MONTH, months);

        return cal.getTime();
    }

    public Date addYears(Date date, int years) {
        Calendar cal = getCalendar(date);
        cal.add(Calendar.YEAR, years);

        return cal.getTime();
    }

    public boolean inRange(Date date, Date start, Date end) {
        return inRange(date, start, end, true, false);
    }

    public boolean inRange(Date date,
                           Date start, Date end,
                           boolean includeStart, boolean includeEnd) {
        boolean startCondition =
                includeStart ? !date.before(start) : date.after(start);
        boolean endCondition =
                includeEnd ? !date.after(end) : date.before(end);
        return startCondition && endCondition;
    }

    // Tests

    private boolean testMoreThanOneGrant(List<Grant> grants) {
        return grants.size() > 1;
    }

    private boolean testGrantInVGRegion(String county, String municipality) {
        String municipalityName = areaCodeLookupService
                .lookupMunicipalityFromCode(county.concat(municipality));

        // lookupResponsibility returns 0 if the municipality name
        // is not located in one of the four counties.
        int responsibilityCode = responsibilityLookupService
                .lookupResponsibility(municipalityName);

        return responsibilityCode != 0;
    }

//    // PART OF "check recipe in age intervals" BLOCK.
//    public boolean testRecipeDateBeforeAge8(Date recipeDate,
//                                            Date dateOf8thBirthday) {
//        return recipeDate.before(dateOf8thBirthday);
//    }
//
//    public boolean testRecipeDateBetweenAge8and16(Date recipeDate,
//                                                  Date dateOf8thBirthday,
//                                                  Date dateOf16thBirthday) {
//        return !recipeDate.before(dateOf8thBirthday)
//                && recipeDate.before(dateOf16thBirthday);
//    }
//
//    public boolean testRecipeDateBetweenAge8and20(Date recipeDate,
//                                                  Date dateOf8thBirthday,
//                                                  Date dateOf20thBirthday) {
//        return !recipeDate.before(dateOf8thBirthday)
//                && recipeDate.before(dateOf20thBirthday);
//    }
//    // END

    public boolean testRecipeDateBeforeAge16(Date recipeDate,
                                             Date dateOf16thBirthday) {
        return recipeDate.before(dateOf16thBirthday);
    }

    public boolean testRecipeDateBeforeAge20(Date recipeDate,
                                             Date dateOf20thBirthday) {
        return recipeDate.before(dateOf20thBirthday);
    }

    public boolean testDeliveryDateIs6MonthAfterAge16(
            Date deliveryDate, Date dateOf16thBirthday) {
        return deliveryDate.after(addMonths(dateOf16thBirthday, 6));
    }

    public boolean testDeliveryDateIs6MonthAfterAge20(
            Date deliveryDate, Date dateOf20thBirthday) {
        return deliveryDate.after(addMonths(dateOf20thBirthday, 6));
    }

    public boolean testDeliveryDateIs12MonthsAfterRecipeDate(
            Date deliveryDate, Date recipeDate) {
        return deliveryDate.after(addMonths(recipeDate, 12));
    }

    public boolean testAmountLessThanOrEqual800(long totalAmount) {
        return totalAmount <= 800;
    }

    public boolean testAmountLessThanOrEqual1000(long totalAmount) {
        return totalAmount <= 1000;
    }

    public boolean testAmountLessThanOrEqual1600(long totalAmount) {
        return totalAmount <= 1600;
    }

    public boolean testAmountLessThanOrEqual2000(long totalAmount) {
        return totalAmount <= 2000;
    }

    public boolean testAmountLessThanOrEqual2400(long totalAmount) {
        return totalAmount <= 2400;
    }

    public boolean testAmountLessThanOrEqual3000(long totalAmount) {
        return totalAmount <= 3000;
    }
}
