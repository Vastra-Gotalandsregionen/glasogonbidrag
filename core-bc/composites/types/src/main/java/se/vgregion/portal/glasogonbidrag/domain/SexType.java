package se.vgregion.portal.glasogonbidrag.domain;

/**
 * @author Martin Lind - Monator Technologies
 */
public enum SexType {
    MALE("sex-male"),
    FEMALE("sex-female"),
    UNKNOWN("sex-unknown");

    private final String key;

    private SexType(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
