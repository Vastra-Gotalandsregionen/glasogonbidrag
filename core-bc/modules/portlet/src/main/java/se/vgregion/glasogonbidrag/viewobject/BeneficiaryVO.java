package se.vgregion.glasogonbidrag.viewobject;

import se.vgregion.portal.glasogonbidrag.domain.DiagnoseType;
import se.vgregion.portal.glasogonbidrag.domain.VisualLaterality;

import java.util.Date;

/**
 * @author Erik Andersson - Monator Technologies AB
 */
public class BeneficiaryVO {

    private String identificationNumber;
    private String dateOfOBirth;
    private String firstName;
    private String surName;

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }
}
