package se.vgregion.service.glasogonbidrag.domain.internal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.portal.glasogonbidrag.domain.InvoiceStatus;
import se.vgregion.portal.glasogonbidrag.domain.jpa.*;
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

    @Autowired
    private BeneficiaryService beneficiaryService;

    @Override
    @Transactional
    public void create(long userId, long groupId, long companyId,
                       String caseWorker,
                       Invoice invoice) {
        LOGGER.info("Persisting invoice: {}", invoice);

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
        LOGGER.info("Updating invoice: {}", invoice);

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
        LOGGER.info("Add grant {} to invoice {}", grant, invoice);

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

        LOGGER.info("Updating grant: {} on invoice: {}", grant, invoice);

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

        LOGGER.info("Deleting grant: {} from invoice: {}", grant, invoice);

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
}
