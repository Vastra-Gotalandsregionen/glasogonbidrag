package se.vgregion.service.glasogonbidrag.domain.api.service;

import se.vgregion.portal.glasogonbidrag.domain.dto.BeneficiaryDTO;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Beneficiary;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Identification;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Prescription;
import se.vgregion.service.glasogonbidrag.domain.exception.NoIdentificationException;
import se.vgregion.service.glasogonbidrag.types.BeneficiaryIdentificationTuple;
import se.vgregion.service.glasogonbidrag.types.filter.BeneficiaryFilter;
import se.vgregion.service.glasogonbidrag.types.filter.BeneficiaryOrder;

import java.util.Date;
import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public interface BeneficiaryService {
    void create(Beneficiary beneficiary) throws NoIdentificationException;

    void create(Beneficiary beneficiary,
                Prescription prescription,
                long userId,
                long groupId,
                long companyId,
                Date date)
            throws NoIdentificationException;

    BeneficiaryIdentificationTuple update(Beneficiary beneficiary)
            throws NoIdentificationException;

    void delete(Long id);

    void delete(Long id, boolean removeAll);

    Beneficiary find(Long id);

    Beneficiary findWithParts(Long id);

    Beneficiary findWithPartsByIdent(Identification identification);

    List<Beneficiary> findAll();

    List<Beneficiary> findAllWithParts();

    // Methods with filters

    List<BeneficiaryDTO> findAllFiltered(int firstResults,
                                         int maxResult,
                                         BeneficiaryFilter filters,
                                         BeneficiaryOrder order);

    int countFiltered(BeneficiaryFilter filters);

}
