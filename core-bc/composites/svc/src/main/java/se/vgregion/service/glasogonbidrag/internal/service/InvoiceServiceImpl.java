package se.vgregion.service.glasogonbidrag.internal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.service.glasogonbidrag.api.service.InvoiceService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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

        em.persist(invoice);
    }

    @Override
    @Transactional
    public void update(Invoice invoice) {
        LOGGER.info("Updating invoice: {}", invoice);

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
