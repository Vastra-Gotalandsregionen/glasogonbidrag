package se.vgregion.service.glasogonbidrag.domain.internal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Diagnose;
import se.vgregion.service.glasogonbidrag.domain.api.service.DiagnoseService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Service
public class DiagnoseServiceImpl implements DiagnoseService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(DiagnoseServiceImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void create(Diagnose diagnose) {
        LOGGER.debug("Persisting diagnose: {}", diagnose);

        em.persist(diagnose);
    }

    @Override
    @Transactional
    public Diagnose update(Diagnose diagnose) {
        LOGGER.debug("Updating diagnose: {}", diagnose);

        return em.merge(diagnose);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Diagnose diagnose = em.find(Diagnose.class, id);

        LOGGER.debug("Deleting diagnose: {}", diagnose);

        em.remove(diagnose);
    }
}
