package se.vgregion.service.glasogonbidrag.types;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Beneficiary;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Grant;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class BeneficiaryGrantTuple {
    private final Beneficiary beneficiary;
    private final Grant grant;

    public BeneficiaryGrantTuple(Beneficiary beneficiary, Grant grant) {
        this.beneficiary = beneficiary;
        this.grant = grant;
    }

    public Beneficiary getBeneficiary() {
        return beneficiary;
    }

    public Grant getGrant() {
        return grant;
    }
}
