package se.vgregion.portal.glasogonbidrag.domain;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public enum InvoiceStatus {
    PAID("invoice-status-paid"),
    UNPAID("invoice-status-unpaid"),
    CANCELED("invoice-status-canceled");

    private String languageKey;

    InvoiceStatus(String languageKey) {
        this.languageKey = languageKey;
    }

    @Override
    public String toString() {
        return languageKey;
    }
}
