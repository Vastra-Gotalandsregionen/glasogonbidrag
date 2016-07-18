package se.vgregion.service.glasogonbidrag.domain.api.data;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.portal.glasogonbidrag.domain.InvoiceStatus;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Supplier;

import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public interface InvoiceRepository {
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
