package se.vgregion.portal.glasogonbidrag.domain;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public enum InvoiceStatus {
    IN_PROGRESS("invoice-status-in-progress"),
    COMPLETED("invoice-status-completed"),
    CANCELED("invoice-status-canceled"),
    REPLACED("invoice-status-replaced");

    private String languageKey;

    InvoiceStatus(String languageKey) {
        this.languageKey = languageKey;
    }

    public String getLanguageKey() {
        return languageKey;
    }

    @Override
    public String toString() {
        return languageKey;
    }

    public static InvoiceStatus parse(String string) {
        for (InvoiceStatus invoiceStatus : values()) {
            if (invoiceStatus.getLanguageKey().equals(string)) {
                return invoiceStatus;
            }
        }

        throw new IllegalArgumentException("Couldn't parse string " + string);
    }
}
