package se.vgregion.service.glasogonbidrag.domain.internal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.portal.glasogonbidrag.domain.InvoiceStatus;
import se.vgregion.portal.glasogonbidrag.domain.jpa.*;
import se.vgregion.service.glasogonbidrag.domain.api.service.GrantService;
import se.vgregion.service.glasogonbidrag.domain.api.service.InvoiceService;
import se.vgregion.service.glasogonbidrag.domain.exception.GrantAdjustmentAlreadySetException;
import se.vgregion.service.glasogonbidrag.domain.exception.GrantAlreadyExistException;
import se.vgregion.service.glasogonbidrag.domain.exception.GrantMissingAreaException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Calendar;
import java.util.Date;
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

    @Override
    @Transactional
    public void create(long userId, long groupId, long companyId,
                       Invoice invoice) {
        LOGGER.info("Persisting invoice: {}", invoice);

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();

        // If a grant adjustment exists,
        //   update creation date and modification date of this.
        GrantAdjustment adjustment = invoice.getAdjustment();
        if (adjustment != null) {
            adjustment.setCreateDate(date);
            adjustment.setModifiedDate(date);
        }

        // Set user, group and company id of new invoice.
        invoice.setUserId(userId);
        invoice.setGroupId(groupId);
        invoice.setCompanyId(companyId);

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
    public void update(Invoice invoice) {
        LOGGER.info("Updating invoice: {}", invoice);

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();

        // If there was no grant adjustment before,
        //   add creation date and modification date to this.
        // If there was a grant adjustment before,
        //   update the modification date of this.
        GrantAdjustment adjustment = invoice.getAdjustment();
        if (adjustment != null) {
            GrantAdjustment exists = null;

            if (adjustment.getId() != null) {
                exists = em.find(GrantAdjustment.class, adjustment.getId());
            }

            if (exists == null) {
                adjustment.setCreateDate(date);
                adjustment.setModifiedDate(date);

                em.persist(adjustment);
            } else {
                adjustment.setModifiedDate(date);

                em.merge(adjustment);
            }
        }

        // Update modification date of this invoice
        invoice.setModifiedDate(date);

        em.merge(invoice);
    }

    @Override
    @Transactional
    public void updateAddGrant(long userId, long groupId, long companyId,
                               Invoice invoice, Grant grant)
            throws GrantAlreadyExistException, GrantMissingAreaException {
        LOGGER.info("Add grant {} to invoice {}", grant, invoice);

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();

        Set<Grant> grants = invoice.getGrants();
        if (grants == null) {
            throw new RuntimeException("List of grants is null");
        }

        if (grants.contains(grant)) {
            throw new GrantAlreadyExistException("Grant is already set.");
        }

        // Set user, group and company id of new grant.
        grant.setUserId(userId);
        grant.setGroupId(groupId);
        grant.setCompanyId(companyId);

        // Update creation date and modification date of new grant.
        grant.setCreateDate(date);
        grant.setModifiedDate(date);

        // Setup relation from the grant to the invoice.
        grant.setInvoice(invoice);
        // Add grant to the invoice object and update it.
        invoice.addGrant(grant);

        grantService.create(grant);

        this.update(invoice);
    }

    @Override
    @Transactional
    public void updateAddAccountingDistribution(
            long userId, long groupId, long companyId,
            Invoice invoice, AccountingDistribution distribution) {
        LOGGER.info("Add distribution {} to invoice {}",
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

        this.update(invoice);

        // remove old distribution.
        if (old != null) {
            em.remove(old);
        }
    }

    @Override
    @Transactional
    public void updateDeleteGrant(Invoice invoice, Long grantId) {
        Grant grant = em.find(Grant.class, grantId);

        invoice.removeGrant(grant);
        grant.setInvoice(null);

        LOGGER.info("Deleting grant: {} from invoice: {}", grant, invoice);

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();

        invoice.setModifiedDate(date);

        em.merge(invoice);
        em.remove(grant);
    }

    @Override
    @Transactional
    public void updateAddGrantAdjustment(long userId,
                                         long groupId,
                                         long companyId,
                                         Invoice invoice,
                                         GrantAdjustment adjustment)
            throws GrantAdjustmentAlreadySetException {
        LOGGER.info("Add grant adjustment {} to invoice {}",
                adjustment, invoice);

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();

        if (invoice.getAdjustment() != null) {
            throw new GrantAdjustmentAlreadySetException(
                    "A grant adjustment have already been added.");
        }

        // Setup mapping between invoice and adjustment.
        invoice.setAdjustment(adjustment);
        adjustment.setInvoice(invoice);

        // Set creation date and modification date of new adjustment.
        adjustment.setCreateDate(date);
        adjustment.setModifiedDate(date);

        // Set user, group and company id of new adjustment.
        adjustment.setUserId(userId);
        adjustment.setGroupId(groupId);
        adjustment.setCompanyId(companyId);

        // Set modification date of invoice.
        invoice.setModifiedDate(date);

        em.persist(adjustment);
        em.merge(invoice);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Invoice invoice = em.find(Invoice.class, id);

        LOGGER.info("Deleting invoice: {}", invoice);

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
}
