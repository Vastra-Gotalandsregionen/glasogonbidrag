package se.vgregion.service.glasogonbidrag.api.service;

import org.springframework.stereotype.Service;
import se.vgregion.portal.glasogonbidrag.domain.jpa.AccountingDistribution;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Service
public interface AccountingDistributionService {
    void create(AccountingDistribution distribution);

    void update(AccountingDistribution distribution);

    void delete(Long id);
}
