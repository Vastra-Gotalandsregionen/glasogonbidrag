package se.vgregion.service.glasogonbidrag.domain.api.service;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Beneficiary;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Prescription;
import se.vgregion.service.glasogonbidrag.domain.exception.NoIdentificationException;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public interface BeneficiaryService {
    void create(Beneficiary beneficiary) throws NoIdentificationException;

    void update(Beneficiary beneficiary) throws NoIdentificationException;

    void updateAddPrescription(long userId, long groupId, long companyId,
                               Beneficiary beneficiary,
                               Prescription prescription);

    void delete(Long id);
}
