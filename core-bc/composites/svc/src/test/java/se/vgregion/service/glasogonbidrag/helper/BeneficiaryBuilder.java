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

    private String fullName;

    private Identification identification;

    BeneficiaryBuilder() {
        hasParent = false;
    }

    BeneficiaryBuilder(GrantBuilder parent) {
        hasParent = true;
        this.parent = parent;
    }

    BeneficiaryBuilder(String firstName, String lastName) {
        this(String.format("%s %s", firstName, lastName));
    }

    BeneficiaryBuilder(String fullName) {
        hasParent = false;
        this.fullName = fullName;
    }

    public BeneficiaryBuilder fullName(String fullName) {
        this.fullName = fullName;
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
        return fullName != null
                && identification != null;
    }

    private Beneficiary _build() {
        Calendar cal = new GregorianCalendar();
        Date cur = cal.getTime();

        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setId(0L);
        beneficiary.setCreateDate(cur);
        beneficiary.setModifiedDate(cur);

        beneficiary.setFullName(fullName);
        beneficiary.setIdentification(identification);

        return beneficiary;
    }
}
