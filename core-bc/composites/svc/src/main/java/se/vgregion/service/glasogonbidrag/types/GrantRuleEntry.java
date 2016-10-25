package se.vgregion.service.glasogonbidrag.types;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public abstract class GrantRuleEntry {
    protected String errorCode;

    public GrantRuleEntry(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GrantRuleEntry that = (GrantRuleEntry) o;

        if (!errorCode.equals(that.errorCode)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return errorCode.hashCode();
    }
}
