package se.vgregion.service.glasogonbidrag.domain.internal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Grant;
import se.vgregion.service.glasogonbidrag.domain.api.service.GrantService;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Service
public class GrantServiceImpl implements GrantService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(GrantServiceImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void create(Grant grant) {
        LOGGER.info("Persisting grant: {}", grant);

        em.persist(grant);
    }

    @Override
    @Transactional
    public void update(Grant grant) {
        LOGGER.info("Updating grant: {}", grant);

        em.merge(grant);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Grant grant = em.find(Grant.class, id);

        LOGGER.info("Deleting grant: {}", grant);

        em.remove(grant);
    }

}
