package se.vgregion.service.glasogonbidrag.local.api;

import se.vgregion.portal.glasogonbidrag.domain.dto.ImportDTO;

import java.util.Locale;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public interface GrantImportService {
    ImportDTO importData(byte[] data, String currentYear, Locale locale);
}
