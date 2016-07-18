package se.vgregion.service.glasogonbidrag.local.internal;

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

import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Service
public class AccountingDistributionCalculationServiceImpl
        implements AccountingDistributionCalculationService {

    private static final int ACCOUNT = 5832;

    @Override
    public AccountingDistribution calculateFrom(Invoice invoice) {
        List<Grant> grants = invoice.getGrants();

        AccountingDistribution distribution = new AccountingDistribution();

        for (Grant grant : grants) {
            int count = 1;
            int responsibility = lookupResponsibility(grant);
            int account = ACCOUNT;
            int freeCode = lookupFreeCode(grant);
            long amount = grant.getAmount();

            distribution.appendRow(
                    new AccountRow(
                            count, responsibility,
                            account, freeCode, amount));
        }

        return null;
    }

    private int lookupResponsibility(Grant grant) {
        Beneficiary beneficiary = grant.getBeneficiary();
        Identification ident = beneficiary.getIdentification();
        Prescription prescription = grant.getPrescription();
        Diagnose diagnose = prescription.getDiagnose();

        if (ident.getType() == IdentificationType.PROTECTED) {
            return 30120;
        }

        // TODO: Should this be the code for all Diagnose Types?
        if (diagnose.getType() == DiagnoseType.NONE
                && ident.getType() == IdentificationType.LMA) {
            return 30220;
        }

        return 0;
    }

    private int lookupFreeCode(Grant grant) {
        return 0;
    }
}
