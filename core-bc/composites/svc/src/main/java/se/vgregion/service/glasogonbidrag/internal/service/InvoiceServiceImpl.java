package se.vgregion.service.glasogonbidrag.internal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.portal.glasogonbidrag.domain.jpa.GrantAdjustment;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.service.glasogonbidrag.api.service.InvoiceService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Calendar;
import java.util.Date;

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

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();

        // If a grant adjustment exists,
        //   update creation date and modification date of this.
        GrantAdjustment adjustment = invoice.getAdjustment();
        if (adjustment != null) {
            adjustment.setCreateDate(date);
            adjustment.setModifiedDate(date);

            em.persist(adjustment);
        }

        // Update creation date and modification date
        invoice.setCreateDate(date);
        invoice.setModifiedDate(date);

        em.persist(invoice);
    }

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

        // Update modification date
        invoice.setModifiedDate(date);

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
