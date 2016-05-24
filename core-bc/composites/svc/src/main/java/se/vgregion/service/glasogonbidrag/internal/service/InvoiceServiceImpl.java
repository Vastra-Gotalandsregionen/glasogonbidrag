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
    public void create(Invoice invoice) {
        LOGGER.info("Persisting invoice: {}", invoice);

        // Update creation date and modification date
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();

        em.persist(invoice);
    }

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
    public void updateWithGrant(long userId, long groupId, long companyId,
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
