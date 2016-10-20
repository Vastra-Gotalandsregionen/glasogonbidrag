package se.vgregion.portal.glasogonbidrag.domain;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public enum VisualLaterality {
    BILATERAL("visual-laterality-bilateral"),
    RIGHT("visual-laterality-right"),
    LEFT("visual-laterality-left"),
    NONE("visual-laterality-none");

    private String languageKey;

    private VisualLaterality(String languageKey) {
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
