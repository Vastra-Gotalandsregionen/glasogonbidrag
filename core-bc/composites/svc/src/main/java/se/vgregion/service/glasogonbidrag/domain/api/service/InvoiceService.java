package se.vgregion.service.glasogonbidrag.domain.api.service;

import se.vgregion.portal.glasogonbidrag.domain.InvoiceStatus;
import se.vgregion.portal.glasogonbidrag.domain.jpa.*;
import se.vgregion.service.glasogonbidrag.domain.exception.GrantAdjustmentAlreadySetException;
import se.vgregion.service.glasogonbidrag.domain.exception.GrantAlreadyExistException;
import se.vgregion.service.glasogonbidrag.domain.exception.GrantMissingAreaException;

import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public interface InvoiceService {
    void create(long userId, long groupId, long companyId,
                Invoice invoice);

    void update(Invoice invoice);

    void updateAddGrant(long userId, long groupId, long companyId,
                        Invoice invoice, Grant grant)
            throws GrantAlreadyExistException, GrantMissingAreaException;

    void updateDeleteGrant(Invoice invoice, Long grantId);

    void updateAddGrantAdjustment(long userId, long groupId, long companyId,
                                  Invoice invoice, GrantAdjustment adjustment)
        throws GrantAdjustmentAlreadySetException;

    void updateAddAccountingDistribution(
            long userId, long groupId, long companyId,
            Invoice invoice, AccountingDistribution distribution);

    void delete(Long id);

    Invoice find(Long id);

    Invoice findWithParts(Long id);

    Invoice findByVerificationNumber(String number);

    List<Invoice> findAll();

    List<Invoice> findAll(int firstResult, int maxResults);

    List<Invoice> findAllWithParts();

    List<Invoice> findAllOrderByModificationDate();

    List<Invoice> findAllOrderByModificationDate(
            long userId, int firstResult, int maxResults);

    List<Invoice> findAllBySupplier(Supplier supplier);

    List<Invoice> findAllBySupplier(Supplier supplier,
                                    int firstResult,
                                    int maxResults);

    List<Invoice> findAllByInvoiceNumber(String number);

    List<Invoice> findAllWithStatus(InvoiceStatus status);

    List<Invoice> findAllWithStatus(InvoiceStatus status,
                                    long userId);

    List<Invoice> findAllWithStatus(InvoiceStatus status,
                                    long userId,
                                    int firstResult,
                                    int maxResults);

    List<Invoice> findAllWithStatusOrderByModificationDate(
            InvoiceStatus status);

    List<Invoice> findAllWithStatusOrderByModificationDate(
            InvoiceStatus status, int firstResult, int maxResults);
}
