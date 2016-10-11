package se.vgregion.service.glasogonbidrag.domain.internal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.service.glasogonbidrag.domain.api.service.MigrationService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Service
public class MigrationServiceImpl implements MigrationService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(MigrationServiceImpl.class);

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
