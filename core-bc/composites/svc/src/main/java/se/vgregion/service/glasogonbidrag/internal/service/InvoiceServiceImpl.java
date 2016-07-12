package se.vgregion.service.glasogonbidrag.internal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.portal.glasogonbidrag.domain.jpa.GrantAdjustment;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Grant;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.service.glasogonbidrag.api.service.InvoiceService;
import se.vgregion.service.glasogonbidrag.exception.GrantAdjustmentAlreadySetException;
import se.vgregion.service.glasogonbidrag.exception.GrantAlreadyExistException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Service
public class InvoiceServiceImpl implements InvoiceService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(InvoiceServiceImpl.class);

    @PersistenceContext
    private EntityManager em;

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

        // Set user, group and company id of all grants.
        //  Also set creation date and modification date of all grants.
        for (Grant grant : invoice.getGrants()) {
            grant.setUserId(userId);
            grant.setGroupId(groupId);
            grant.setCompanyId(companyId);

            grant.setCreateDate(date);
            grant.setModifiedDate(date);

            grant.setInvoice(invoice);

            em.persist(grant);
        }
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
            throws GrantAlreadyExistException {
        LOGGER.info("Add grant {} to invoice {}", grant, invoice);

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();

        List<Grant> grants = invoice.getGrants();
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

        em.persist(grant);

        this.update(invoice);
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
}
