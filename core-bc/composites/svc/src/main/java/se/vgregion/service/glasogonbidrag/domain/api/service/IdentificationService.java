package se.vgregion.service.glasogonbidrag.domain.api.service;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Identification;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public interface IdentificationService {

    // CRUD

    void create(Identification identification);

    Identification update(Identification identification);

    void delete(Long id);

    // CRUD - Finders

    Identification find(Long id);

    Identification findByNumber(String number);

    Identification findByPersonalNumber(String number);

    Identification findByLMANumber(String number);

    Identification findByReserveumber(String number);

    // Utils

    String generateUniqueIdentificationNumber();
}
