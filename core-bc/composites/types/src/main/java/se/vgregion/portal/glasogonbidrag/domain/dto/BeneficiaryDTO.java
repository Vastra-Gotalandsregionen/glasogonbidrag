package se.vgregion.portal.glasogonbidrag.domain.dto;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Beneficiary;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class BeneficiaryDTO {
    private final long id;

    private final String number;
    private final String firstName;
    private final String lastName;
    private final long count;

    private final Beneficiary beneficiary;

    public BeneficiaryDTO(long id,
                          String number,
                          String firstName,
                          String lastName,
                          long count) {
        this.id = id;
        this.number = number;
        this.firstName = firstName;
        this.lastName = lastName;
        this.count = count;

        this.beneficiary = null;
    }

    public BeneficiaryDTO(Beneficiary beneficiary) {
        this.beneficiary = beneficiary;
        this.id = beneficiary.getId();
        this.number = beneficiary.getIdentification().getNumber();
        this.firstName = beneficiary.getFirstName();
        this.lastName = beneficiary.getLastName();
        this.count = beneficiary.getGrants().size();
    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return String.format("%s %s", firstName, lastName);
    }

    public long getCount() {
        return count;
    }

    public Beneficiary getBeneficiary() {
        return beneficiary;
    }
}
