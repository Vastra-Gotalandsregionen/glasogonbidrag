package se.vgregion.service.glasogonbidrag.types;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Beneficiary;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Identification;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class BeneficiaryIdentificationTuple {
    private final Beneficiary beneficiary;
    private final Identification identification;

    public BeneficiaryIdentificationTuple(Beneficiary beneficiary,
                                          Identification identification) {
        this.beneficiary = beneficiary;
        this.identification = identification;
    }

    public Beneficiary getBeneficiary() {
        return beneficiary;
    }

    public Identification getIdentification() {
            return identification;
        }
}
