package se.vgregion.service.glasogonbidrag.domain.api.data;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;

import java.util.Date;
import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public interface MigrationRepository {
    List<Date> getMigrations();

    List<Invoice> getInvoices(Date date);
}
