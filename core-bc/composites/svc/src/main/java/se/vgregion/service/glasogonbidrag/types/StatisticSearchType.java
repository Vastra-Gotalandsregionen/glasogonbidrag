package se.vgregion.service.glasogonbidrag.types;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public enum StatisticSearchType {
    MUNICIPALITY("type_mun"),
    BIRTH_YEAR("type_birth"),
    SEX("type_sex"),
    GRANT_TYPE("type_grant");

    private final String letter;

    private StatisticSearchType(String letter) {
        this.letter = letter;
    }

    public String getLetter() {
        return letter;
    }

    public StatisticSearchType parse(String letter) {
        for (StatisticSearchType statisticSearchType : values()) {
            if (statisticSearchType.getLetter().equals(letter)) {
                return statisticSearchType;
            }
        }
        throw new IllegalArgumentException("No such search type.");
    }
}
