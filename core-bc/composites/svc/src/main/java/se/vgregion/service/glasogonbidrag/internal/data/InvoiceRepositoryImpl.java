package se.vgregion.service.glasogonbidrag.internal.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.portal.glasogonbidrag.domain.InvoiceStatus;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Supplier;
import se.vgregion.service.glasogonbidrag.api.data.InvoiceRepository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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


        try {
            return q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Invoice findByVerificationNumber(String number) {
        TypedQuery<Invoice> q = em.createNamedQuery(
                "glasogonbidrag.invoice.findByVerificationNumber",
                Invoice.class);
        q.setParameter("number", number);


        try {
            return q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Invoice> findAll() {
        return findAll(-1, -1);
    }

    @Override
    public List<Invoice> findAll(int firstResult, int maxResults) {
        TypedQuery<Invoice> q = em.createNamedQuery(
                "glasogonbidrag.invoice.findAll", Invoice.class);

        if (firstResult >= 0) {
            q.setFirstResult(firstResult);

            if (maxResults > 0) {
                q.setMaxResults(maxResults);
            }
        }

        return q.getResultList();
    }

    @Override
    public List<Invoice> findAllWithParts() {
        TypedQuery<Invoice> q = em.createNamedQuery(
                "glasogonbidrag.invoice.findAllWithParts", Invoice.class);
        return q.getResultList();
    }

    @Override
    public List<Invoice> findAllOrderByModificationDate() {
        return findAllOrderByModificationDate(-1, -1, -1);
    }

    @Override
    public List<Invoice> findAllOrderByModificationDate(
            long userId, int firstResult, int maxResults) {
        TypedQuery<Invoice> q;

        if (userId == -1) {
            q = em.createNamedQuery(
                    "glasogonbidrag.invoice.findAllOrderByModificationDate",
                    Invoice.class);
        } else {
            q = em.createNamedQuery(
                    "glasogonbidrag.invoice." +
                            "findAllByUserOrderByModificationDate",
                    Invoice.class);
            q.setParameter("user", userId);
        }

        if (firstResult >= 0) {
            q.setFirstResult(firstResult);

            if (maxResults > 0) {
                q.setMaxResults(maxResults);
            }
        }

        return q.getResultList();
    }

    @Override
    public List<Invoice> findAllByInvoiceNumber(String number) {
        TypedQuery<Invoice> q = em.createNamedQuery(
                "glasogonbidrag.invoice.findAllByInvoiceNumber",
                Invoice.class);
        q.setParameter("number", number);

        return q.getResultList();
    }

    @Override
    public List<Invoice> findAllBySupplier(Supplier supplier) {
        return findAllBySupplier(supplier, -1, -1);
    }

    @Override
    public List<Invoice> findAllBySupplier(Supplier supplier,
                                           int firstResult,
                                           int maxResults) {
        TypedQuery<Invoice> q = em.createNamedQuery(
                "glasogonbidrag.invoice.findAllBySupplier", Invoice.class);
        q.setParameter("supplier", supplier);

        return q.getResultList();
    }

    @Override
    public List<Invoice> findAllWithStatus(InvoiceStatus status) {
        TypedQuery<Invoice> q = em.createNamedQuery(
                "glasogonbidrag.invoice.findAllByStatus", Invoice.class);
        q.setParameter("status", status);

        return q.getResultList();
    }

    @Override
    public List<Invoice> findAllWithStatus(InvoiceStatus status, long userId) {
        TypedQuery<Invoice> q = em.createNamedQuery(
                "glasogonbidrag.invoice.findAllByStatus", Invoice.class);
        q.setParameter("status", status);

        return q.getResultList();
    }

    @Override
    public List<Invoice> findAllWithStatus(InvoiceStatus status,
                                           long userId,
                                           int firstResult,
                                           int maxResults) {
        TypedQuery<Invoice> q;
        if (userId != -1) {
            q = em.createNamedQuery(
                    "glasogonbidrag.invoice.findAllByStatusAndUser",
                    Invoice.class);
            q.setParameter("user", userId);
        } else {
            q = em.createNamedQuery(
                    "glasogonbidrag.invoice.findAllByStatus", Invoice.class);
        }
        q.setParameter("status", status);

        return q.getResultList();
    }

    @Override
    public List<Invoice> findAllWithStatusOrderByModificationDate(
            InvoiceStatus status) {
        return findAllWithStatusOrderByModificationDate(status, -1, -1);
    }

    @Override
    public List<Invoice> findAllWithStatusOrderByModificationDate(
            InvoiceStatus status, int firstResult, int maxResults) {
        TypedQuery<Invoice> q = em.createNamedQuery(
                "glasogonbidrag.invoice" +
                        ".findAllByStatusOrderByModificationDate",
                Invoice.class);
        q.setParameter("status", status);

        if (firstResult >= 0) {
            q.setFirstResult(firstResult);

            if (maxResults > 0) {
                q.setMaxResults(maxResults);
            }
        }

        return null;
    }
}
