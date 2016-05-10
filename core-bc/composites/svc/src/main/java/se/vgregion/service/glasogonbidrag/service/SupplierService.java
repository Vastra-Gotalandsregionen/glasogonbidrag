package se.vgregion.service.glasogonbidrag.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Supplier;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Service
public class SupplierService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(SupplierService.class);

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void create(Supplier supplier) {
        LOGGER.info("Persisting supplier: {}", supplier);

        em.persist(supplier);
    }

    @Transactional
    public void update(Supplier supplier) {
        LOGGER.info("Updating supplier: {}", supplier);

        em.merge(supplier);
    }

    @Transactional
    public void delete(Long id) {
        Supplier supplier = em.find(Supplier.class, id);

        LOGGER.info("Deleting supplier: {}", supplier);

        em.remove(supplier);
    }
}
