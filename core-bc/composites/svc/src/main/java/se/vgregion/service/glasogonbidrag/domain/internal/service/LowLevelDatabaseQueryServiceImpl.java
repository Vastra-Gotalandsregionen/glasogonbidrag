package se.vgregion.service.glasogonbidrag.domain.internal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import se.vgregion.portal.glasogonbidrag.domain.dto.BeneficiaryDTO;
import se.vgregion.portal.glasogonbidrag.domain.dto.InvoiceDTO;
import se.vgregion.portal.glasogonbidrag.domain.dto.SupplierDTO;
import se.vgregion.portal.glasogonbidrag.domain.dto.SupplierInvoiceDTO;
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
    public int countSuppliers() {
        String query =
                "SELECT COUNT(*) " +
                "FROM Supplier i";

        TypedQuery<Long> q = em.createQuery(query, Long.class);

        Long result = q.getSingleResult();

        LOGGER.debug("countSuppliers() - " +
                        "The query {} got the result {}",
                query, result);

        return result.intValue();
    }

    @Override
    public List<SupplierDTO> listSuppliers(
            LowLevelSortOrder sort, int firstResults, int maxResult)
                throws Exception {
        StringBuilder query = new StringBuilder();
        query.append(
                "SELECT new se.vgregion.portal.glasogonbidrag.domain.dto." +
                           "SupplierDTO( " +
                                "s.id, s.name, s.externalServiceId," +
                                "COUNT(i), s.active ) " +
                "FROM Supplier s " +
                "LEFT JOIN s.invoices i " +
                "GROUP BY s.id " +
                "ORDER BY ");
        query.append(sort.toString());


        TypedQuery<SupplierDTO> q =
                em.createQuery(query.toString(), SupplierDTO.class);

        q.setFirstResult(firstResults);
        q.setMaxResults(maxResult);

        List<SupplierDTO> result = q.getResultList();

        LOGGER.debug("listSuppliers() - The query {} found {} results",
                query, result.size());

        return result;
    }

    public int countInvoices() {
        String query =
                "SELECT COUNT(*) " +
                "FROM Invoice i";

        TypedQuery<Long> q = em.createQuery(query, Long.class);

        Long result = q.getSingleResult();

        LOGGER.debug("countInvoices() - " +
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
                                "i.status, i.caseWorker ) " +
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

        LOGGER.debug("listInvoices() - The query {} found {} results",
                query, result.size());

        return result;
    }

    public int countInvoicesForSupplier(long supplierId) {
        String query =
                "SELECT COUNT(*) " +
                "FROM Invoice i " +
                "WHERE i.supplier.id = :id";

        TypedQuery<Long> q = em.createQuery(query, Long.class);
        q.setParameter("id", supplierId);

        Long result = q.getSingleResult();

        LOGGER.debug("countInvoices() - " +
                        "The query {} got the result {}",
                query, result);

        return result.intValue();
    }

    public List<SupplierInvoiceDTO> listInvoicesForSupplier(
            long supplierId, LowLevelSortOrder sort,
            int firstResults, int maxResult)
                throws Exception {
        StringBuilder query = new StringBuilder();
        query.append(
                "SELECT new se.vgregion.portal.glasogonbidrag.domain.dto." +
                           "SupplierInvoiceDTO( " +
                                "i.id, i.verificationNumber, " +
                                "i.status, i.caseWorker ) " +
                "FROM Invoice i " +
                "LEFT JOIN i.supplier s ");
        query.append(
                "WHERE i.supplier.id = ").append(supplierId).append(" ");

//        if (!sort.getFilters().isEmpty()) {
//            query.append("WHERE ").append(sort.getFilterString()).append(" ");
//        }

        query.append("ORDER BY ").append(sort.toString());

        TypedQuery<SupplierInvoiceDTO> q =
                em.createQuery(query.toString(), SupplierInvoiceDTO.class);
        q.setFirstResult(firstResults);
        q.setMaxResults(maxResult);

        List<SupplierInvoiceDTO> result = q.getResultList();

        LOGGER.debug("listInvoicesForSupplier() - " +
                        "The query {} found {} results",
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

        LOGGER.debug("countBeneficiaries() - " +
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

        LOGGER.debug("listBeneficiaries() - The query {} found {} results",
                query, result.size());

        return result;
    }
}
