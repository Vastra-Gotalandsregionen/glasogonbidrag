package se.vgregion.service.glasogonbidrag.internal.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Supplier;
import se.vgregion.service.glasogonbidrag.api.data.SupplierRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Repository
public class SupplierRepositoryImpl implements SupplierRepository {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(SupplierRepositoryImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public Supplier find(long id) {
        return em.find(Supplier.class, id);
    }

    @Override
    public List<Supplier> findAll() {
        TypedQuery<Supplier> q = em.createNamedQuery(
                "glasogonbidrag.supplier.findAll", Supplier.class);

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
