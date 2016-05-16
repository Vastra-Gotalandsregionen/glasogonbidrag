package se.vgregion.service.glasogonbidrag.internal.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Identification;
import se.vgregion.service.glasogonbidrag.api.data.IdentificationRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Repository
public class IdentificationRepositoryImpl implements IdentificationRepository {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(IdentificationRepositoryImpl.class);

    @PersistenceContext
    private EntityManager em;

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

        return q.getSingleResult();
    }

    @Override
    public Identification findByLMANumber(String number) {
        TypedQuery<Identification> q = em.createNamedQuery(
                "glasogonbidrag.identification.findByLMANumber",
                Identification.class);
        q.setParameter("number", number);

        return q.getSingleResult();
    }
}
