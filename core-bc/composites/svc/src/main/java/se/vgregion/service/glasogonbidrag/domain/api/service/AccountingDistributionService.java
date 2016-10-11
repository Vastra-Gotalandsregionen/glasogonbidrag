package se.vgregion.service.glasogonbidrag.domain.api.service;

import se.vgregion.portal.glasogonbidrag.domain.jpa.AccountingDistribution;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Beneficiary;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Prescription;
import se.vgregion.service.glasogonbidrag.domain.exception.NoIdentificationException;

import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public interface AccountingDistributionService {

    AccountingDistribution find(long id);

    AccountingDistribution findWithParts(long id);

    List<AccountingDistribution> findAll();

    List<AccountingDistribution> findAll(int firstResult, int maxResults);

    List<AccountingDistribution> findAllWithParts();

    List<AccountingDistribution> findAllWithParts(int firstResult,
                                                  int maxResults);
}
