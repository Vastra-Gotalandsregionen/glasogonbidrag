package se.vgregion.service.glasogonbidrag.helper;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Beneficiary;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Grant;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Prescription;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class GrantBuilder {
    private Date deliveryDate;
    private BigDecimal krona;
    private String county;
    private String municipality;
    private Beneficiary beneficiary;
    private Prescription prescription;

    public GrantBuilder delivery(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
        return this;
    }

    public GrantBuilder delivery(int year, int month, int dayOfMonth) {
        this.deliveryDate =
                new GregorianCalendar(year, month, dayOfMonth).getTime();
        return this;
    }

    public GrantBuilder amount(BigDecimal krona) {
        this.krona = krona;
        return this;
    }

    public GrantBuilder area(String county, String municipality) {
        this.county = county;
        this.municipality = municipality;
        return this;
    }

    public PrescriptionBuilder prescription() {
        return new PrescriptionBuilder(this);
    }

    public GrantBuilder prescription(Prescription prescription) {
        this.prescription = prescription;
        return this;
    }

    public BeneficiaryBuilder beneficiary() {
        return BeneficiaryFactory.newBeneficiary(this);
    }

    public GrantBuilder beneficiary(Beneficiary beneficiary) {
        this.beneficiary = beneficiary;
        return this;
    }

    public Grant build() {
        Calendar cal = new GregorianCalendar();
        Date cur = cal.getTime();

        Grant grant = new Grant();
        grant.setId(0L);
        grant.setUserId(0L);
        grant.setGroupId(0L);
        grant.setCompanyId(0L);
        grant.setCreateDate(cur);
        grant.setModifiedDate(cur);

        grant.setDeliveryDate(deliveryDate);
        grant.setAmountAsKrona(krona);
        grant.setCounty(county);
        grant.setMunicipality(municipality);

        grant.setBeneficiary(beneficiary);
        grant.setPrescription(prescription);

        if (!beneficiary.getPrescriptionHistory().contains(prescription)) {
            beneficiary.getPrescriptionHistory().add(prescription);
        }

        if (prescription.getBeneficiary() == null ||
                prescription.getBeneficiary() != beneficiary) {
            prescription.setBeneficiary(beneficiary);
        }

        prescription.setGrant(grant);

        beneficiary.getGrants().add(grant);

        return grant;
    }
}
