package se.vgregion.service.glasogonbidrag.domain.internal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Supplier;
import se.vgregion.service.glasogonbidrag.domain.api.service.SupplierService;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

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
    public Supplier update(Supplier supplier) {
        LOGGER.info("Updating supplier: {}", supplier);

        return em.merge(supplier);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Supplier supplier = em.find(Supplier.class, id);

        LOGGER.info("Deleting supplier: {}", supplier);

        em.remove(supplier);
    }

    @Override
    public Supplier find(long id) {
        return em.find(Supplier.class, id);
    }

    @Override
    public Supplier findWithInvoices(long id) {
        TypedQuery<Supplier> q = em.createNamedQuery(
                "glasogonbidrag.supplier.findWithInvoices", Supplier.class);
        q.setParameter("id", id);

        try {
            return q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Supplier> findAll() {
        TypedQuery<Supplier> q = em.createNamedQuery(
                "glasogonbidrag.supplier.findAll", Supplier.class);

        return q.getResultList();
    }

    @Override
    public List<Supplier> findAllWithInvoices() {
        TypedQuery<Supplier> q = em.createNamedQuery(
                "glasogonbidrag.supplier.findAllWithInvoices", Supplier.class);

        return q.getResultList();
    }

    @Override
    public List<Supplier> findAllActive() {
        TypedQuery<Supplier> q = em.createNamedQuery(
                "glasogonbidrag.supplier.findAllActive", Supplier.class);

        return q.getResultList();
    }

    @Override
    public List<Supplier> findAllInactive() {
        TypedQuery<Supplier> q = em.createNamedQuery(
                "glasogonbidrag.supplier.findAllInactive", Supplier.class);

        return q.getResultList();
    }

    @Override
    public List<Supplier> findAllByName(String name) {
        TypedQuery<Supplier> q = em.createNamedQuery(
                "glasogonbidrag.supplier.findAllByName", Supplier.class);
        q.setParameter("name", name);

        return q.getResultList();
    }

}
