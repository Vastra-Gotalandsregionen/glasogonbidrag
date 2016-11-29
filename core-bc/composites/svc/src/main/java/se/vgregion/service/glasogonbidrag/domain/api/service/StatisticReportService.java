package se.vgregion.service.glasogonbidrag.domain.api.service;

import se.vgregion.service.glasogonbidrag.types.StatisticSearchRequest;
import se.vgregion.service.glasogonbidrag.types.StatisticSearchResponse;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public interface StatisticReportService {
    StatisticSearchResponse search(StatisticSearchRequest request);
}
