package se.vgregion.service.glasogonbidrag.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Supplier;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Repository
public class InvoiceRepository {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(InvoiceRepository.class);

    @PersistenceContext
    private EntityManager em;

    public Invoice find(Long id) {
        return em.find(Invoice.class, id);
    }

    public Invoice findWithParts(Long id) {
        TypedQuery<Invoice> q = em.createNamedQuery(
                "glasogonbidrag.invoice.findWithParts", Invoice.class);
        q.setParameter("id", id);

        return q.getSingleResult();
    }

    public List<Invoice> findAll() {
        TypedQuery<Invoice> q = em.createNamedQuery(
                "glasogonbidrag.invoice.findAll", Invoice.class);

        return q.getResultList();
    }

    public List<Invoice> findAllBySupplier(Supplier supplier) {
        TypedQuery<Invoice> q = em.createNamedQuery(
                "glasogonbidrag.invoice.findAllBySupplier", Invoice.class);
        q.setParameter("supplier", supplier);

        return q.getResultList();
    }

}
