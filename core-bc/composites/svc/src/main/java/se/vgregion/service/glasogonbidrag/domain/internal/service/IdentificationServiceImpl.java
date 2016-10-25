package se.vgregion.service.glasogonbidrag.domain.internal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Identification;
import se.vgregion.service.glasogonbidrag.domain.api.service.IdentificationService;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

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
    public Identification update(Identification identification) {
        LOGGER.info("Updating identification: {}", identification);

        return em.merge(identification);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Identification identification = em.find(Identification.class, id);

        LOGGER.info("Deleting identification: {}", identification);

        em.remove(identification);
    }

    @Override
    public Identification find(Long id) {
        return em.find(Identification.class, id);
    }

    @Override
    public Identification findByPersonalNumber(String number) {
        TypedQuery<Identification> q = em.createNamedQuery(
                "glasogonbidrag.identification.findByPersonalNumber",
                Identification.class);
        q.setParameter("number", number);

        try {
            return q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Identification findByLMANumber(String number) {
        TypedQuery<Identification> q = em.createNamedQuery(
                "glasogonbidrag.identification.findByLMANumber",
                Identification.class);
        q.setParameter("number", number);

        try {
            return q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
