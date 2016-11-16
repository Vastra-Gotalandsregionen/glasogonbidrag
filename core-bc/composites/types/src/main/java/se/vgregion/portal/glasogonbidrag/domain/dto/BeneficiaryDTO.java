package se.vgregion.portal.glasogonbidrag.domain.dto;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Beneficiary;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class BeneficiaryDTO {
    private final long id;

    private final String number;
    private final String fullName;
    private final long count;

    private final Beneficiary beneficiary;

    public BeneficiaryDTO(long id,
                          String number,
                          String fullName,
                          long count) {
        this.id = id;
        this.number = number;
        this.fullName = fullName;
        this.count = count;

        this.beneficiary = null;
    }

    public BeneficiaryDTO(Beneficiary beneficiary) {
        this.beneficiary = beneficiary;
        this.id = beneficiary.getId();
        this.number = beneficiary.getIdentification().getNumber();
        this.fullName = beneficiary.getFullName();
        this.count = beneficiary.getGrants().size();
    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public String getFullName() {
        return fullName;
    }

    public long getCount() {
        return count;
    }

    public Beneficiary getBeneficiary() {
        return beneficiary;
    }
}
