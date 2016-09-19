package se.vgregion.portal.glasogonbidrag.domain;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public enum DiagnoseType {
    APHAKIA("diagnose-type-aphakia"),
    KERATOCONUS("diagnose-type-keratoconus"),
    SPECIAL("diagnose-type-special"),
    NONE("diagnose-type-other");

    private String languageKey;

    DiagnoseType(String languageKey) {
        this.languageKey = languageKey;
    }

    public String getLanguageKey() {
        return languageKey;
    }

//    @Override
//    public String toString() {
//        return languageKey;
//    }

}
