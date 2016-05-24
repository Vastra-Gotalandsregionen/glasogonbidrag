package se.vgregion.service.glasogonbidrag.internal.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Beneficiary;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Identification;
import se.vgregion.service.glasogonbidrag.api.data.BeneficiaryRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Repository
public class BeneficiaryRepositoryImpl implements BeneficiaryRepository {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(BeneficiaryRepositoryImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public Beneficiary find(Long id) {
        return em.find(Beneficiary.class, id);
    }

    @Override
    public Beneficiary findWithParts(Long id) {
        TypedQuery<Beneficiary> q = em.createNamedQuery(
                "glasogonbidrag.beneficiary.findWithParts", Beneficiary.class);
        q.setParameter("id", id);

        return q.getSingleResult();
    }

    @Override
    public Beneficiary findWithPartsByIdent(Identification identification) {
        TypedQuery<Beneficiary> q = em.createNamedQuery(
                "glasogonbidrag.beneficiary.findWithPartsByIdent",
                Beneficiary.class);
        q.setParameter("id", identification);

        return q.getSingleResult();
    }

    @Override
    public List<Beneficiary> findAll() {
        TypedQuery<Beneficiary> q = em.createNamedQuery(
                "glasogonbidrag.beneficiary.findAll", Beneficiary.class);

        return q.getResultList();
    }
}
