package se.vgregion.service.glasogonbidrag.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Identification;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Service
public class IdentificationService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(IdentificationService.class);

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void create(Identification identification) {
        LOGGER.info("Persisting identification: {}", identification);

        em.persist(identification);
    }

    @Transactional
    public void update(Identification identification) {
        LOGGER.info("Updating identification: {}", identification);

        em.merge(identification);
    }

    @Transactional
    public void delete(Long id) {
        Identification identification = em.find(Identification.class, id);

        LOGGER.info("Deleting identification: {}", identification);

        em.remove(identification);
    }
}
