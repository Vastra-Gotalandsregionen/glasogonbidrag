package se.vgregion.service.glasogonbidrag.api.data;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Identification;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public interface IdentificationRepository {
    Identification find(Long id);

    Identification findByPersonalNumber(String number);

    Identification findByLMANumber(String number);
}
