package se.vgregion.service.glasogonbidrag.types;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class BeneficiaryNameTuple {
    private String fullName;

    public BeneficiaryNameTuple() {
    }

    public BeneficiaryNameTuple(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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
                '}';
    }
}
