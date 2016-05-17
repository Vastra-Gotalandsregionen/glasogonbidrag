package se.vgregion.service.glasogonbidrag.api.service;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Grant;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public interface InvoiceService {
    void create(Invoice invoice);

    void update(Invoice invoice);

    void delete(Long id);
}
