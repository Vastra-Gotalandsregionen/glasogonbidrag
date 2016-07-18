package se.vgregion.service.glasogonbidrag.domain.internal.data;

import se.vgregion.portal.glasogonbidrag.domain.jpa.AccountingDistribution;
import se.vgregion.service.glasogonbidrag.domain.api.data.AccountingDistributionRepository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class AccountingDistributionRepositoryImpl
        implements AccountingDistributionRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public AccountingDistribution find(long id) {
        return em.find(AccountingDistribution.class, id);
    }

    @Override
    public AccountingDistribution findWithParts(long id) {
        TypedQuery<AccountingDistribution> q =
                em.createNamedQuery(
                        "glasogonbidrag.distribution.findWithParts",
                        AccountingDistribution.class);
        q.setParameter("id", id);

        try {
            return q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<AccountingDistribution> findAll() {
        return findAll(-1, -1);
    }

    @Override
    public List<AccountingDistribution> findAll(int firstResult,
                                                int maxResults) {
        TypedQuery<AccountingDistribution> q =
                em.createNamedQuery("glasogonbidrag.distribution.findAll",
                        AccountingDistribution.class);

        if (firstResult >= 0) {
            q.setFirstResult(firstResult);

            if (maxResults > 0) {
                q.setMaxResults(maxResults);
            }
        }

        return q.getResultList();
    }

    @Override
    public List<AccountingDistribution> findAllWithParts() {
        return findAllWithParts(-1, -1);
    }


    @Override
    public List<AccountingDistribution> findAllWithParts(int firstResult,
                                                         int maxResults) {
        TypedQuery<AccountingDistribution> q =
                em.createNamedQuery(
                        "glasogonbidrag.distribution.findAllWithParts",
                        AccountingDistribution.class);

        if (firstResult >= 0) {
            q.setFirstResult(firstResult);

            if (maxResults > 0) {
                q.setMaxResults(maxResults);
            }
        }

        return q.getResultList();
    }

}
