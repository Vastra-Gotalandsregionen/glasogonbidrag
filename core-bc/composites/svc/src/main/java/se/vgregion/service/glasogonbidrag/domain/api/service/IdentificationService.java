package se.vgregion.service.glasogonbidrag.domain.api.service;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Identification;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public interface IdentificationService {
    void create(Identification identification);

    Identification update(Identification identification);

    void delete(Long id);

    Identification find(Long id);

    Identification findByPersonalNumber(String number);

    Identification findByLMANumber(String number);
}
