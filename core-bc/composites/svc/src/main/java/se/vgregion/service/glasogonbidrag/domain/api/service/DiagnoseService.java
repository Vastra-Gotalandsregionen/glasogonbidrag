package se.vgregion.service.glasogonbidrag.domain.api.service;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Diagnose;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public interface DiagnoseService {
    void create(Diagnose diagnose);

    void update(Diagnose diagnose);

    void delete(Long id);
}
