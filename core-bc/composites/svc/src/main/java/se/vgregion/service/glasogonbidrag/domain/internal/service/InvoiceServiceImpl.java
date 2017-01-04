package se.vgregion.service.glasogonbidrag.domain.internal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.portal.glasogonbidrag.domain.InvoiceStatus;
import se.vgregion.portal.glasogonbidrag.domain.dto.InvoiceDTO;
import se.vgregion.portal.glasogonbidrag.domain.dto.SupplierInvoiceDTO;
import se.vgregion.portal.glasogonbidrag.domain.jpa.*;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Supplier;
import se.vgregion.service.glasogonbidrag.domain.api.service.BeneficiaryService;
import se.vgregion.service.glasogonbidrag.domain.api.service.GrantService;
import se.vgregion.service.glasogonbidrag.domain.api.service.InvoiceService;
import se.vgregion.service.glasogonbidrag.domain.exception.GrantAlreadyExistException;
import se.vgregion.service.glasogonbidrag.domain.exception.GrantMissingAreaException;
import se.vgregion.service.glasogonbidrag.domain.exception.NoIdentificationException;
import se.vgregion.service.glasogonbidrag.types.BeneficiaryIdentificationTuple;
import se.vgregion.service.glasogonbidrag.types.InvoiceBeneficiaryIdentificationTuple;
import se.vgregion.service.glasogonbidrag.types.InvoiceBeneficiaryTuple;
import se.vgregion.service.glasogonbidrag.types.InvoiceGrantTuple;
import se.vgregion.service.glasogonbidrag.types.filter.InvoiceFilter;
import se.vgregion.service.glasogonbidrag.types.filter.InvoiceOrder;
import se.vgregion.service.glasogonbidrag.util.SharedStringMethod;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Service
public class InvoiceServiceImpl implements InvoiceService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(InvoiceServiceImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private GrantService grantService;

    @Autowired
    private BeneficiaryService beneficiaryService;

    @Override
    @Transactional
    public void create(long userId, long groupId, long companyId,
                       String caseWorker,
                       Invoice invoice) {
        LOGGER.debug("Persisting invoice: {}", invoice);

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();

        // Set user, group and company id of new invoice.
        invoice.setUserId(userId);
        invoice.setGroupId(groupId);
        invoice.setCompanyId(companyId);
        invoice.setCaseWorker(caseWorker);

        // Update creation date and modification date
        invoice.setCreateDate(date);
        invoice.setModifiedDate(date);

        em.persist(invoice);
    }

    /**
     * Updated the invoice object. Don't use this to persist when adding
     * new grants. to add one grant to the invoice object use
     * {@link InvoiceServiceImpl#updateAddGrant}
     *
     * @param invoice the invoice to update.
     */
    @Override
    @Transactional
    public Invoice update(String caseWorker, Invoice invoice) {
        LOGGER.debug("Updating invoice: {}", invoice);

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();

        // Update modification date of this invoice
        invoice.setModifiedDate(date);
        invoice.setCaseWorker(caseWorker);

        return em.merge(invoice);
    }

    @Override
    @Transactional
    public InvoiceBeneficiaryIdentificationTuple updateAddGrant(
            long userId, long groupId, long companyId,
            String caseWorker,
            Invoice invoice, Grant grant)
    throws GrantAlreadyExistException,
           GrantMissingAreaException,
           NoIdentificationException {
        LOGGER.debug("Add grant {} to invoice {}", grant, invoice);

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();

        Beneficiary beneficiary = grant.getBeneficiary();
        Prescription prescription = grant.getPrescription();

        Set<Grant> grants = invoice.getGrants();
        if (grants == null) {
            throw new RuntimeException("List of grants is null");
        }

        if (grants.contains(grant)) {
            throw new GrantAlreadyExistException("Grant is already set.");
        }

        updateGrantData(grant, userId, groupId, companyId, caseWorker, date);
        updatePrescriptionData(prescription, userId, groupId, companyId, date);

        // Setup relation from the grant to the invoice and
        // from the invoice to the grant.
        grant.setInvoice(invoice);
        invoice.addGrant(grant);

        beneficiary.getPrescriptionHistory().add(prescription);
        prescription.setBeneficiary(beneficiary);

        em.persist(grant.getPrescription());
        grantService.create(grant);

        // We have modified the invoice, update this to reflect this.
        invoice.setModifiedDate(date);
        beneficiary.setModifiedDate(date);

        BeneficiaryIdentificationTuple transport =
                beneficiaryService.update(beneficiary);
        Invoice newInvoice = this.update(caseWorker, invoice);

        return new InvoiceBeneficiaryIdentificationTuple(
                newInvoice,
                transport.getBeneficiary(),
                transport.getIdentification());
    }

    private void updateGrantData(Grant grant,
                                 long userId,
                                 long groupId,
                                 long companyId,
                                 String caseWorker,
                                 Date date) {
        // Set user, group and company id of new grant.
        grant.setUserId(userId);
        grant.setGroupId(groupId);
        grant.setCompanyId(companyId);
        grant.setCaseWorker(caseWorker);

        // Update creation date and modification date of new grant.
        grant.setCreateDate(date);
        grant.setModifiedDate(date);

    }

    private void updatePrescriptionData(Prescription prescription,
                                        long userId,
                                        long groupId,
                                        long companyId,
                                        Date date) {
        // Set user, group and company id of new prescription.
        prescription.setUserId(userId);
        prescription.setGroupId(groupId);
        prescription.setCompanyId(companyId);

        // Update creation date and modification date of new prescription.
        prescription.setCreateDate(date);
        prescription.setModifiedDate(date);

    }

    @Override
    @Transactional
    public InvoiceGrantTuple updateGrant(String caseWorker,
                                         Invoice invoice, Grant grant)
            throws GrantMissingAreaException {

        AccountingDistribution distribution = invoice.getDistribution();

        invoice.setDistribution(null);

        LOGGER.debug("Updating grant: {} on invoice: {}", grant, invoice);

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();

        invoice.setModifiedDate(date);
        grant.setModifiedDate(date);

        invoice.setCaseWorker(caseWorker);
        grant.setCaseWorker(caseWorker);

        Invoice newInvoice = em.merge(invoice);

        Grant newGrant = grantService.update(grant);

        // remove old distribution.
        if (distribution != null) {
            distribution = em.find(
                    AccountingDistribution.class,
                    distribution.getId());
            em.remove(distribution);
        }

        return new InvoiceGrantTuple(newInvoice, newGrant);
    }

    @Override
    @Transactional
    public InvoiceBeneficiaryTuple updateDeleteGrant(String caseWorker,
                                                     Invoice invoice,
                                                     long grantId) {
        Grant grant = em.find(Grant.class, grantId);

        AccountingDistribution distribution = invoice.getDistribution();
        Beneficiary beneficiary = grant.getBeneficiary();

        // Remove grant from invoice. Invalidate distribution.
        invoice.removeGrant(grant);
        invoice.setDistribution(null);

        Prescription prescription = grant.getPrescription();
        prescription.setBeneficiary(null);

        // Remove invoice from grant. And other mappings
        grant.setInvoice(null);
        grant.setBeneficiary(null);
        grant.setPrescription(null);

        beneficiary.getPrescriptionHistory().remove(prescription);
        beneficiary.getGrants().remove(grant);

        LOGGER.debug("Deleting grant: {} from invoice: {}", grant, invoice);

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();

        // We have modified the invoice, update this to reflect this.
        invoice.setModifiedDate(date);
        invoice.setCaseWorker(caseWorker);

        // Remove grant and prescription.
        em.remove(prescription);
        em.remove(grant);


        Invoice newInvoice = em.merge(invoice); // TODO: wrong number of grants.
                                                // TODO: Understand what I meant when I wrote the above todo?
        Beneficiary newBeneficiary = em.merge(beneficiary);

        // remove old distribution.
        if (distribution != null) {
            distribution = em.find(
                    AccountingDistribution.class,
                    distribution.getId());
            em.remove(distribution);
        }

        return new InvoiceBeneficiaryTuple(newInvoice, newBeneficiary);
    }

    @Override
    @Transactional
    public Invoice updateAddAccountingDistribution(
            long userId, long groupId, long companyId,
            String caseWorker,
            Invoice invoice, AccountingDistribution distribution) {
        // TODO: if the invoice is any other status than IN_PROGRESS throw exception.
        LOGGER.debug("Add distribution {} to invoice {}",
                distribution, invoice);

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();

        AccountingDistribution old = invoice.getDistribution();

        // Set user, group and company id of new distribution
        distribution.setUserId(userId);
        distribution.setGroupId(groupId);
        distribution.setCompanyId(companyId);

        // Update creation date and modification date of the new
        // accounting distribution.
        distribution.setCreateDate(date);
        distribution.setModifiedDate(date);

        // Setup relation from the distribution to the invoice.
        invoice.setDistribution(distribution);

        // Persist all rows for the distribution.
        for (AccountRow row : distribution.getRows()) {
            em.persist(row);
        }

        // Persist the distribution.
        em.persist(distribution);

        Invoice newInvoice = this.update(caseWorker, invoice);

        // remove old distribution.
        if (old != null) {
            AccountingDistribution distributionToRemove =
                    em.find(AccountingDistribution.class, old.getId());
            em.remove(distributionToRemove);
        }

        return newInvoice;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Invoice invoice = em.find(Invoice.class, id);

        LOGGER.debug("Deleting invoice: {}", invoice);

        em.remove(invoice);
    }

    @Override
    public Invoice find(Long id) {
        return em.find(Invoice.class, id);
    }

    @Override
    @Transactional
    public Invoice findWithParts(Long id) {
        TypedQuery<Invoice> q = em.createNamedQuery(
                "glasogonbidrag.invoice.findWithParts", Invoice.class);
        q.setParameter("id", id);

        try {
            return q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Invoice findByVerificationNumber(String number) {
        TypedQuery<Invoice> q = em.createNamedQuery(
                "glasogonbidrag.invoice.findByVerificationNumber",
                Invoice.class);
        q.setParameter("number", number);


        try {
            return q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Invoice> findAll() {
        return findAll(-1, -1);
    }

    @Override
    public List<Invoice> findAll(int firstResult, int maxResults) {
        TypedQuery<Invoice> q = em.createNamedQuery(
                "glasogonbidrag.invoice.findAll", Invoice.class);

        if (firstResult >= 0) {
            q.setFirstResult(firstResult);

            if (maxResults > 0) {
                q.setMaxResults(maxResults);
            }
        }

        return q.getResultList();
    }

    @Override
    @Transactional
    public List<Invoice> findAllWithParts() {
        TypedQuery<Invoice> q = em.createNamedQuery(
                "glasogonbidrag.invoice.findAllWithParts", Invoice.class);
        return q.getResultList();
    }

    @Override
    public List<Invoice> findAllOrderByModificationDate() {
        return findAllOrderByModificationDate(-1, -1, -1);
    }

    @Override
    public List<Invoice> findAllOrderByModificationDate(
            long userId, int firstResult, int maxResults) {
        TypedQuery<Invoice> q;

        if (userId == -1) {
            q = em.createNamedQuery(
                    "glasogonbidrag.invoice.findAllOrderByModificationDate",
                    Invoice.class);
        } else {
            q = em.createNamedQuery(
                    "glasogonbidrag.invoice." +
                            "findAllByUserOrderByModificationDate",
                    Invoice.class);
            q.setParameter("user", userId);
        }

        if (firstResult >= 0) {
            q.setFirstResult(firstResult);

            if (maxResults > 0) {
                q.setMaxResults(maxResults);
            }
        }

        return q.getResultList();
    }

    @Override
    public List<Invoice> findAllByInvoiceNumber(String number) {
        TypedQuery<Invoice> q = em.createNamedQuery(
                "glasogonbidrag.invoice.findAllByInvoiceNumber",
                Invoice.class);
        q.setParameter("number", number);

        return q.getResultList();
    }

    @Override
    public List<Invoice> findAllBySupplier(Supplier supplier) {
        return findAllBySupplier(supplier, -1, -1);
    }

    @Override
    public List<Invoice> findAllBySupplier(Supplier supplier,
                                           int firstResult,
                                           int maxResults) {
        TypedQuery<Invoice> q = em.createNamedQuery(
                "glasogonbidrag.invoice.findAllBySupplier", Invoice.class);
        q.setParameter("supplier", supplier);

        return q.getResultList();
    }

    @Override
    public List<Invoice> findAllWithStatus(InvoiceStatus status) {
        TypedQuery<Invoice> q = em.createNamedQuery(
                "glasogonbidrag.invoice.findAllByStatus", Invoice.class);
        q.setParameter("status", status);

        return q.getResultList();
    }

    @Override
    public List<Invoice> findAllWithStatus(InvoiceStatus status, long userId) {
        TypedQuery<Invoice> q = em.createNamedQuery(
                "glasogonbidrag.invoice.findAllByStatus", Invoice.class);
        q.setParameter("status", status);

        return q.getResultList();
    }

    @Override
    public List<Invoice> findAllWithStatus(InvoiceStatus status,
                                           long userId,
                                           int firstResult,
                                           int maxResults) {
        TypedQuery<Invoice> q;
        if (userId != -1) {
            q = em.createNamedQuery(
                    "glasogonbidrag.invoice.findAllByStatusAndUser",
                    Invoice.class);
            q.setParameter("user", userId);
        } else {
            q = em.createNamedQuery(
                    "glasogonbidrag.invoice.findAllByStatus", Invoice.class);
        }
        q.setParameter("status", status);

        return q.getResultList();
    }

    @Override
    public List<Invoice> findAllWithStatusOrderByModificationDate(
            InvoiceStatus status) {
        return findAllWithStatusOrderByModificationDate(status, -1, -1);
    }

    @Override
    public List<Invoice> findAllByCaseWorker(String caseWorker) {
        TypedQuery<Invoice> q = em.createNamedQuery(
                "glasogonbidrag.invoice" +
                        ".findAllByCaseWorker",
                Invoice.class);
        q.setParameter("caseWorker", caseWorker);

        return q.getResultList();
    }

    @Override
    public List<Invoice> findAllWithStatusOrderByModificationDate(
            InvoiceStatus status, int firstResult, int maxResults) {
        TypedQuery<Invoice> q = em.createNamedQuery(
                "glasogonbidrag.invoice" +
                        ".findAllByStatusOrderByModificationDate",
                Invoice.class);
        q.setParameter("status", status);

        if (firstResult >= 0) {
            q.setFirstResult(firstResult);

            if (maxResults > 0) {
                q.setMaxResults(maxResults);
            }
        }

        return q.getResultList();
    }

    // Filter invoice view specific methods

    public List<InvoiceDTO> findAllFiltered(int firstResults,
                                            int maxResult,
                                            InvoiceFilter filters,
                                            InvoiceOrder order) {
        String query =
                "SELECT new se.vgregion.portal.glasogonbidrag.domain.dto." +
                           "InvoiceDTO( " +
                                "i.id, i.verificationNumber, s.name," +
                                "i.invoiceNumber, i.amount, COUNT(g), " +
                                "i.status, i.caseWorker ) " +
                "FROM Invoice i " +
                "LEFT JOIN i.supplier s " +
                "LEFT JOIN i.grants g ";

        if (filters.hasFilters()) {
            query += buildWhereCondition(filters);
        }

        query += "GROUP BY i.id, s.name ";

        if (order.hasOrderBy()) {

            List<String> orderBy = new ArrayList<>();

            if (order.isOrderByVerificationNumber()) {
                orderBy.add("i.verificationNumber");
            }

            if (order.isOrderBySupplier()) {
                orderBy.add("s.name");
            }

            if (order.isOrderByInvoiceNumber()) {
                orderBy.add("i.invoiceNumber");
            }

            if (order.isOrderByAmount()) {
                orderBy.add("i.amount ");
            }

            if (order.isOrderByCount()) {
                orderBy.add("COUNT(g) ");
            }

            if (order.isOrderByStatus()) {
                orderBy.add("i.status ");
            }

            if (order.isOrderByCaseWorker()) {
                orderBy.add("i.caseWorker ");
            }

            query += "ORDER BY ";
            query += SharedStringMethod.join(orderBy, ", ").concat(" ");
            query += order.getOrderType().toString();
        } else {
            query += "ORDER BY i.id ASC";
        }

        TypedQuery<InvoiceDTO> q = em.createQuery(query, InvoiceDTO.class);

        q.setFirstResult(firstResults);
        q.setMaxResults(maxResult);

        if (filters.hasFilters()) {
            setQueryParameters(q, filters);
        }

        List<InvoiceDTO> result = q.getResultList();

        LOGGER.debug("listInvoices() - The query {} found {} results",
                query, result.size());

        return result;
    }


    public int countFiltered(InvoiceFilter filters) {
        String query =
                "SELECT COUNT(*) " +
                "FROM Invoice i " +
                "LEFT JOIN i.supplier s ";

        if (filters.hasFilters()) {
            query += buildWhereCondition(filters);
        }

        TypedQuery<Long> q = em.createQuery(query, Long.class);

        if (filters.hasFilters()) {
            setQueryParameters(q, filters);
        }

        Long result;
        try {
            result = q.getSingleResult();
        } catch (NoResultException e) {
            result = 0L;
        }

        LOGGER.debug("countInvoices() - " +
                        "The query {} got the result {}",
                query, result);

        return result.intValue();
    }

    @Override
    public List<SupplierInvoiceDTO> findAllBySupplierFiltered(
            long supplierId,
            int firstResults,
            int maxResult,
            InvoiceFilter filters,
            InvoiceOrder order) {
        String query =
                "SELECT new se.vgregion.portal.glasogonbidrag.domain.dto." +
                           "SupplierInvoiceDTO( " +
                                "i.id, i.verificationNumber, " +
                                "i.status, i.caseWorker ) " +
                "FROM Invoice i " +
                "LEFT JOIN i.supplier s ";

        if (filters.hasFilters()) {
            query += buildWhereCondition(filters);
            query += "AND i.supplier.id = :supplierId ";
        } else {
            query += "WHERE i.supplier.id = :supplierId ";
        }

        query += "GROUP BY i.id, s.name ";

        if (order.hasOrderBy()) {
            List<String> orderBy = new ArrayList<>();

            if (order.isOrderByVerificationNumber()) {
                orderBy.add("i.verificationNumber");
            }

            if (order.isOrderByStatus()) {
                orderBy.add("i.status ");
            }

            if (order.isOrderByCaseWorker()) {
                orderBy.add("i.caseWorker ");
            }

            query += "ORDER BY ";
            query += SharedStringMethod.join(orderBy, ", ").concat(" ");
            query += order.getOrderType().toString();
        } else {
            query += "ORDER BY i.id ASC";
        }

        TypedQuery<SupplierInvoiceDTO> q = em.createQuery(query, SupplierInvoiceDTO.class);

        q.setFirstResult(firstResults);
        q.setMaxResults(maxResult);

        if (filters.hasFilters()) {
            setQueryParameters(q, filters);
        }

        q.setParameter("supplierId", supplierId);

        List<SupplierInvoiceDTO> result = q.getResultList();

        LOGGER.debug("listInvoices() - The query {} found {} results",
                query, result.size());

        return result;
    }

    @Override
    public int countBySupplierFiltered(long supplierId,
                                       InvoiceFilter filters) {
        String query =
                "SELECT COUNT(*) " +
                "FROM Invoice i " +
                "LEFT JOIN i.supplier s ";

        if (filters.hasFilters()) {
            query += buildWhereCondition(filters);
            query += "AND i.supplier.id = :supplierId ";
        } else {
            query += "WHERE i.supplier.id = :supplierId ";
        }

        TypedQuery<Long> q = em.createQuery(query, Long.class);

        if (filters.hasFilters()) {
            setQueryParameters(q, filters);
        }

        q.setParameter("supplierId", supplierId);

        Long result;
        try {
            result = q.getSingleResult();
        } catch (NoResultException e) {
            result = 0L;
        }

        LOGGER.debug("countInvoices() - " +
                        "The query {} got the result {}",
                query, result);

        return result.intValue();
    }

    private String buildWhereCondition(InvoiceFilter filters) {
        List<String> whereCases = new ArrayList<>();

        if (filters.hasVerificationNumberFilter()) {
            whereCases.add("UPPER(i.verificationNumber) LIKE " +
                    "CONCAT('%'," +
                    "CONCAT(UPPER(:verificationNumberFilter), '%'))");
        }

        if (filters.hasSupplierFilter()) {
            whereCases.add("UPPER(s.name) LIKE " +
                    "CONCAT('%'," +
                    "CONCAT(UPPER(:supplierFilter), '%'))");
        }

        if (filters.hasInvoiceNumberFilter()) {
            whereCases.add("UPPER(i.invoiceNumber) LIKE " +
                    "CONCAT('%'," +
                    "CONCAT(UPPER(:invoiceNumberFilter), '%'))");
        }

        if (filters.hasStatusFilter()) {
            whereCases.add("i.status = :statusFilter");
        }


        if (filters.hasCaseWorkerFilter()) {
            whereCases.add("UPPER(i.caseWorker) LIKE " +
                    "CONCAT('%'," +
                    "CONCAT(UPPER(:caseWorkerFilter), '%'))");
        }

        return "WHERE ".concat(
                SharedStringMethod.join(whereCases, " AND ")).concat(" ");
    }

    private <T> void setQueryParameters(TypedQuery<T> q,
                                        InvoiceFilter filters) {
        if (filters.hasVerificationNumberFilter()) {
            q.setParameter(
                    "verificationNumberFilter",
                    filters.getVerificationNumber());
        }

        if (filters.hasSupplierFilter()) {
            q.setParameter(
                    "supplierFilter",
                    filters.getSupplier());
        }

        if (filters.hasInvoiceNumberFilter()) {
            q.setParameter(
                    "invoiceNumberFilter",
                    filters.getInvoiceNumber());
        }

        if (filters.hasStatusFilter()) {
            q.setParameter(
                    "statusFilter",
                    filters.getStatus());
        }

        if (filters.hasCaseWorkerFilter()) {
            q.setParameter(
                    "caseWorkerFilter",
                    filters.getCaseWorker());
        }
    }
}
