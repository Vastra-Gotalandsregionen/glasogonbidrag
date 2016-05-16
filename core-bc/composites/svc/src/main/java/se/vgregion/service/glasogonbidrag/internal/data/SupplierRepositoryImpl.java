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
    public Supplier find(String name) {
        return em.find(Supplier.class, name);
    }

    @Override
    public List<Supplier> findAll() {
        TypedQuery<Supplier> q = em.createNamedQuery(
                "glasogonbidrag.supplier.findAll", Supplier.class);

        return q.getResultList();
    }
}
