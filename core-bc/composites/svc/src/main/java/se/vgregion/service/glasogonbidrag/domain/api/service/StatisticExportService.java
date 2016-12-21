package se.vgregion.service.glasogonbidrag.domain.api.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 *
 * Export the following data:
 *  - Amount
 *  - Sex
 *  - Grant type
 *  - Birthday
 *  - Recipe date
 *  - delivery date
 *  - Create date
 *  - County
 *  - municipality
 *  - Responsibility
 *  - Free code
 *
 * @author Martin Lind - Monator Technologies AB
 */
public interface StatisticExportService {
    byte[] export(Date start, Date end, Map<String, String> localization)
            throws IOException;
}
