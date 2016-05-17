package se.vgregion.service.glasogonbidrag.internal.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Supplier;
import se.vgregion.service.glasogonbidrag.api.data.InvoiceRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Repository
public class InvoiceRepositoryImpl implements InvoiceRepository {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(InvoiceRepositoryImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public Invoice find(Long id) {
        return em.find(Invoice.class, id);
    }

    @Override
    public Invoice findWithParts(Long id) {
        TypedQuery<Invoice> q = em.createNamedQuery(
                "glasogonbidrag.invoice.findWithParts", Invoice.class);
        q.setParameter("id", id);

        return q.getSingleResult();
    }

    @Override
    public Invoice findByVerificationNumber(String number) {
        TypedQuery<Invoice> q = em.createNamedQuery(
                "glasogonbidrag.invoice.findByVerificationNumber",
                Invoice.class);
        q.setParameter("number", number);

        return q.getSingleResult();
    }

    @Override
    public List<Invoice> findAll() {
        TypedQuery<Invoice> q = em.createNamedQuery(
                "glasogonbidrag.invoice.findAll", Invoice.class);

        return q.getResultList();
    }

    @Override
    public List<Invoice> findByInvoiceNumber(String number) {
        TypedQuery<Invoice> q = em.createNamedQuery(
                "glasogonbidrag.invoice.findByInvoiceNumber",
                Invoice.class);
        q.setParameter("number", number);

        return q.getResultList();
    }

    @Override
    public List<Invoice> findAllBySupplier(Supplier supplier) {
        TypedQuery<Invoice> q = em.createNamedQuery(
                "glasogonbidrag.invoice.findAllBySupplier", Invoice.class);
        q.setParameter("supplier", supplier);

        return q.getResultList();
    }

}
