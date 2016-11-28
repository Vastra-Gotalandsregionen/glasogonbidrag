package se.vgregion.service.glasogonbidrag.types;

import se.vgregion.portal.glasogonbidrag.domain.dto.StatisticReportDTO;

import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class StatisticSearchResponse {
    private final StatisticSearchType searchType;
    private final List<StatisticReportDTO> resultList;

    public StatisticSearchResponse(StatisticSearchType searchType,
                                   List<StatisticReportDTO> resultList) {
        this.resultList = resultList;
        this.searchType = searchType;
    }

    public StatisticSearchType getSearchType() {
        return searchType;
    }

    public List<StatisticReportDTO> getResultList() {
        return resultList;
    }
}
