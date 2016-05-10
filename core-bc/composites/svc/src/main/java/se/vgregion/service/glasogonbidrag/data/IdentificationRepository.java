package se.vgregion.service.glasogonbidrag.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Identification;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Repository
public class IdentificationRepository {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(IdentificationRepository.class);

    @PersistenceContext
    private EntityManager em;

    public Identification find(Long id) {
        return em.find(Identification.class, id);
    }

    public Identification findByPersonalNumber(String number) {
        TypedQuery<Identification> q = em.createNamedQuery(
                "glasogonbidrag.identification.findByPersonalNumber",
                Identification.class);
        q.setParameter("number", number);

        return q.getSingleResult();
    }

    public Identification findByLMANumber(String number) {
        TypedQuery<Identification> q = em.createNamedQuery(
                "glasogonbidrag.identification.findByLMANumber",
                Identification.class);
        q.setParameter("number", number);

        return q.getSingleResult();
    }
}
