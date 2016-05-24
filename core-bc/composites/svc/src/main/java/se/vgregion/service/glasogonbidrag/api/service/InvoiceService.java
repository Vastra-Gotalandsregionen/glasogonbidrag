package se.vgregion.service.glasogonbidrag.api.service;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Grant;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.service.glasogonbidrag.exception.GrantAlreadyExistException;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public interface InvoiceService {
    void create(Invoice invoice);

    void update(Invoice invoice);

    void updateWithGrant(long userId, long groupId, long companyId,
                         Invoice invoice, Grant grant)
            throws GrantAlreadyExistException;

    void delete(Long id);
}
