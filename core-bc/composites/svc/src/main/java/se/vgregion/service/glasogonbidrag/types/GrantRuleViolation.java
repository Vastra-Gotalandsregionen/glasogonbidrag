package se.vgregion.service.glasogonbidrag.types;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class GrantRuleViolation {
    private String errorCode;

    public GrantRuleViolation(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GrantRuleViolation that = (GrantRuleViolation) o;

        if (!errorCode.equals(that.errorCode)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return errorCode.hashCode();
    }
}
