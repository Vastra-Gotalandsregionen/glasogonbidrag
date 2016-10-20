package se.vgregion.service.glasogonbidrag.local.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.vgregion.portal.glasogonbidrag.domain.IdentificationType;
import se.vgregion.portal.glasogonbidrag.domain.jpa.AccountRow;
import se.vgregion.portal.glasogonbidrag.domain.jpa.AccountingDistribution;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Beneficiary;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Diagnose;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Grant;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Identification;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Prescription;
import se.vgregion.service.glasogonbidrag.local.api.AccountingDistributionCalculationService;
import se.vgregion.service.glasogonbidrag.local.api.AreaCodeLookupService;
import se.vgregion.service.glasogonbidrag.local.api.RegionResponsibilityLookupService;
import se.vgregion.service.glasogonbidrag.local.exception.FreeCodeNotFoundException;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Service
public class AccountingDistributionCalculationServiceImpl
        implements AccountingDistributionCalculationService {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(
                    AccountingDistributionCalculationServiceImpl.class);

    private static final int ACCOUNT = 5832;

    @Autowired
    private RegionResponsibilityLookupService regionLookupService;

    @Autowired
    private AreaCodeLookupService areaCodeLookupService;

    @Override
    public AccountingDistribution calculateFrom(Invoice invoice) {
        LOGGER.info("Generating distribution for {}", invoice);

        Set<Grant> grants = invoice.getGrants();

        AccountingDistribution distribution = new AccountingDistribution();

        for (Grant grant : grants) {
            int responsibility = lookupResponsibility(grant);
            int account = AccountingDistributionCalculationServiceImpl.ACCOUNT;
            int freeCode = lookupFreeCode(grant);
            long amount = grant.getAmount();

            LOGGER.info("Adding grant for {}, {} {} and {}",
                    responsibility, account, freeCode, amount);

            distribution.appendRow(
                    new AccountRow(responsibility,
                            account, freeCode, amount));
        }

        return distribution;
    }

    private int lookupResponsibility(Grant grant) {
        Beneficiary beneficiary = grant.getBeneficiary();
        String countyCode = grant.getCounty();
        String municipalityCode = grant.getMunicipality();
        Identification ident = beneficiary.getIdentification();

        String municipality = areaCodeLookupService.lookupMunicipalityFromCode(
                countyCode.concat(municipalityCode));

        if (ident.getType() == IdentificationType.PROTECTED) {
            return 30120;
        }

        if (ident.getType() == IdentificationType.LMA ||
                ident.getType() == IdentificationType.RESERVE ||
                ident.getType() == IdentificationType.OTHER) {
            return 30220;
        }

        return regionLookupService.lookupResponsibility(municipality);
    }

    private int lookupFreeCode(Grant grant) {
        Calendar cal = new GregorianCalendar();

        Beneficiary beneficiary = grant.getBeneficiary();
        Prescription prescription = grant.getPrescription();

        Diagnose diagnose;
        if (prescription != null) {
            diagnose = prescription.getDiagnose();
        } else {
            throw new IllegalStateException("Diagnose may not be null!");
        }

        int age;
        if (beneficiary != null && beneficiary.getIdentification() != null) {
            Date birthDay = beneficiary.getIdentification().getBirthDate();
            Date prescriptionDate = grant.getPrescription().getDate();
            age = calculateYearsBetween(birthDay, prescriptionDate);
        } else {
            throw new IllegalStateException(
                    "Beneficiary and it's Identification may not be null!");
        }

        int freeCode;

        switch (diagnose.getType()) {
            case APHAKIA:
                freeCode = 9586;
                break;

            case KERATOCONUS:
                freeCode = 9587;
                break;

            case SPECIAL:
                freeCode = 9588;
                break;

            case NONE:
                freeCode = lookupNoneDiagnoseFreeCode(age);
                break;

            default:
                throw new FreeCodeNotFoundException(
                        "Diagnose is set to unknown type, " +
                                "only: APHAKIA, KERATOCONUS, SPECIAL and " +
                                "NONE are supported!");
        }

        return freeCode;
    }

    private int lookupNoneDiagnoseFreeCode(int age) {
        if (0 <= age && age <= 7) {
            return 9589;
        } else if (8 <= age && age <= 19) {
            return 9802;
        } else {
            throw new FreeCodeNotFoundException(
                    "None diagnose but age is in not between 0 to 19!");
        }
    }

    private int calculateYearsBetween(Date first, Date last) {
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);

        int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
        if (a.get(Calendar.MONTH) > b.get(Calendar.MONTH)
                || (a.get(Calendar.MONTH) == b.get(Calendar.MONTH)
                && a.get(Calendar.DAY_OF_MONTH) >
                    b.get(Calendar.DAY_OF_MONTH))) {
            diff = diff - 1;
        }

        return diff;
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        return cal;
    }
}
