package se.vgregion.service.glasogonbidrag.domain.api.service;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Identification;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public interface IdentificationService {
    void create(Identification identification);

    void update(Identification identification);

    void delete(Long id);
}
