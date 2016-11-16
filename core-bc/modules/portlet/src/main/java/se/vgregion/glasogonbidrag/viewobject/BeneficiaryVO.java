package se.vgregion.glasogonbidrag.viewobject;

/**
 * @author Erik Andersson - Monator Technologies AB
 */
public class BeneficiaryVO {

    private String identificationNumber;
    private String dateOfOBirth;
    private String fullName;

    public BeneficiaryVO() {}

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getDateOfOBirth() {
        return dateOfOBirth;
    }

    public void setDateOfOBirth(String dateOfOBirth) {
        this.dateOfOBirth = dateOfOBirth;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
