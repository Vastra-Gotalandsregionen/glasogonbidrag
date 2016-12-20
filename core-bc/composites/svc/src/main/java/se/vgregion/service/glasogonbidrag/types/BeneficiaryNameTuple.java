package se.vgregion.service.glasogonbidrag.types;

import se.vgregion.portal.glasogonbidrag.domain.SexType;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class BeneficiaryNameTuple {
    private String fullName;
    private SexType sex;
    private boolean protectedNumber;

    public BeneficiaryNameTuple() {
        this("", SexType.UNKNOWN);
    }

    public BeneficiaryNameTuple(String fullName) {
        this(fullName, SexType.UNKNOWN);
    }

    public BeneficiaryNameTuple(String fullName, SexType sex) {
        this(fullName, sex, false);
    }

    public BeneficiaryNameTuple(String fullName, boolean protectedNumber) {
        this(fullName, SexType.UNKNOWN, protectedNumber);
    }

    public BeneficiaryNameTuple(String fullName, SexType sex, boolean protectedNumber) {
        this.fullName = fullName;
        this.sex = sex;
        this.protectedNumber = protectedNumber;
    }


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public SexType getSex() {
        return sex;
    }

    public void setSex(SexType sex) {
        this.sex = sex;
    }

    public boolean isProtectedNumber() {
        return protectedNumber;
    }

    public void setProtectedNumber(boolean protectedNumber) {
        this.protectedNumber = protectedNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BeneficiaryNameTuple that = (BeneficiaryNameTuple) o;

        if (fullName != null ? !fullName.equals(that.fullName) : that.fullName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return fullName != null ? fullName.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "BeneficiaryNameTuple{" +
                "fullName='" + fullName + '\'' +
                ",sex='" + sex.getKey() + '\'' +
                '}';
    }
}
