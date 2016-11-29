package se.vgregion.service.glasogonbidrag.types;

import se.vgregion.portal.glasogonbidrag.domain.DiagnoseType;
import se.vgregion.portal.glasogonbidrag.domain.IdentificationType;
import se.vgregion.portal.glasogonbidrag.domain.SexType;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class StatisticSearchRequest {
    private StatisticSearchType type;
    private StatisticSearchDateInterval interval;

    private SexType sex;
    private IdentificationType identificationType;
    private DiagnoseType diagnoseType;

    private StatisticSearchIntegerInterval birthYearInterval;

    public StatisticSearchRequest() {}

    public StatisticSearchType getType() {
        return type;
    }

    public void setType(StatisticSearchType type) {
        this.type = type;
    }

    public StatisticSearchDateInterval getInterval() {
        return interval;
    }

    public void setInterval(StatisticSearchDateInterval interval) {
        this.interval = interval;
    }

    public SexType getSex() {
        return sex;
    }

    public void setSex(SexType sex) {
        this.sex = sex;
    }

    public IdentificationType getIdentificationType() {
        return identificationType;
    }

    public void setIdentificationType(IdentificationType identificationType) {
        this.identificationType = identificationType;
    }

    public DiagnoseType getDiagnoseType() {
        return diagnoseType;
    }

    public void setDiagnoseType(DiagnoseType diagnoseType) {
        this.diagnoseType = diagnoseType;
    }

    public StatisticSearchIntegerInterval getBirthYearInterval() {
        return birthYearInterval;
    }

    public void setBirthYearInterval(
            StatisticSearchIntegerInterval birthYearInterval) {
        this.birthYearInterval = birthYearInterval;
    }
}
