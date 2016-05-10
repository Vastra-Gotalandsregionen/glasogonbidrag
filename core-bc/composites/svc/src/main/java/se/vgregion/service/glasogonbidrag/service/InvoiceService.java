package se.vgregion.service.glasogonbidrag.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Service
public class InvoiceService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(InvoiceService.class);

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void create(Invoice invoice) {
        LOGGER.info("Persisting invoice: {}", invoice);

        em.persist(invoice);
    }

    @Transactional
    public void update(Invoice invoice) {
        LOGGER.info("Updating invoice: {}", invoice);

        em.merge(invoice);
    }

    @Transactional
    public void delete(Long id) {
        Invoice invoice = em.find(Invoice.class, id);

        LOGGER.info("Deleting invoice: {}", invoice);

        em.remove(invoice);
    }
}
