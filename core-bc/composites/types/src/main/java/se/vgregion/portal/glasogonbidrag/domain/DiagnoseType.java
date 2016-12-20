package se.vgregion.portal.glasogonbidrag.domain;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Diagnose;
import se.vgregion.portal.glasogonbidrag.domain.jpa.diagnose.Aphakia;
import se.vgregion.portal.glasogonbidrag.domain.jpa.diagnose.Keratoconus;
import se.vgregion.portal.glasogonbidrag.domain.jpa.diagnose.None;
import se.vgregion.portal.glasogonbidrag.domain.jpa.diagnose.Special;

/**
 * @author Martin Lind - Monator Technologies AB
 */
// TODO: rename NONE to CHILD
public enum DiagnoseType {
    APHAKIA("diagnose-type-aphakia", Aphakia.class),
    KERATOCONUS("diagnose-type-keratoconus", Keratoconus.class),
    SPECIAL("diagnose-type-special", Special.class),
    NONE("diagnose-type-child", None.class);

    private String languageKey;
    private Class<? extends Diagnose> diagnoseClass;

    DiagnoseType(String languageKey, Class<? extends Diagnose> diagnoseClass) {
        this.languageKey = languageKey;
        this.diagnoseClass = diagnoseClass;
    }

    public String getLanguageKey() {
        return languageKey;
    }

    public Class<? extends Diagnose> getDiagnoseClass() {
        return diagnoseClass;
    }

    public static DiagnoseType parse(String string) {
        for (DiagnoseType diagnoseType : values()) {
            if (diagnoseType.getLanguageKey().equals(string)) {
                return diagnoseType;
            }
        }

        throw new IllegalArgumentException(
                "Didn't find diagnose for language key " + string);
    }
}
