package se.vgregion.service.glasogonbidrag.domain.api.service;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Beneficiary;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Identification;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Prescription;
import se.vgregion.service.glasogonbidrag.domain.exception.NoIdentificationException;

import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public interface BeneficiaryService {
    void create(Beneficiary beneficiary) throws NoIdentificationException;

    Beneficiary update(Beneficiary beneficiary)
            throws NoIdentificationException;

    Beneficiary updateAddPrescription(
            long userId, long groupId, long companyId,
            Beneficiary beneficiary, Prescription prescription);

    void delete(Long id);

    Beneficiary find(Long id);

    Beneficiary findWithParts(Long id);

    Beneficiary findWithPartsByIdent(Identification identification);

    List<Beneficiary> findAll();

    List<Beneficiary> findAllWithParts();
}
