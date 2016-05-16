package se.vgregion.service.glasogonbidrag.api.data;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Beneficiary;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Identification;

import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public interface BeneficiaryRepository {
    Beneficiary find(Long id);

    Beneficiary findWithParts(Long id);

    Beneficiary findWithPartsByIdent(Identification identification);

    List<Beneficiary> findAll();
}
