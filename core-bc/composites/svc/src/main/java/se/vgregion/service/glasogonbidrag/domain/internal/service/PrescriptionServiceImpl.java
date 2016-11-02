package se.vgregion.service.glasogonbidrag.domain.internal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Beneficiary;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Prescription;
import se.vgregion.service.glasogonbidrag.domain.api.service.PrescriptionService;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Service
public class PrescriptionServiceImpl implements PrescriptionService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(PrescriptionServiceImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public Prescription find(long id) {
        return em.find(Prescription.class, id);
    }

    @Override
    @Transactional
    public Prescription findLatest(Beneficiary beneficiary) {
        TypedQuery<Prescription> q = em.createNamedQuery(
                "glasogonbidrag.prescription.findLatest", Prescription.class);
        q.setParameter("beneficiary", beneficiary);
        q.setMaxResults(1);

        try {
            return q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }



}
