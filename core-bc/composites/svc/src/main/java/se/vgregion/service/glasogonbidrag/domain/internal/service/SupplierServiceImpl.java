package se.vgregion.service.glasogonbidrag.domain.internal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Supplier;
import se.vgregion.service.glasogonbidrag.domain.api.service.SupplierService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Service
public class SupplierServiceImpl implements SupplierService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(SupplierServiceImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void create(Supplier supplier) {
        LOGGER.info("Persisting supplier: {}", supplier);

        em.persist(supplier);
    }

    @Override
    @Transactional
    public void update(Supplier supplier) {
        LOGGER.info("Updating supplier: {}", supplier);

        em.merge(supplier);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Supplier supplier = em.find(Supplier.class, id);

        LOGGER.info("Deleting supplier: {}", supplier);

        em.remove(supplier);
    }

}
