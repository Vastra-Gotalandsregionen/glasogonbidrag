package se.vgregion.service.glasogonbidrag.internal.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.service.glasogonbidrag.api.data.MigrationRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Repository
public class MigrationRepositoryImpl implements MigrationRepository {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(MigrationRepositoryImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Date> getMigrations() {
        TypedQuery<Date> q = em.createNamedQuery(
                "glasogonbidrag.migration.findAllDates",
                Date.class);

        return q.getResultList();
    }

    @Override
    public List<Invoice> getInvoices(Date date) {
        TypedQuery<Invoice> q = em.createNamedQuery(
                "glasogonbidrag.migration.findAllWithPartsByDate",
                Invoice.class);
        q.setParameter("date", date);

        return q.getResultList();
    }

}
