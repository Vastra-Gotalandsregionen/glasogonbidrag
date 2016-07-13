package se.vgregion.service.glasogonbidrag.internal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.portal.glasogonbidrag.domain.jpa.AccountingDistribution;
import se.vgregion.service.glasogonbidrag.api.service.AccountingDistributionService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class AccountingDistributionServiceImpl
        implements AccountingDistributionService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(AccountingDistributionServiceImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void create(AccountingDistribution distribution) {
        LOGGER.info("Persisting distribution: {}", distribution);

        em.persist(distribution);
    }

    @Override
    @Transactional
    public void update(AccountingDistribution distribution) {
        LOGGER.info("Updating distribution: {}",distribution);

        em.merge(distribution);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        AccountingDistribution distribution =
                em.find(AccountingDistribution.class, id);

        LOGGER.info("Deleting distribution: {}", distribution);

        em.remove(distribution);
    }

}
