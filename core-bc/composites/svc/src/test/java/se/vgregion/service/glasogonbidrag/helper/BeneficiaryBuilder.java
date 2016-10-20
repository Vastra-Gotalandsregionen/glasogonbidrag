package se.vgregion.service.glasogonbidrag.helper;

import org.slf4j.Logger;
import se.vgregion.portal.glasogonbidrag.domain.IdentificationType;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Beneficiary;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Grant;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Identification;
import se.vgregion.portal.glasogonbidrag.domain.jpa.identification.Personal;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class BeneficiaryBuilder {
    private boolean hasParent;
    private GrantBuilder parent;

    private String firstName;
    private String lastName;

    private Identification identification;

    BeneficiaryBuilder() {
        hasParent = false;
    }

    BeneficiaryBuilder(GrantBuilder parent) {
        hasParent = true;
        this.parent = parent;
    }

    BeneficiaryBuilder(String firstName, String lastName) {
        hasParent = false;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public BeneficiaryBuilder firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public BeneficiaryBuilder lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public BeneficiaryBuilder identification(String number) {
        identification = new Personal(number);
        identification.setId(0L);
        return this;
    }

    public Beneficiary build() {
        if (hasParent) throw new IllegalStateException();
        if (validate()) return _build();
        else throw new IllegalStateException("Validation failed.");
    }

    public GrantBuilder end() {
        if (!hasParent) throw new IllegalStateException();
        if (validate()) return parent.beneficiary(_build());
        else throw new IllegalStateException("Validation failed.");
    }

    private boolean validate() {
        return firstName != null
                && lastName != null
                && identification != null;
    }

    private Beneficiary _build() {
        Calendar cal = new GregorianCalendar();
        Date cur = cal.getTime();

        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setId(0L);
        beneficiary.setCreateDate(cur);
        beneficiary.setModifiedDate(cur);

        beneficiary.setFirstName(firstName);
        beneficiary.setLastName(lastName);
        beneficiary.setIdentification(identification);

        return beneficiary;
    }
}
