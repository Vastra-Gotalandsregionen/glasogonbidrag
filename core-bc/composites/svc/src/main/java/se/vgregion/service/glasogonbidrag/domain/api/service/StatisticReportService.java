package se.vgregion.service.glasogonbidrag.domain.api.service;

import se.vgregion.portal.glasogonbidrag.domain.dto.StatisticReportDTO;

import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public interface StatisticReportService {
    List<StatisticReportDTO> search();
}
