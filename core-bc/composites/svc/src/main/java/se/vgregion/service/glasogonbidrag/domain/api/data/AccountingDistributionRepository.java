package se.vgregion.service.glasogonbidrag.domain.api.data;

import org.springframework.stereotype.Repository;
import se.vgregion.portal.glasogonbidrag.domain.jpa.AccountingDistribution;

import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Repository
public interface AccountingDistributionRepository {
    AccountingDistribution find(long id);

    AccountingDistribution findWithParts(long id);

    List<AccountingDistribution> findAll();

    List<AccountingDistribution> findAll(int firstResult, int maxResults);

    List<AccountingDistribution> findAllWithParts();

    List<AccountingDistribution> findAllWithParts(int firstResult,
                                                  int maxResults);
}
