package se.vgregion.service.glasogonbidrag.domain.internal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import se.vgregion.portal.glasogonbidrag.domain.dto.BeneficiaryDTO;
import se.vgregion.service.glasogonbidrag.domain.api.service.LowLevelDatabaseQueryService;
import se.vgregion.service.glasogonbidrag.types.LowLevelSortOrder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Service
public class LowLevelDatabaseQueryServiceImpl
        implements LowLevelDatabaseQueryService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(LowLevelDatabaseQueryServiceImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public int countBeneficiaries() {
        String query =
                "SELECT COUNT(*) " +
                "FROM Beneficiary b";

        TypedQuery<Long> q = em.createQuery(query, Long.class);

        Long result = q.getSingleResult();

        LOGGER.info("countBeneficiaries() - " +
                        "The query {} got the result {}",
                query, result);

        return result.intValue();
    }

    @Override
    public List<BeneficiaryDTO> listBeneficiaries(
            LowLevelSortOrder sort, int firstResults, int maxResult)
                throws Exception {
        String query =
                "SELECT new se.vgregion.portal.glasogonbidrag.domain.dto." +
                            "BeneficiaryDTO( " +
                                "b.id, i.number, b.firstName, " +
                                "b.lastName, COUNT(g) ) " +
                "FROM Beneficiary b " +
                "LEFT JOIN b.grants g " +
                "LEFT JOIN b.identification i " +
                "GROUP BY b.id, i.number " +
                "ORDER BY " + sort.toString();

        TypedQuery<BeneficiaryDTO> q =
                em.createQuery(query, BeneficiaryDTO.class);
        q.setFirstResult(firstResults);
        q.setMaxResults(maxResult);

        List<BeneficiaryDTO> result = q.getResultList();

        LOGGER.info("listBeneficiaries() - The query {} found {} results",
                query, result.size());

        return result;
    }
}
