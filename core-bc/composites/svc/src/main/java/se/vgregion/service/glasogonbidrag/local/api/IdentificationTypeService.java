package se.vgregion.service.glasogonbidrag.local.api;

import se.vgregion.portal.glasogonbidrag.domain.IdentificationType;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public interface IdentificationTypeService {
    IdentificationType detect(String number, String currentYear);
}
