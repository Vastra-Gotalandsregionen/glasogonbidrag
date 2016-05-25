package se.vgregion.service.glasogonbidrag.internal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Grant;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.service.glasogonbidrag.api.service.InvoiceService;
import se.vgregion.service.glasogonbidrag.exception.GrantAlreadyExistException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
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

        // Set user, group and company id of new invoice.
        invoice.setUserId(userId);
        invoice.setGroupId(groupId);
        invoice.setCompanyId(companyId);

        // Update creation date and modification date
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();

        invoice.setCreateDate(date);
        invoice.setModifiedDate(date);

        // Set user, group and company id of all grants.
        //  Also set creation date and modification date of all grants.
        for (Grant grant : invoice.getGrants()) {
            grant.setUserId(userId);
            grant.setGroupId(groupId);
            grant.setCompanyId(companyId);

            grant.setCreateDate(date);
            grant.setModifiedDate(date);
        }

        em.persist(invoice);
    }

    /**
     * Updated the invoice object. Don't use this to persist when adding
     * new grants, to persist all new grants use
     * {@link InvoiceServiceImpl#updateWithGrants}
     * or to add just one grant to the invoice object use
     * {@link InvoiceServiceImpl#updateAddGrant}
     *
     * @param invoice the invoice to update.
     */
    @Override
    @Transactional
    public void update(Invoice invoice) {
        LOGGER.info("Updating invoice: {}", invoice);

        // Update modification date of this invoice
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();

        invoice.setModifiedDate(date);

        em.merge(invoice);
    }

    @Override
    @Transactional
    public void updateWithGrants(long userId, long groupId, long companyId,
                                 Invoice invoice) {
        LOGGER.info("Updating invoice {} with all grants", invoice);

        TypedQuery<Invoice> q = em.createNamedQuery(
                "glasogonbidrag.invoice.findWithGrants", Invoice.class);
        q.setParameter("id", invoice.getId());

        Invoice dbInvoice;
        try {
            dbInvoice = q.getSingleResult();
        } catch (NoResultException e) {
            LOGGER.warn("No result from query.");
            return;
        }

        // Update modification date of this invoice
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();

        invoice.setModifiedDate(date);

        // Set user, group and company id of all new grants.
        //  Also set creation date and modified date of new grants.
        if (dbInvoice.getGrants().size() != invoice.getGrants().size()) {
            LOGGER.info("Number of grants in database differs.");

            List<Grant> dbGrants = dbInvoice.getGrants();

            List<Grant> newGrants = new ArrayList<>();
            for (Grant grant : invoice.getGrants()) {
                if (!dbGrants.contains(grant)) {
                    newGrants.add(grant);
                }
            }

            for (Grant grant : newGrants) {
                LOGGER.info("New grant: {}", grant);
                grant.setUserId(userId);
                grant.setGroupId(groupId);
                grant.setCompanyId(companyId);

                grant.setCreateDate(date);
                grant.setModifiedDate(date);
            }
        }

        em.merge(invoice);
    }

    @Override
    @Transactional
    public void updateAddGrant(long userId, long groupId, long companyId,
                               Invoice invoice, Grant grant)
            throws GrantAlreadyExistException {
        LOGGER.info("Add grant {} to invoice {}", invoice, grant);

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
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();

        grant.setCreateDate(date);
        grant.setModifiedDate(date);

        // Add grant to the invoice object and update it.
        invoice.addGrant(grant);

        this.update(invoice);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Invoice invoice = em.find(Invoice.class, id);

        LOGGER.info("Deleting invoice: {}", invoice);

        em.remove(invoice);
    }

}
