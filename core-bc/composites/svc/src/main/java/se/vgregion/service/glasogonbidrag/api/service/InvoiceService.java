package se.vgregion.service.glasogonbidrag.api.service;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Grant;
import se.vgregion.portal.glasogonbidrag.domain.jpa.GrantAdjustment;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.service.glasogonbidrag.exception.GrantAdjustmentAlreadySetException;
import se.vgregion.service.glasogonbidrag.exception.GrantAlreadyExistException;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public interface InvoiceService {
    void create(long userId, long groupId, long companyId,
                Invoice invoice);

    void update(Invoice invoice);

    void updateAddGrant(long userId, long groupId, long companyId,
                        Invoice invoice, Grant grant)
            throws GrantAlreadyExistException;

    void updateDeleteGrant(Invoice invoice, Long grantId);

    void updateAddGrantAdjustment(long userId, long groupId, long companyId,
                                  Invoice invoice, GrantAdjustment adjustment)
        throws GrantAdjustmentAlreadySetException;

    void delete(Long id);
}
