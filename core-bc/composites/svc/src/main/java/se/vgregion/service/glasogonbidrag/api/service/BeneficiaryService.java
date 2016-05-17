package se.vgregion.service.glasogonbidrag.api.service;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Beneficiary;
import se.vgregion.service.glasogonbidrag.exception.NoIdentificationException;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public interface BeneficiaryService {
    void create(Beneficiary beneficiary) throws NoIdentificationException;

    void update(Beneficiary beneficiary) throws NoIdentificationException;

    void delete(Long id);
}
