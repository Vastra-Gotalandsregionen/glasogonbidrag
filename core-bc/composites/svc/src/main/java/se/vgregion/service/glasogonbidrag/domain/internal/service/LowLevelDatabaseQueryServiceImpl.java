package se.vgregion.service.glasogonbidrag.domain.internal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import se.vgregion.portal.glasogonbidrag.domain.dto.BeneficiaryDTO;
import se.vgregion.portal.glasogonbidrag.domain.dto.InvoiceDTO;
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

    public int countInvoices() {
        String query =
                "SELECT COUNT(*) " +
                "FROM Invoice i";

        TypedQuery<Long> q = em.createQuery(query, Long.class);

        Long result = q.getSingleResult();

        LOGGER.info("countInvoices() - " +
                        "The query {} got the result {}",
                query, result);

        return result.intValue();
    }

    public List<InvoiceDTO> listInvoices(
            LowLevelSortOrder sort, int firstResults, int maxResult)
                throws Exception {
        StringBuilder query = new StringBuilder();
        query.append(
                "SELECT new se.vgregion.portal.glasogonbidrag.domain.dto." +
                           "InvoiceDTO( " +
                                "i.id, i.verificationNumber, s.name," +
                                "i.invoiceNumber, i.amount, COUNT(g), " +
                                "i.status, s.name ) " + // TODO: last s.name is wrong should be owner, when this is added...
                "FROM Invoice i " +
                "LEFT JOIN i.supplier s " +
                "LEFT JOIN i.grants g ");

        if (!sort.getFilters().isEmpty()) {
            query.append("WHERE ").append(sort.getFilterString()).append(" ");
        }

        query.append("GROUP BY i.id, s.name ");
        query.append("ORDER BY ").append(sort.toString());

        TypedQuery<InvoiceDTO> q =
                em.createQuery(query.toString(), InvoiceDTO.class);
        q.setFirstResult(firstResults);
        q.setMaxResults(maxResult);

        List<InvoiceDTO> result = q.getResultList();

        LOGGER.info("listInvoices() - The query {} found {} results",
                query, result.size());

        return result;
    }

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
