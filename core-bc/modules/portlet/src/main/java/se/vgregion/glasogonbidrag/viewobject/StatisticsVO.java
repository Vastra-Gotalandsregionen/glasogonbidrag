package se.vgregion.glasogonbidrag.viewobject;

import se.vgregion.portal.glasogonbidrag.domain.internal.KronaCalculationUtil;

import java.math.BigDecimal;

/**
 * @author Erik Andersson - Monator Technologies AB
 */
public class StatisticsVO {

    // Attributes

    private String label;
    private int numberOfGrants;
    private long grantsSum;

    // Helpers
    private final KronaCalculationUtil currency =
            new KronaCalculationUtil();

    // Constructors

    public StatisticsVO() {}

    public StatisticsVO(String label, int numberOfGrants, long grantsSum) {
        this.label = label;
        this.numberOfGrants = numberOfGrants;
        this.grantsSum = grantsSum;
    }

    // Getters and Setters

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getNumberOfGrants() {
        return numberOfGrants;
    }

    public void setNumberOfGrants(int numberOfGrants) {
        this.numberOfGrants = numberOfGrants;
    }

    public long getGrantsSum() {
        return grantsSum;
    }

    public void setGrantsSum(long grantsSum) {
        this.grantsSum = grantsSum;
    }

    // Helper classes
    public BigDecimal getGrantsSumAsKrona() {
        return currency.calculatePartsAsKrona(grantsSum);
    }
}
