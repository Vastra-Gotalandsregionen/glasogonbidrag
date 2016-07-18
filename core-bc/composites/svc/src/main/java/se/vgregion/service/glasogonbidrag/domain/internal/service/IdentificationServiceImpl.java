package se.vgregion.service.glasogonbidrag.domain.internal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Identification;
import se.vgregion.service.glasogonbidrag.domain.api.service.IdentificationService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Service
public class IdentificationServiceImpl implements IdentificationService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(IdentificationServiceImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void create(Identification identification) {
        LOGGER.info("Persisting identification: {}", identification);

        em.persist(identification);
    }

    @Override
    @Transactional
    public void update(Identification identification) {
        LOGGER.info("Updating identification: {}", identification);

        em.merge(identification);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Identification identification = em.find(Identification.class, id);

        LOGGER.info("Deleting identification: {}", identification);

        em.remove(identification);
    }
}
