package se.vgregion.service.glasogonbidrag.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Supplier;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Service
public class SupplierRepository {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(SupplierRepository.class);

    @PersistenceContext
    private EntityManager em;

    public Supplier find(Long id) {
        return em.find(Supplier.class, id);
    }

    public List<Supplier> findAll() {
        TypedQuery<Supplier> q = em.createNamedQuery(
                "glasogonbidrag.supplier.findAll", Supplier.class);

        return q.getResultList();
    }
}
