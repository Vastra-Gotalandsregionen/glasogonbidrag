package se.vgregion.portal.glasogonbidrag.domain;

import se.vgregion.portal.glasogonbidrag.domain.jpa.identification.Personal;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public enum IdentificationType {
    PERSONAL("identification-type-personal"),
    RESERVE("identification-type-reserve"),
    LMA("identification-type-lma"),
    OTHER("identification-type-other"),
    PROTECTED("identification-type-protected"),
    NONE("identification-type-none");

    IdentificationType(String languageKey) {
        this.languageKey = languageKey;
    }

    private String languageKey;

    public String getLanguageKey() {
        return languageKey;
    }

}
