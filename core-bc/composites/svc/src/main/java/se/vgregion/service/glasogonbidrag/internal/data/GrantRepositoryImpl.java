package se.vgregion.service.glasogonbidrag.internal.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Grant;
import se.vgregion.service.glasogonbidrag.api.data.GrantRepository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Repository
public class GrantRepositoryImpl implements GrantRepository {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(GrantRepositoryImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Grant> findByUser(long userId) {
        TypedQuery<Grant> q = em.createNamedQuery(
                "glasogonbidrag.grant.findByUser", Grant.class);
        q.setParameter("user", userId);

        return q.getResultList();
    }

    @Override
    public long currentProgressByUserAndDate(long userId, Date date) {
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
}
