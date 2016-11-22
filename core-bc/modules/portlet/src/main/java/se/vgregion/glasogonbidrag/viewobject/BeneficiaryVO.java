package se.vgregion.glasogonbidrag.viewobject;

import java.util.Date;

/**
 * @author Erik Andersson - Monator Technologies AB
 */
public class BeneficiaryVO {

    private String identificationNumber;
    private Date dateOfOBirth;
    private String fullName;

    public BeneficiaryVO() {}

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public Date getDateOfOBirth() {
        return dateOfOBirth;
    }

    public void setDateOfOBirth(Date dateOfOBirth) {
        this.dateOfOBirth = dateOfOBirth;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
