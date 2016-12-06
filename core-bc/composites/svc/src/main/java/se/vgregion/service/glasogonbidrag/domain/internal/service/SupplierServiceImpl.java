package se.vgregion.service.glasogonbidrag.domain.internal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.portal.glasogonbidrag.domain.dto.SupplierDTO;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Supplier;
import se.vgregion.service.glasogonbidrag.domain.api.service.SupplierService;
import se.vgregion.service.glasogonbidrag.types.lowlevel.SupplierFilter;
import se.vgregion.service.glasogonbidrag.types.lowlevel.SupplierOrder;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Service
public class SupplierServiceImpl implements SupplierService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(SupplierServiceImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void create(Supplier supplier) {
        LOGGER.info("Persisting supplier: {}", supplier);

        em.persist(supplier);
    }

    @Override
    @Transactional
    public Supplier update(Supplier supplier) {
        LOGGER.info("Updating supplier: {}", supplier);

        return em.merge(supplier);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Supplier supplier = em.find(Supplier.class, id);

        LOGGER.info("Deleting supplier: {}", supplier);

        em.remove(supplier);
    }

    @Override
    public Supplier find(long id) {
        return em.find(Supplier.class, id);
    }

    @Override
    @Transactional
    public Supplier findWithInvoices(long id) {
        TypedQuery<Supplier> q = em.createNamedQuery(
                "glasogonbidrag.supplier.findWithInvoices", Supplier.class);
        q.setParameter("id", id);

        try {
            return q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Supplier> findAll() {
        TypedQuery<Supplier> q = em.createNamedQuery(
                "glasogonbidrag.supplier.findAll", Supplier.class);

        return q.getResultList();
    }

    @Override
    public List<Supplier> findAllWithInvoices() {
        TypedQuery<Supplier> q = em.createNamedQuery(
                "glasogonbidrag.supplier.findAllWithInvoices", Supplier.class);

        return q.getResultList();
    }

    @Override
    public List<Supplier> findAllActive() {
        TypedQuery<Supplier> q = em.createNamedQuery(
                "glasogonbidrag.supplier.findAllActive", Supplier.class);

        return q.getResultList();
    }

    @Override
    public List<Supplier> findAllInactive() {
        TypedQuery<Supplier> q = em.createNamedQuery(
                "glasogonbidrag.supplier.findAllInactive", Supplier.class);

        return q.getResultList();
    }

    @Override
    public List<Supplier> findAllByName(String name) {
        TypedQuery<Supplier> q = em.createNamedQuery(
                "glasogonbidrag.supplier.findAllByName", Supplier.class);
        q.setParameter("name", name);

        return q.getResultList();
    }

    // Filter methods

    public List<SupplierDTO> findAllFiltered(int firstResults,
                                             int maxResult,
                                             SupplierFilter filters,
                                             SupplierOrder order) {
        String query =
                "SELECT new se.vgregion.portal.glasogonbidrag.domain.dto." +
                           "SupplierDTO( " +
                                "s.id, s.name, s.externalServiceId," +
                                "COUNT(i), s.active ) " +
                "FROM Supplier s " +
                "LEFT JOIN s.invoices i ";

        if (filters.hasFilters()) {
            query += buildWhereCondition(filters);
        }

        query += "GROUP BY s.id ";

        if (order.hasOrderBy()) {
            List<String> orderBy = new ArrayList<>();

            if (order.isOrderBySupplierName()) {
                orderBy.add("s.name");
            }

            if (order.isOrderByExternalServiceId()) {
                orderBy.add("s.externalServiceId");
            }

            if (order.isOrderByCount()) {
                orderBy.add("COUNT(i)");
            }

            if (order.isOrderByActive()) {
                orderBy.add("s.active");
            }

            query += "ORDER BY ";
            query += join(orderBy, ", ").concat(" ");
            query += order.getOrderType().toString();
        } else {
            query += "ORDER BY s.id ASC";
        }

        TypedQuery<SupplierDTO> q = em.createQuery(query, SupplierDTO.class);

        q.setFirstResult(firstResults);
        q.setMaxResults(maxResult);

        if (filters.hasFilters()) {
            setQueryParameters(q, filters);
        }

        List<SupplierDTO> result = q.getResultList();

        LOGGER.debug("listSuppliers() - The query {} found {} results",
                query, result.size());

        return result;
    }

    public int countFiltered(SupplierFilter filters) {
        String query =
                "SELECT COUNT(*) " +
                "FROM Supplier s ";

        if (filters.hasFilters()) {
            query += buildWhereCondition(filters);
        }

        TypedQuery<Long> q = em.createQuery(query, Long.class);

        setQueryParameters(q, filters);

        Long result;
        try {
            result = q.getSingleResult();
        } catch (NoResultException e) {
            result = 0L;
        }

        LOGGER.debug("countSuppliers() - " +
                        "The query {} got the result {}",
                query, result);

        return result.intValue();
    }

    private String buildWhereCondition(SupplierFilter filters) {
        List<String> whereCases = new ArrayList<>();

        if (filters.hasSupplierName()) {
            whereCases.add("UPPER(s.name) LIKE " +
                    "CONCAT('%'," +
                    "CONCAT(UPPER(:supplierNameFilter), '%'))");
        }

        if (filters.hasExternalServiceId()) {
            whereCases.add("UPPER(s.externalServiceId) LIKE " +
                    "CONCAT('%'," +
                    "CONCAT(UPPER(:externalServiceIdFilter), '%'))");
        }

        if (filters.hasActive()) {
            whereCases.add("s.active = :activeFilter");
        }

        return "WHERE ".concat(join(whereCases, " AND ")).concat(" ");
    }

    private <T> void setQueryParameters(TypedQuery<T> q,
                                        SupplierFilter filters) {
        if (filters.hasSupplierName()) {
            q.setParameter("supplierNameFilter", filters.getSupplierName());
        }

        if (filters.hasExternalServiceId()) {
            q.setParameter(
                    "externalServiceIdFilter",
                    filters.getExternalServiceId());
        }

        if (filters.hasActive()) {
            q.setParameter("activeFilter", filters.getActive().booleanValue());
        }
    }

    // TODO: This method is copied to many places consolidate in single file.
    private String join(List<String> join, String separator) {
        StringBuilder builder = new StringBuilder();

        Iterator<String> iterator = join.iterator();
        if (iterator.hasNext()) builder.append(iterator.next());
        while (iterator.hasNext())
            builder.append(separator).append(iterator.next());

        return builder.toString();
    }
}
