package se.vgregion.service.glasogonbidrag.domain.internal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Grant;
import se.vgregion.service.glasogonbidrag.domain.api.service.GrantService;
import se.vgregion.service.glasogonbidrag.domain.exception.GrantMissingAreaException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public void create(Grant grant) throws GrantMissingAreaException {
        LOGGER.info("Persisting grant: {}", grant);

        checkGrantArea(grant);

        em.persist(grant);
    }

    @Override
    @Transactional
    public Grant update(Grant grant) throws GrantMissingAreaException {
        LOGGER.info("Updating grant: {}", grant);

        checkGrantArea(grant);

        return em.merge(grant);
    }

    @Override
    @Transactional
    public void delete(long id) {
        Grant grant = find(id);

        LOGGER.info("Deleting grant: {}", grant);

        em.remove(grant);
    }

    @Override
    public Grant find(long id) {
        return id > 0 ? em.find(Grant.class, id) : null;
    }

    @Override
    @Transactional
    public Grant findWithParts(long id) {
        if (id == -1) return null;

        TypedQuery<Grant> q = em.createNamedQuery(
                "glasogonbidrag.grant.findWithParts", Grant.class);
        q.setParameter("id", id);

        Grant result = null;

        try {
            result = q.getSingleResult();
        } catch (NoResultException e) {
            // Ignore exception
        }

        return result;
    }

    @Override
    public List<Grant> findByDate(Date date) {
        TypedQuery<Grant> q = em.createNamedQuery(
                "glasogonbidrag.grant.findAllByDate", Grant.class);
        q.setParameter("date", date, TemporalType.DATE);

        return q.getResultList();
    }

    @Override
    public List<Grant> findByUser(long userId) {
        if (userId == -1) return new ArrayList<>();

        TypedQuery<Grant> q = em.createNamedQuery(
                "glasogonbidrag.grant.findAllByUser", Grant.class);
        q.setParameter("user", userId);

        return q.getResultList();
    }

    @Override
    public List<Grant> findAllByCaseWorker(String caseWorker) {

        TypedQuery<Grant> q = em.createNamedQuery(
                "glasogonbidrag.grant.findAllByCaseWorker", Grant.class);
        q.setParameter("caseWorker", caseWorker);

        return q.getResultList();
    }

    @Override
    public long currentProgressByDate(Date date) {
        TypedQuery<Long> q = em.createNamedQuery(
                "glasogonbidrag.grant.currentProgressByDate",
                Long.class);
        q.setParameter("date", date, TemporalType.DATE);

        Long result = null;
        try {
            result = q.getSingleResult();
        } catch (NoResultException e) {
            // Ignore exception
        }

        if (result != null) {
            return result;
        } else {
            return 0;
        }
    }

    @Override
    public long currentProgressByUserAndDate(long userId, Date date) {
        if (userId == -1) return 0;

        TypedQuery<Long> q = em.createNamedQuery(
                "glasogonbidrag.grant.currentProgressByUserAndDate",
                Long.class);
        q.setParameter("user", userId);
        q.setParameter("date", date, TemporalType.DATE);

        Long result = null;
        try {
            result = q.getSingleResult();
        } catch (NoResultException e) {
            // Ignore exception
        }

        if (result != null) {
            return result;
        } else {
            return 0;
        }
    }

    private void checkGrantArea(Grant grant) throws GrantMissingAreaException {
        if (grant.getMunicipality() == null
                || grant.getMunicipality().trim().isEmpty()
                || grant.getCounty() == null
                || grant.getCounty().trim().isEmpty()) {
            throw new GrantMissingAreaException();
        }
    }
}
