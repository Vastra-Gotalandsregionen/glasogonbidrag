package se.vgregion.service.glasogonbidrag.local.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.vgregion.portal.glasogonbidrag.domain.DiagnoseType;
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
import se.vgregion.service.glasogonbidrag.local.api.RegionResponsibilityLookupService;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

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

    @Override
    public AccountingDistribution calculateFrom(Invoice invoice) {
        List<Grant> grants = invoice.getGrants();

        LOGGER.info("IS IT NULL? {}", grants == null ? "YES!" : "NO.");

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
        String municipality = grant.getMunicipality();
        Identification ident = beneficiary.getIdentification();
        Prescription prescription = grant.getPrescription();

        Diagnose diagnose = null;
        if (prescription != null) {
            diagnose = prescription.getDiagnose();
        }

        if (ident.getType() == IdentificationType.PROTECTED) {
            return 30120;
        }

        // TODO: Should this be the code for all Diagnose Types?
        if (diagnose != null && diagnose.getType() == DiagnoseType.NONE
                && ident.getType() == IdentificationType.LMA) {
            return 30220;
        }

        return regionLookupService.lookupResponsibility(municipality);
    }

    private int lookupFreeCode(Grant grant) {
        Beneficiary beneficiary = grant.getBeneficiary();

        int freeCode = -1;

        Calendar cal = new GregorianCalendar();
        int age = beneficiary.calculateAge(cal.getTime());

        if (0 <= age && age <= 18 ) {
            freeCode = 9191;
        }

        return freeCode;
    }
}
